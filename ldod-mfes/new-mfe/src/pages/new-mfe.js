/** @format */
import sheet from './style-css.js';
import './abstract.js';
export class NewMfe extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = sheet;
		this.title = 'Hey there';
		this.counter = 5;
	}

	static observedAttributes = ['test'];

	#__increment = () => {
		this.counter += 1;
		this.span.innerHTML = this.counter;
	};

	connectedCallback() {
		this.render();
	}

	render() {
		this.shadowRoot.innerHTML = /*html*/ `
			<h2>${this.title} Nr. <span id="counter">${this.counter}
			</span>!</h2><button id="incrementor">increment</button>`;
		this.addEventListeners();
	}

	addEventListeners() {
		this.button = this.shadowRoot.querySelector('#incrementor');
		this.span = this.shadowRoot.querySelector('#counter');
		this.button.onclick = this.#__increment;
	}
}
customElements.define('new-mfe', NewMfe);
let instance;
const mount = (lang, ref) => {
	instance = html`<new-mfe></new-mfe>`;
	document.querySelector(ref).appendChild(instance);
};

const unMount = () => {
	instance?.remove?.();
};
const path = '/pure';

export { mount, unMount, path };
