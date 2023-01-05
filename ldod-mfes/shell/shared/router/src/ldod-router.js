import { ldodEventPublisher, ldodEventSubscriber } from './events-module';
import { addEndSlash, addStartSlash, isSlash, PATH_REGEX, removeEndSlash } from './utils';
export default class LdodRouter extends HTMLElement {
	constructor() {
		super();
		this.shadow && this.attachShadow({ mode: 'open' });
	}

	static get observedAttributes() {
		return ['language'];
	}

	get shadow() {
		return this.getAttribute('shadow');
	}

	get self() {
		return this.shadow ? this.shadowRoot : this;
	}

	get location() {
		return removeEndSlash(location.pathname);
	}

	get route() {
		return addStartSlash(removeEndSlash(this.getAttribute('route'))) ?? '';
	}

	get base() {
		return addStartSlash(removeEndSlash(this.getAttribute('base'))) ?? '';
	}

	get routerPathname() {
		return removeEndSlash(`/${this.base}${this.route}`)?.replace(/\/\/+/g, '/');
	}

	get outlet() {
		if (this.self.querySelector('ldod-outlet')) return this.self.querySelector('ldod-outlet');
		const outlet = document.createElement('ldod-outlet');
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
		let target = PATH_REGEX.exec(this.normalizePath(path)).at(0);
		let current = removeEndSlash(this.normalizePath(this.active.path));
		return current === target;
	};

	isFromThisRouter = path => {
		if (!this.route) return true;
		const target = this.base ? path.replace(this.base, '') : path;
		return target.startsWith(this.route);
	};

	async connectedCallback() {
		if (!this.id) throw new Error('Each router must have an unique ID');
		if (!this.routes && !this.index) return;
		this.processRoutes();
		this.self.append(this.outlet);
		this.addEventListeners();
		this.navigate();
	}

	processRoutes() {
		if (!this.routes) {
			this.routes = {};
			return;
		}

		this.routes = Object.entries(this.routes).reduce((prev, [key, api]) => {
			let path = removeEndSlash(`/${this.base}/${this.route}/${key}`.replace(/\/\/+/g, '/'));
			prev[path] = api;
			return prev;
		}, {});
	}

	attributeChangedCallback(name, oldV, newV) {
		if (name === 'language' && oldV && oldV !== newV) this.handleLanguageChange(newV);
	}

	handleLanguageChange(language) {
		this.self.querySelectorAll('[language]').forEach(ele => ele.setAttribute('language', language));
	}

	disconnectedCallback() {
		this.unsubURL();
		window.removeEventListener('popstate', this.handlePopstate);
	}

	addEventListeners() {
		this.unsubURL = ldodEventSubscriber('url-changed', this.handleURLChanged);
		window.addEventListener('popstate', this.handlePopstate);
	}

	navigate = (path = this.location, state = {}) => {
		if (this.isPathActive(path)) return;
		if (this.location !== path) history.pushState(state, undefined, path);
		this.render();
	};

	handleURLChanged = ({ payload: { path, state } }) => {
		if (path && this.isFromThisRouter(path)) this.navigate(this.getFullPath(path), state);
	};

	handlePopstate = e => {
		this.isFromThisRouter(this.location) && this.navigate(this.location);
	};

	async render() {
		let route = await this.getRoute();
		ldodEventPublisher('loading', true);
		await this.appendMFE(route);
		ldodEventPublisher('loading', false);
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
							return +(pathBlength === locationLength) - +(pathAlength === locationLength);
						})[0]?.[1];
		if (!targetPath) targetPath = this.fallback;
		if (typeof targetPath === 'function') {
			await this.processPathVariables(targetPath);
		}
		return targetPath;
	}

	processPathVariables = async api => {
		const subPaths = (await api()).path.split('/');
		const newHistoryState = subPaths.reduce(
			(state, subPath, index) => {
				if (subPath.startsWith(':')) {
					const key = subPath.replace(':', '');
					const value = addStartSlash(this.location.replace(this.routerPathname, '')).split('/')[index];
					return { ...state, [key]: value };
				}
				return state;
			},
			{ ...history.state }
		);
		if (newHistoryState) history.replaceState(newHistoryState, '');
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

customElements.define('ldod-router', LdodRouter);
