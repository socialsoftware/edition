/** @format */

import { ldodValidator, inputSchema } from './data-validation/data-schema-validator';
import navPanelHtml from './nav-panel-html';
import style from './style.css?inline';
import '@core-ui';
import '@ui/tooltip.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
const sheet = new CSSStyleSheet();
sheet.replaceSync(buttonsCss + style);

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
		this.addBtns = Array.from(this.shadowRoot.querySelectorAll('button#btn-add'));
		this.addEventListeners();
	}

	attributeChangedCallback(name, prev, curr) {
		this.changedAttribute[name]?.(prev, curr);
	}

	addEventListeners = () => {
		this.checkboxes.forEach(cb => cb.addEventListener('change', this.onCheckbox));
		this.addBtns.forEach(btn => btn.addEventListener('click', this.onAdd));
	};

	removeEventListeners() {
		this.checkboxes.forEach(cb => cb.removeEventListener('change', this.onCheckbox));
	}

	onCheckbox = e => {
		this.dispatchEvent(
			getCustomEvent(`${this.id}:changed`, {
				id: e.target.id,
				name: e.target.name,
				selected: this.checkboxes
					.filter(cb => cb.checked)
					.map(({ name, id }) => ({ id, name })),
			})
		);
	};

	onAdd = e => this.dispatchEvent(getCustomEvent(`${this.id}:clicked`, { target: e.target }));

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

function getCustomEvent(evtName, detail) {
	return new CustomEvent(evtName, {
		detail,
		bubbles: true,
		composed: true,
	});
}
