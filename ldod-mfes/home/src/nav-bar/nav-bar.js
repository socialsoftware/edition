/** @format */

import './li-lang-drop';
import '@shared/dropdown/li-dropdown.js';
import { ldodEventBus, ldodEventSubscriber } from '@shared/ldod-events.js';
import transitionsCss from '@shared/bootstrap/transitions-css.js';
import headerSchema from './header-data-schema.json';

const constants = {
	en: {
		header_title: 'LdoD Archive',
	},
	pt: {
		header_title: 'Arquivo LdoD',
	},
	es: {
		header_title: 'Archivo LdoD',
	},
};

const sheet = new CSSStyleSheet();
sheet.replaceSync(transitionsCss);

let Collapse;
const loadBootstrapJSModules = async () => {
	Collapse = (await import('@shared/bootstrap/collapse.js')).default;
};

class NavBar extends HTMLElement {
	constructor() {
		super();
		ldodEventBus.register(`ldod:header`, headerSchema);
		ldodEventBus.subscribe(`ldod:header`, this.onHeader);
		this.headersToConsume = [];
		if (NavBar.instance) return NavBar.instance;
		NavBar.instance = this;
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

	get dropdownSize() {
		return this.shadowRoot.querySelectorAll("li[is='drop-down']").length + 1;
	}

	// Custom Elements hooks

	connectedCallback() {
		if (!this.shadowRoot) {
			this.attachShadow({ mode: 'open' });
			import('./nav-bar-html').then(mod => {
				this.shadowRoot.innerHTML = mod.default(this.language);
				this.consumeHeaders();
			});
		}
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.addEventBusListeners();
		window.addEventListener('pointermove', this.render, { once: true });
		this.setAdminVisibility();
	}

	disconnectedCallback() {
		this.unsubLogout?.();
		this.unsubLogin?.();
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name](oldValue, newValue);
	}

	// EventBus Communication

	addEventBusListeners() {
		this.unsubLogout = ldodEventSubscriber('logout', this.onUserLogout);
		this.unsubLogin = ldodEventSubscriber('login', this.onUserLogin);
	}

	onUserLogin = ({ payload }) => {
		this.user = payload;
		this.setAdminVisibility();
	};

	onUserLogout = () => {
		this.user && this.setAdminVisibility(true);
		this.user = undefined;
	};

	// Custom element on render

	render = async () => {
		await this.createInstances();
		this.addEventListeners();
		this.setDropDownMaxWidth();
	};

	async createInstances() {
		await loadBootstrapJSModules().then(() => {
			this.collapses = Array.from(this.shadowRoot.querySelectorAll('.collapse')).map(
				collapse => new Collapse(collapse, { toggle: false })
			);
		});
	}

	addEventListeners() {
		this.collapseToggler?.addEventListener('click', this.onToggler);

		document.addEventListener('click', e => {
			if (!this.contains(e.target)) this.toggleDropdownMenu(e);
		});

		this.shadowRoot.addEventListener('click', this.toggleDropdownMenu);

		this.shadowRoot.addEventListener('click', e => {
			if (e.target.classList.contains('dropdown-item'))
				this.shadowRoot.querySelector('#navbar-nav').classList.remove('show');
		});
	}

	setDropDownMaxWidth() {
		sheet.insertRule(`ul.navbar-nav>li{max-width: ${100 / this.dropdownSize}%}`);
	}

	onToggler = () =>
		this.collapses.forEach(element => {
			element.toggle();
		});

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

	toggleUserComponentMenu = (userC, force = undefined) => {
		if (force === undefined)
			return userC
				.querySelectorAll('a.dropdown-toggle, ul.dropdown-menu')
				.forEach(ele => ele.classList.toggle('show'));

		userC
			.querySelectorAll('a.dropdown-toggle, ul.dropdown-menu')
			.forEach(ele => ele.classList.toggle('show', force));
	};

	handleChangedAttribute = {
		language: (oldV, newV) => oldV !== newV && this.onLanguageChange(),
	};

	onLanguageChange() {
		this.shadowRoot.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', this.language);
		});
		this.shadowRoot.querySelectorAll('[data-navbar-key]').forEach(ele => {
			ele.textContent = constants[this.language][ele.dataset.navbarKey];
		});
	}

	setAdminVisibility = (hide = !this.isAdmin) => {
		const adminDrop = this.adminDropdown;
		if (adminDrop) adminDrop.hidden = hide;
	};

	newHeader = payload => {
		const template = document.createElement('template');
		template.innerHTML = /*html*/ `
		<li
			is="drop-down"
			key="${payload.name}"
			language="${this.language}"
			data-headers='${JSON.stringify(payload)}'
		></li>`;
		return template.content.firstElementChild.cloneNode(true);
	};

	onHeader = ({ payload }) => {
		const drop = [...this.dropdowns].find(d => d.key === payload.name);
		if (drop) drop.onNewLink({ payload });
		else {
			const header = this.newHeader(payload);
			this.addHeader(header);
		}
	};

	addHeader(liDropdownNode) {
		if (!(liDropdownNode instanceof HTMLLIElement)) return;
		if (this.dropdownSize >= 10) return;
		const ref = [...this.dropdowns].find(
			d => d.key.localeCompare(liDropdownNode.getAttribute('key')) == 1
		);
		if (!this.adminDropdown) this.headersToConsume.push(liDropdownNode);
		else ref.insertAdjacentElement('beforebegin', liDropdownNode);
	}

	consumeHeaders() {
		while (this.headersToConsume.length) this.addHeader(this.headersToConsume.pop());
	}
}

!customElements.get('nav-bar') && customElements.define('nav-bar', NavBar);
