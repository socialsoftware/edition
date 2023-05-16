/** @format */

import { LitElement, html } from '@lit-bundle';
import { customElement } from 'lit/decorators.js';
import sheet from './style-css';
import { myState } from './my-state';
import { observeState } from '@vendor/lit-element-state_1.7.0/lit-state.js';
@customElement('new-mfe-lit-ts')
export class NewMfeLitTs extends observeState(LitElement) {
	static override styles = sheet;

	override render() {
		return html`
			<h2>Hey there Nr. ${myState.counter}!</h2>
			<button @click=${() => myState.counter++}>increment</button>
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
