/** @format */

let Collapse, Dropdown;

import hostStyle from './style/host-style.css?inline';
import rootBS from '@shared/bootstrap/root-css.js';
import navbarBS from '@shared/bootstrap/navbar-css.js';
import navBS from '@shared/bootstrap/nav-css.js';
import dropdownBS from '@shared/bootstrap/dropdown-css.js';

import container from './style/container.css?inline';
import navbar from './style/navbar.css?inline';
import dropdown from './style/dropdown.css?inline';

import './li-lang-menu';
import navbarHtml from './navbar-html';

const sheet = new CSSStyleSheet();
sheet.replaceSync(
	rootBS + navbarBS + navBS + dropdownBS + hostStyle + container + navbar + dropdown
);

const loadBootstrapJSModules = async () => {
	Collapse = (await import('@shared/bootstrap/collapse.js')).default;
	Dropdown = (await import('@shared/bootstrap/dropdown.js')).default;
};

export class LdodNavbar extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.shadowRoot.innerHTML = `${navbarHtml(this.language)}`;
	}
	static get observedAttributes() {
		return ['language'];
	}
	get language() {
		return this.getAttribute('language') || 'en';
	}

	get collapseToggler() {
		return this.shadowRoot.querySelector('button[data-bs-toggle="collapse"]');
	}

	get dropItems() {
		return Array.from(this.shadowRoot.querySelectorAll('.dropdown-menu a.dropdown-item'));
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name](oldValue, newValue);
	}

	handleChangedAttribute = {
		language: (oldV, newV) => oldV && oldV !== newV && this.onLanguageChange(),
	};

	onLanguageChange() {
		Array.from(this.shadowRoot.querySelectorAll('[language]')).forEach(ele =>
			ele.setAttribute('language', this.language)
		);
	}

	connectedCallback() {
		window.addEventListener('pointermove', this.render, { once: true });
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
		this.dropdowns = Array.from(this.shadowRoot.querySelectorAll('.dropdown-toggle')).map(
			drop => new Dropdown(drop)
		);
	};
	addEventListeners() {
		this.collapseToggler.addEventListener('click', this.onToggler);
		this.handleDropdownClick();
		this.handleDropdownBlur();
		this.dropItems.forEach(item => item.addEventListener('pointerdown', this.onDropdownItem));
	}
	onToggler = () => this.collapses.forEach(element => element.toggle());

	handleDropdownClick = () => {
		this.dropdowns.forEach(drop =>
			drop._element.addEventListener('pointerdown', e => e.button === 0 && drop.toggle())
		);
	};

	handleDropdownBlur = () => {
		this.dropdowns.forEach(drop => {
			drop._element.addEventListener('focusout', () => {
				console.log(drop);
				drop.hide();
			});
		});
	};

	onDropdownItem = e => e.button === 2 && e.preventDefault();
}
customElements.define('ldod-navbar', LdodNavbar);
