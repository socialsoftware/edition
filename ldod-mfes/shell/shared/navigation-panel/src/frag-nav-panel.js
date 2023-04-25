/** @format */

import { ldodValidator, inputSchema } from './data-validation/data-schema-validator';
import navPanelHtml from './nav-panel-html';
import style from './style.css?inline';
import '@shared/ldod-icons.js';
import '@shared/tooltip.js';
const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

export default class FragNavPanel extends HTMLElement {
	constructor() {
		super();
		this.checkboxes = [];
		if (!this.id) this.id = crypto.randomUUID();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}

	static get observedAttributes() {
		return ['data-input'];
	}

	hasInput() {
		return this.hasAttribute('data-input');
	}

	connectedCallback() {
		this.render();
	}

	render() {
		if (!this.hasInput()) return;
		this.data = JSON.parse(this.dataset.input ?? '{}');
		if (!isDataValid(this.data)) return;
		this.removeEventListeners();
		this.removeAttribute('data-input');
		this.shadowRoot.innerHTML = navPanelHtml(this.data);
		this.checkboxes = Array.from(this.shadowRoot.querySelectorAll("input[type='checkbox']"));
		this.addEventListeners();
	}

	attributeChangedCallback(name, prev, curr) {
		this.changedAttribute[name]?.(prev, curr);
	}

	addEventListeners() {
		this.checkboxes.forEach(cb => cb.addEventListener('change', this.onCheckbox));
	}

	removeEventListeners() {
		this.checkboxes.forEach(cb => cb.removeEventListener('change', this.onCheckbox));
	}

	onCheckbox = () => {
		const selected = this.checkboxes.filter(cb => cb.checked).map(cb => cb.id);
		this.dispatchEvent(getSelectionEvent(this.id, selected));
	};

	changedAttribute = {
		'data-input': (_, curr) => curr && this.render(),
	};
}

!customElements.get('frag-nav-panel') && customElements.define('frag-nav-panel', FragNavPanel);

function isDataValid(data) {
	const { errors, valid } = ldodValidator.validate(data, inputSchema);
	errors.forEach(er => console.error(er));
	return valid;
}

function getSelectionEvent(id, selected) {
	return new CustomEvent(`${id}:changed`, {
		detail: selected,
		bubbles: true,
		composed: true,
	});
}
