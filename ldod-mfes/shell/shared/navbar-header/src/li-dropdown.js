/** @format */
import { addLiItem, createDropdownRawHTML } from './li-dropdown-html';
import { headerDataSchemaValidator } from './data-schema-validator';

let DropdownBS;
const loadDropdownJSModule = async () => {
	if (DropdownBS) return;
	DropdownBS = (await import('@shared/bootstrap/dropdown.js')).default;
};

export class DropDown extends HTMLLIElement {
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
		if (!this.innerHTML) this.innerHTML = createDropdownRawHTML(this.data, this.language);
		this.toggler = this.querySelector('a.dropdown-toggle');
		this.menu = this.querySelector('ul.dropdown-menu');
		this.externalLinks = this.menu.querySelector('div#external-links');
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
		Object.entries(constants || {}).reduce((prev, [key, val]) => {
			prev[key] = { ...prev[key], ...val };
			return prev;
		}, this.constants);
		const itemsHTML = /*html*/ `
			${data.pages.reduce((html, page) => {
				return html + addLiItem(page, this.language, this.constants);
			}, '')}
		`;
		if (replace) this.replaceExternalLinks(itemsHTML);
		else this.addMenuLinks(itemsHTML);
	};

	replaceExternalLinks = html => {
		this.externalLinks.innerHTML = html;
	};

	addMenuLinks = html => {
		const template = document.createElement('template');
		template.innerHTML = html;
		[...template.content.cloneNode(true).children].forEach(node => {
			this.externalLinks.insertAdjacentElement('beforebegin', node);
		});
	};

	loadDropdownModule = async () => {
		await loadDropdownJSModule();
		this.dropdown = new DropdownBS(this.querySelector('.dropdown-toggle'));
	};
}
customElements.define('drop-down', DropDown, { extends: 'li' });
