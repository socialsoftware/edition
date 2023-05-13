/** @format */

import { LitElement, html } from '@lit-bundle';
import { customElement, property } from 'lit/decorators.js';
import sheet from './style-css';

@customElement('new-mfe-lit-ts')
export class NewMfeLitTs extends LitElement {
	static override styles = sheet;

	@property()
	title = 'Hey There';

	@property({ type: Number })
	counter = 0;

	private __increment() {
		this.counter++;
	}

	override render() {
		return html`
			<h2>${this.title} Nr. ${this.counter}!</h2>
			<button @click=${this.__increment}>increment</button>
		`;
	}
}
declare global {
	interface HTMLElementTagNameMap {
		'new-mfe-lit-ts': NewMfeLitTs;
	}
}
let instance: HTMLElement;
const mount = (lang: String, ref: String) => {
	instance = window.html`<new-mfe-lit-ts></new-mfe-lit-ts>`;
	document.querySelector(ref).appendChild(instance);
};
const unMount = () => {
	instance.remove?.();
};
const path = '/lit-ts';

export { mount, unMount, path };
