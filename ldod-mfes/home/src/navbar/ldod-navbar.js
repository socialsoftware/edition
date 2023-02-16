/** @format */

let Collapse;

import hostStyle from './style/host-style.css?inline';
import rootBS from '@shared/bootstrap/root-css.js';
import navbarBS from '@shared/bootstrap/navbar-css.js';
import navBS from '@shared/bootstrap/nav-css.js';
import dropdownBS from '@shared/bootstrap/dropdown-css.js';
import { ldodEventSubscriber } from '@shared/ldod-events.js';

import './li-dropdown/li-dropdown';
import container from './style/container.css?inline';
import navbar from './style/navbar.css?inline';
import dropdown from './style/dropdown.css?inline';

import './li-lang-menu';
import navbarHtml from './navbar-html';
import constants from './constants';
import { getDropItems } from './li-dropdown/li-dropdown-html';
import { virtualReferences } from '../external-deps';

(await import('user').catch(e => console.error(e)))?.default.bootstrap();

const sheet = new CSSStyleSheet();
sheet.replaceSync(
	rootBS + navbarBS + navBS + dropdownBS + hostStyle + container + navbar + dropdown
);

const loadBootstrapJSModules = async () => {
	Collapse = (await import('@shared/bootstrap/collapse.js')).default;
};
const DEFAULT_SELECTED_VE = [];

export class LdodNavbar extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.shadowRoot.innerHTML = `${navbarHtml(this.language)}`;
		this.selectedVE = DEFAULT_SELECTED_VE;
	}
	static get observedAttributes() {
		return ['language'];
	}
	get language() {
		return this.getAttribute('language') || 'en';
	}

	get dropdowns() {
		return this.shadowRoot.querySelectorAll("li[is='drop-down']");
	}

	get userComponents() {
		return this.shadowRoot.querySelectorAll("li[is='user-component']");
	}

	get collapseToggler() {
		return this.shadowRoot.querySelector('button[data-bs-toggle="collapse"]');
	}

	get dropItems() {
		return Array.from(this.shadowRoot.querySelectorAll('.dropdown-menu a.dropdown-item'));
	}

	get editionDropdown() {
		return this.shadowRoot.querySelector("[key='editions']");
	}
	get adminDropdown() {
		return this.shadowRoot.querySelector("[key='admin']");
	}
	get isAdmin() {
		return this.user && this.user.roles.includes('ROLE_ADMIN');
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name](oldValue, newValue);
	}

	handleChangedAttribute = {
		language: (oldV, newV) => oldV && oldV !== newV && this.onLanguageChange(),
	};

	onLanguageChange() {
		this.shadowRoot.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', this.language);
		});
		this.shadowRoot.querySelectorAll('[data-navbar-key]').forEach(ele => {
			ele.textContent = constants[this.language][ele.dataset.navbarKey];
		});
	}

	connectedCallback() {
		this.addEventBusListeners();
		window.addEventListener('pointermove', this.render, { once: true });
		this.setAdminVisibility();
	}

	disconnectedCallback() {
		this.unsubSelectedVe?.();
		this.unsubLogout?.();
		this.unsubLogin?.();
	}
	render = async () => {
		await this.createInstances();
		this.addEventListeners();
	};
	createInstances = async () => {
		await loadBootstrapJSModules();
		this.collapses = Array.from(this.shadowRoot.querySelectorAll('.collapse')).map(
			collapse => new Collapse(collapse, { toggle: false })
		);
	};

	addEventBusListeners() {
		this.unsubSelectedVe = ldodEventSubscriber('selected-ve', this.addSelectedVE);
		this.unsubLogout = ldodEventSubscriber('logout', this.onUserLogout);
		this.unsubLogin = ldodEventSubscriber('login', this.onUserLogin);
	}

	addEventListeners() {
		this.collapseToggler.addEventListener('click', this.onToggler);
		document.addEventListener('click', e => {
			if (!this.contains(e.target)) this.toggleDropdownMenu(e);
		});
		this.shadowRoot.addEventListener('click', this.toggleDropdownMenu);
		this.shadowRoot.addEventListener('click', e => {
			if (e.target.classList.contains('dropdown-item'))
				this.shadowRoot.querySelector('#navbar-nav').classList.remove('show');
		});
	}

	toggleDropdownMenu = e => {
		const key = e.target.getAttribute('key');
		this.dropdowns.forEach(drop => {
			if (drop.key === key) return drop.toggleAttribute('show');
			drop.toggleAttribute('show', false);
		});
		this.userComponents.forEach(userC => {
			if (userC.getAttribute('key') === key) return this.toggleUserComponentMenu(userC);
			this.toggleUserComponentMenu(userC, false);
		});
	};

	onToggler = () => this.collapses.forEach(element => element.toggle());

	toggleUserComponentMenu = (userC, force = undefined) => {
		if (force === undefined)
			return userC
				.querySelectorAll('a.dropdown-toggle, ul.dropdown-menu')
				.forEach(ele => ele.classList.toggle('show'));

		userC
			.querySelectorAll('a.dropdown-toggle, ul.dropdown-menu')
			.forEach(ele => ele.classList.toggle('show', force));
	};

	onDropdownItem = e => e.button === 2 && e.preventDefault();

	onUserLogin = ({ payload }) => {
		if (!this.user) {
			this.selectedVE = payload.selectedVE;
			this.updateVE();
		}
		this.user = payload;
		this.setAdminVisibility();
	};

	onUserLogout = () => {
		this.user && this.setAdminVisibility(true);
		this.user && this.removeEditions();
		this.user = undefined;
		this.selectedVE = DEFAULT_SELECTED_VE;
		this.updateVE();
	};

	setAdminVisibility = (hide = !this.isAdmin) => {
		this.adminDropdown.hidden = hide;
	};

	addSelectedVE = ({ payload }) => {
		this.selectedVE = payload.selected
			? [...this.selectedVE, payload.name]
			: this.selectedVE.filter(acr => acr !== payload.name);
		this.updateVE();
	};

	updateVE() {
		this.removeEditions();
		this.addEditions();
	}

	addEditions() {
		const template = document.createElement('template');
		template.innerHTML = /*html*/ `
			${this.selectedVE
				.map(
					ve => /*html*/ `
					<li selected>
						<a
							class="dropdown-item"
							is="nav-to"
							to="${virtualReferences?.virtualEdition?.(ve) || ''}"
							id="${ve.toLowerCase()}"
						>
							${ve}
						</a>
					</li>
					`
				)
				.join('')}
		`;
		this.editionDropdown
			.querySelector('ul.dropdown-menu')
			.appendChild(template.content.cloneNode(true));
	}

	removeEditions() {
		this.editionDropdown.querySelectorAll('li[selected]').forEach(li => li.remove());
	}
}
customElements.define('ldod-navbar', LdodNavbar);
