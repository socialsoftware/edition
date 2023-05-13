/** @format */

import './li-lang-drop';
import '@ui/nav-dropdown.js';
import transitionsCss from '@ui/bootstrap/transitions-css.js';
import headerSchema from './data-schema.json';
import { ldodEventBus } from './ldod-event-bus';

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
		ldodEventBus.register(`ldod:header`, headerSchema);
		ldodEventBus.subscribe(`ldod:header`, this.onHeader);
		if (!this.isRendered) this.attachShadow({ mode: 'open' });
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
		return this.shadowRoot.querySelectorAll("li[is='nav-dropdown']");
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

	onHeader = ({ payload }) => {
		const drop = this.getHeader(payload.name);
		if (drop) drop.onNewLink({ payload });
		else {
			const header = this.newHeader(payload);
			this.addHeader(header);
		}
	};

	newHeader = payload => {
		const template = document.createElement('template');
		template.innerHTML = /*html*/ `
		<li
			is="nav-dropdown"
			key="${payload.name}"
			language="${this.language}"
			data-headers='${JSON.stringify(payload)}'
		></li>`;
		return template.content.firstElementChild.cloneNode(true);
	};

	addHeader(liDropdownNode) {
		if (!this.checkNavBarRenderedAndValid(liDropdownNode)) return;
		const key = liDropdownNode.getAttribute('key');
		const where = key === 'admin' ? 'afterbegin' : 'beforebegin';
		(this.getHeader(key) || this.reference).insertAdjacentElement(where, liDropdownNode);
	}

	consumeHeaders() {
		while (this.headersToConsume.length) this.addHeader(this.headersToConsume.pop());
	}

	getHeader(name) {
		return [...this.dropdowns].find(d => d.key === name);
	}

	checkNavBarRenderedAndValid(liDropdownNode) {
		if (!this.isRendered) this.headersToConsume.push(liDropdownNode);
		return this.isRendered && liDropdownNode instanceof HTMLLIElement && this.dropdownSize < 10;
	}
}

!customElements.get('nav-bar') && customElements.define('nav-bar', NavBar);

async function getNavbarHTML(lang) {
	return (await import('./nav-bar-html')).default(lang);
}
