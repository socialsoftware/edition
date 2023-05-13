/** @format */
const data = ['1', '2', '3'];
import { LitElement, html } from '@lit-bundle';
import sheet from './style-css.js';
function myhtml(templates, ...values) {
	console.log(templates);
	console.log(values);
	const t = html(templates, ...values);
	return t;
}
export class NewMfeLit extends LitElement {
	static styles = sheet;

	static properties = {
		title: { type: String },
		counter: { type: Number },
		data: { type: Array },
	};

	constructor() {
		super();
		this.title = 'Hey there';
		this.counter = 5;
		this.data = ['1', '2', '4'];
	}

	#__increment = () => {
		this.counter += 1;
		this.data = data;
	};

	render() {
		return myhtml`
			<h2>${this.title} Nr. ${this.counter}!</h2>
			<button @click=${this.#__increment}>increment</button>
			${this.data.map(entry => html`<div>${entry}</div>`)}
		`;
	}
}
window.customElements.define('new-mfe-lit', NewMfeLit);

let instance;
const mount = (lang, ref) => {
	instance = window.html`<new-mfe-lit></new-mfe-lit>`;
	document.querySelector(ref).appendChild(instance);
};
const unMount = () => {
	instance.remove?.();
};
const path = '/lit';

export { mount, unMount, path };
