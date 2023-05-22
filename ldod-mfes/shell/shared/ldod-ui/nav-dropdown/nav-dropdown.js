/** @format */
import { addLiItem, createDropdownRawHTML } from './nav-dropdown-html';
import { headerDataSchemaValidator } from './data-schema-validator';

let DropdownBS;
const loadDropdownJSModule = async () => {
	if (DropdownBS) return;
	DropdownBS = (await import('@ui/bootstrap/dropdown.js')).default;
};

export class NavDropdown extends HTMLLIElement {
	constructor() {
		super();
	}

	get key() {
		return this.getAttribute('key');
	}

	get language() {
		return this.getAttribute('language');
	}

	get show() {
		return this.hasAttribute('show');
	}

	get constants() {
		return this.data.constants;
	}

	static get observedAttributes() {
		return ['show', 'language', 'data-headers'];
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name](oldValue, newValue);
	}
	handleChangedAttribute = {
		show: () => {
			this.toggler.classList.toggle('show', this.show);
			this.menu.classList.toggle('show', this.show);
		},
		language: (oldValue, newValue) => {
			if (!this.data || !oldValue || oldValue === newValue) return;
			this.querySelectorAll('[data-dropdown-key]').forEach(element => {
				element.textContent =
					this.constants[this.language][element.dataset.dropdownKey] ||
					element.dataset.dropdownKey;
			});
		},
		'data-headers': (_, headersData) => {
			if (headersData) this.data = JSON.parse(headersData);
			this.removeAttribute('data-headers');
			if (headerDataSchemaValidator(this.data)) this.render();
		},
	};

	connectedCallback() {
		this.render();
	}

	render = async () => {
		if (!this.data) return;
		this.hidden = this.data.hidden;
		if (!this.innerHTML) this.innerHTML = createDropdownRawHTML(this.data, this.language);
		this.toggler = this.querySelector('a.dropdown-toggle');
		this.menu = this.querySelector('ul.dropdown-menu');
		this.containerLinks = this.menu.querySelector('div.container-links');
		this.addEventListeners();
	};

	addEventListeners = () => {
		window.addEventListener('pointermove', this.loadDropdownModule, { once: true });
		this.toggler.addEventListener('click', e => {
			e.stopPropagation();
			this.dispatchEvent(new PointerEvent('click', { bubbles: true, composed: true }));
		});
	};

	onNewLink = ({ payload }) => {
		if (!payload) return;
		const { replace = false, name, data, constants } = payload;
		if (name !== this.key) return;
		this.setConstants(constants);

		const itemsHTML = /*html*/ `
			${data.links.reduce((html, page) => {
				return html + addLiItem(page, this.language, this.constants);
			}, '')}
		`;

		if (replace) this.containerLinks.innerHTML = '';
		this.containerLinks.insertAdjacentHTML('beforeend', itemsHTML);
	};

	loadDropdownModule = async () => {
		await loadDropdownJSModule();
		this.dropdown = new DropdownBS(this.querySelector('.dropdown-toggle'));
	};

	setConstants(constants = {}) {
		Object.entries(constants).reduce((prev, [key, val]) => {
			prev[key] = { ...prev[key], ...val };
			return prev;
		}, this.constants);
	}
}
customElements.define('nav-dropdown', NavDropdown, { extends: 'li' });
