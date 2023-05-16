/** @format */
import { LitElement, html } from '@lit-bundle';
import { observeState } from '@vendor/lit-element-state_1.7.0/lit-state.js';
import { myState } from './my-state';
import sheet from './style-css';

export class NewMfeLit extends observeState(LitElement) {
	static styles = sheet;
	render() {
		return html`
			<h2>Hey there Nr. ${myState.counter}!</h2>
			<button @click=${() => myState.counter++}>increment</button>
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
