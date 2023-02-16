/** @format */

let Collapse;

import hostStyle from './style/host-style.css?inline';
import rootBS from '@shared/bootstrap/root-css.js';
import navbarBS from '@shared/bootstrap/navbar-css.js';
import navBS from '@shared/bootstrap/nav-css.js';
import dropdownBS from '@shared/bootstrap/dropdown-css.js';
import './li-dropdown/li-dropdown';
import container from './style/container.css?inline';
import navbar from './style/navbar.css?inline';
import dropdown from './style/dropdown.css?inline';

import './li-lang-menu';
import navbarHtml from './navbar-html';
import constants from './constants';

(await import('user').catch(e => console.error(e)))?.default.bootstrap();

const sheet = new CSSStyleSheet();
sheet.replaceSync(
	rootBS + navbarBS + navBS + dropdownBS + hostStyle + container + navbar + dropdown
);

const loadBootstrapJSModules = async () => {
	Collapse = (await import('@shared/bootstrap/collapse.js')).default;
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
	};

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
