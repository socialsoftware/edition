/** @format */

import ButtonComponent from './button-html';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import rootCss from '@ui/bootstrap/root-css.js';
export class LdodButton extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.sheet = new CSSStyleSheet();
		this.sheet.replaceSync(buttonsCss + rootCss);
		this.shadowRoot.adoptedStyleSheets = [this.sheet];
	}

	get class() {
		return this.getAttribute('class') ?? '';
	}

	get type() {
		return this.getAttribute('type') ?? '';
	}

	get btnId() {
		return this.dataset.btnid;
	}

	get title() {
		return this.getAttribute('title');
	}

	get button() {
		let id = this.btnId ? `#${this.btnId}` : '';
		return this.shadowRoot.querySelector(`button${id}`);
	}

	static get observedAttributes() {
		return ['title'];
	}

	connectedCallback() {
		this.shadowRoot.innerHTML = ButtonComponent(this.btnId, this.class, this.type, this.title);
		this.addEventListeners();
	}

	addEventListeners() {
		typeof this.handlers === 'object' &&
			Object.entries(this.handlers).forEach(([event, listener]) => {
				this.button.addEventListener(event, listener);
			});
	}

	attributeChangedCallback(name, oldV, newV) {
		this.changeAttribute[name](oldV, newV);
	}

	changeAttribute = {
		title: (oldV, newV) => {
			if (oldV && oldV !== newV) this.button.textContent = newV;
		},
	};

	disconnectedCallback() {}
}

!customElements.get('ldod-button') && customElements.define('ldod-button', LdodButton);
