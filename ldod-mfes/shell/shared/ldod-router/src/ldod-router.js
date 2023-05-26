/** @format */

import { ldodEventPublisher, ldodEventSubscriber } from '../../ldod-event-bus/src/helpers';
import { navigateTo } from './nav-to';
import { addEndSlash, addStartSlash, isSlash, PATH_PATTERN, removeEndSlash } from './utils';

export let BASE_PATH;
export default class LdodRouter extends HTMLElement {
	constructor() {
		super();
		this.routes = {};
		this.shadow && this.attachShadow({ mode: 'open' });
	}

	static get observedAttributes() {
		return ['language', 'base'];
	}

	get shadow() {
		return this.hasAttribute('shadow');
	}

	get self() {
		return this.shadow ? this.shadowRoot : this;
	}

	get location() {
		return removeEndSlash(location.pathname);
	}

	get route() {
		return addStartSlash(removeEndSlash(this.getAttribute('route'))) || '';
	}

	get base() {
		return addStartSlash(removeEndSlash(BASE_PATH)) || '';
	}

	get routerPathname() {
		return removeEndSlash(`/${this.base}${this.route}`)?.replace(/\/\/+/g, '/');
	}

	get outlet() {
		let outlet = this.self.querySelector('ldod-outlet');
		if (!outlet) outlet = document.createElement('ldod-outlet');
		outlet.id = `${this.id}-outlet`;
		return outlet;
	}

	get language() {
		return this.getAttribute('language');
	}

	set language(language) {
		this.setAttribute('language', language);
	}

	getFullPath = path => removeEndSlash(`/${this.base}/${path ?? ''}`.replace(/\/\/+/g, '/'));

	normalizePath = pathname =>
		isSlash(this.routerPathname)
			? pathname
			: addEndSlash(pathname).replace(`${this.routerPathname}/`, '/').replace(/\/\/+/g, '/');

	isPathActive = path => {
		if (!this.active) return false;
		let target = PATH_PATTERN.exec(this.normalizePath(path)).at(0);
		let current = removeEndSlash(this.normalizePath(this.active.path));
		return current === target;
	};

	isFromThisRouter = path => {
		if (!this.route) return true;
		const target = this.base ? path.replace(this.base, '') : path;
		return target.startsWith(this.route);
	};

	async connectedCallback() {
		this.addEventListeners();
		if (!this.id) throw new Error('Each router must have an unique ID');
		this.self.append(this.outlet);
		await this.processRoutes();
	}

	async processRoutes(routes = this.routes) {
		this.routes = Object.entries(routes).reduce((prev, [key, api]) => {
			let path = removeEndSlash(`/${this.base}/${this.route}/${key}`.replace(/\/\/+/g, '/'));
			prev[path] = api;
			return prev;
		}, {});
		await this.navigate();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onAttributeChange[name](oldV, newV);
	}

	onAttributeChange = {
		language: (oldV, newV) => oldV && oldV !== newV && this.handleLanguageChange(newV),
		base: (_, basePath) => {
			if (!BASE_PATH && basePath) BASE_PATH = basePath;
		},
	};

	handleLanguageChange(language) {
		this.self
			.querySelectorAll('[language]')
			.forEach(ele => ele.setAttribute('language', language));
	}

	disconnectedCallback() {
		this.unsubURL();
		window.removeEventListener('popstate', this.handlePopstate);
	}

	addEventListeners() {
		this.addEventListener('error', onError);
		this.unsubURL = ldodEventSubscriber('url-changed', this.handleURLChanged);
		window.addEventListener('popstate', this.handlePopstate);
	}

	navigate = async (path = this.location, state = {}) => {
		if (this.isPathActive(path)) return;
		if (this.location !== path) history.pushState(state, undefined, path);
		await this.render();
	};

	handleURLChanged = async ({ payload: { path, state } }) => {
		if (path && this.isFromThisRouter(path)) await this.navigate(this.getFullPath(path), state);
	};

	handlePopstate = async () =>
		this.isFromThisRouter(this.location) && (await this.navigate(this.location));

	async render() {
		let route = await this.getRoute();
		ldodEventPublisher('loading', true);
		try {
			await this.appendMFE(route);
		} catch (error) {
			onError(error);
		} finally {
			ldodEventPublisher('loading', false);
		}
	}

	isARouteMatch = path => {
		const pathSplit = path.split('/');
		const locationSplit = this.location.split('/');
		return pathSplit.every((subpath, index) =>
			!subpath.startsWith(':') ? locationSplit[index] === subpath : true
		);
	};

	async getRoute() {
		let targetPath =
			this.location === this.routerPathname
				? this.index
				: Object.entries(this.routes)
						.filter(([path, api]) => this.isARouteMatch(path) && api)
						.sort(([pathA], [pathB]) => {
							const locationLength = this.location.split('/').length;
							const pathAlength = pathA?.split('/').length;
							const pathBlength = pathB?.split('/').length;
							return (
								+(pathBlength === locationLength) -
								+(pathAlength === locationLength)
							);
						})[0]?.[1] ?? this.fallback;
		if (typeof targetPath === 'function') this.updateRoutingParameters(await targetPath());
		return targetPath;
	}

	updateRoutingParameters = api => {
		const newState = this.updateHistoryState(api.path.split('/'));
		if (newState) history.replaceState(newState, '');
	};

	/**
	 *
	 * @param {Array<string>} subPaths
	 * @returns
	 */
	updateHistoryState = subPaths => {
		let updatedHistoryState = { ...history.state };
		subPaths.forEach((subPath, index) => {
			if (!subPath.startsWith(':')) return;
			const key = subPath.replace(':', '');
			const value = addStartSlash(this.location.replace(this.routerPathname, '')).split('/')[
				index
			];
			updatedHistoryState = { ...updatedHistoryState, [key]: value };
		});
		return updatedHistoryState;
	};

	async appendMFE(route) {
		if (!route) return;
		this.active && (await this.removeMFE());
		this.active = await route();
		await this.active.mount(this.language, `#${this.outlet.id}`);
	}

	async removeMFE() {
		await this.active?.unMount();
		this.outlet.innerHTML = '';
	}
}

!customElements.get('ldod-router') && customElements.define('ldod-router', LdodRouter);

function onError(e) {
	console.error(e);
	ldodEventPublisher('error');
	navigateTo('/');
}
