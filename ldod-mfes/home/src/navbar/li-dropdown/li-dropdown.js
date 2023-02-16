/** @format */
import headersMenus from '../headers-menus';
import liDropdownHtml from './li-dropdown-html';

let dropdown;
const loadDropdownJSModule = async () => {
	dropdown = (await import('@shared/bootstrap/dropdown.js')).default;
};

export class Dropdown extends HTMLLIElement {
	constructor() {
		super();
		this.data = headersMenus[this.key];
		this.innerHTML = liDropdownHtml(this);
		this.toggler = this.querySelector('a.dropdown-toggle');
		this.menu = this.querySelector('ul.dropdown-menu');
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

	static get observedAttributes() {
		return ['show'];
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.handleChangedAttribute[name]();
	}
	handleChangedAttribute = {
		show: () => {
			this.toggler.classList.toggle('show', this.show);
			this.menu.classList.toggle('show', this.show);
		},
	};

	connectedCallback() {
		window.addEventListener('pointermove', this.render, { once: true });
		this.toggler.addEventListener('click', e => {
			e.stopPropagation();
			this.dispatchEvent(new PointerEvent('click', { bubbles: true, composed: true }));
		});
	}

	render = async () => {
		await loadDropdownJSModule();
		this.dropdown = new dropdown(this.querySelector('.dropdown-toggle'));
	};
}
customElements.define('drop-down', Dropdown, { extends: 'li' });
