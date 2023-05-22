/** @format */

import './li-lang-drop';
import '@ui/nav-dropdown.js';
import transitionsCss from '@ui/bootstrap/transitions-css.js';
import navbarMenuSchema from './json-schemas/navbar-menu-schema';
import { ldodEventBus } from './ldod-event-bus';
import './auth-menu/auth-menu';

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
	Collapse = (await import('@ui/bootstrap/collapse.js')).default;
};

class NavBar extends HTMLElement {
	constructor() {
		super();
		this.isRendered = this.shadowRoot?.innerHTML;
		ldodEventBus.register(`ldod:header`, navbarMenuSchema);
		ldodEventBus.subscribe(`ldod:header`, this.onMenuCallback);
		if (!this.isRendered) this.attachShadow({ mode: 'open' });
		this.headersToConsume = [];
		if (!NavBar.instance) NavBar.instance = this;
		return NavBar.instance;
	}

	static get observedAttributes() {
		return ['language'];
	}
	get language() {
		return this.getAttribute('language') || 'en';
	}

	get dropdowns() {
		return this.shadowRoot.querySelectorAll("li[is='nav-dropdown']");
	}

	get userComponents() {
		return this.shadowRoot.querySelectorAll("li[is='user-auth-menu']");
	}

	get collapseToggler() {
		return this.shadowRoot.querySelector('button[data-bs-toggle="collapse"]');
	}

	get dropItems() {
		return Array.from(this.shadowRoot.querySelectorAll('.dropdown-menu a.dropdown-item'));
	}

	get reference() {
		return this.shadowRoot.querySelector('div#reference');
	}

	get dropdownSize() {
		return this.shadowRoot.querySelectorAll("li[is='nav-dropdown']").length + 1;
	}

	// Custom Elements hooks

	async connectedCallback() {
		if (!this.isRendered) {
			this.shadowRoot.innerHTML = await getNavbarHTML(this.language);
			this.isRendered = true;
			this.consumeHeaders();
		}
		this.shadowRoot.adoptedStyleSheets = [sheet];
		window.addEventListener('pointermove', this.render, { once: true });
	}

	disconnectedCallback() {
		this.unsubLogout?.();
		this.unsubLogin?.();
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name](oldValue, newValue);
	}

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
			return userC.querySelectorAll('a.dropdown-toggle, ul.dropdown-menu').forEach(ele => {
				ele.classList.toggle('show');
			});

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

	onMenuCallback = ({ payload }) => {
		const drop = this.getNavbarMenuByKey(payload.name);
		if (drop) drop.onNewLink({ payload });
		else {
			const header = this.newMenu(payload);
			this.addMenu(header);
		}
	};

	newMenu = payload => {
		const template = document.createElement('template');
		template.innerHTML = /*html*/ `
		<li
			${payload.hidden ? 'hidden' : ''} 
			is="nav-dropdown"
			key="${payload.name}"
			language="${this.language}"
			data-headers='${JSON.stringify(payload)}'
		></li>`;
		return template.content.firstElementChild.cloneNode(true);
	};

	addMenu(liDropdownNode) {
		if (!this.checkNavBarRenderedAndValid(liDropdownNode)) return;
		const key = liDropdownNode.getAttribute('key');
		const where = key === 'admin' ? 'afterend' : 'beforebegin';
		(this.getNavbarMenuByKey(key) || this.reference).insertAdjacentElement(
			where,
			liDropdownNode
		);
	}

	consumeHeaders() {
		while (this.headersToConsume.length) this.addMenu(this.headersToConsume.pop());
	}

	checkNavBarRenderedAndValid(liDropdownNode) {
		if (!this.isRendered) this.headersToConsume.push(liDropdownNode);
		return this.isRendered && liDropdownNode instanceof HTMLLIElement && this.dropdownSize < 10;
	}

	getNavbarMenuByKey = key =>
		this.shadowRoot.querySelector(`li[is='nav-dropdown'][key='${key}']`);

	setMenuVis = (key, hidden) => {
		const menu = this.getNavbarMenuByKey(key);
		if (!menu) return;
		menu.hidden = hidden;
	};
}

!customElements.get('nav-bar') && customElements.define('nav-bar', NavBar);

async function getNavbarHTML(lang) {
	return (await import('./nav-bar-html')).default(lang);
}
