/** @format */

import MessageComponent from './MessageComponent';

const hello = {
	pt: 'Olá Mundo!',
	en: 'Hello World!',
};

export default lang => {
	const temp = document.createElement('template');
	temp.innerHTML = /*html*/ `<div is="hello-world" language="${lang}"></div>`;
	return temp.content.firstElementChild.cloneNode(true);
};

customElements.define(
	'hello-world',
	class extends HTMLDivElement {
		static get observedAttributes() {
			return ['language'];
		}
		constructor() {
			super();
			this.render();
		}

		get lang() {
			return this.getAttribute('language');
		}

		render = () =>
			this.appendChild(
				MessageComponent({ header: hello[this.lang], message: 'Some message' })
			);
		attributeChangedCallback(name, oldV, newV) {
			this[name]?.(oldV, newV);
		}
		language = (oldV, newV) => oldV && oldV !== newV && this.render();
	},
	{ extends: 'div' }
);
