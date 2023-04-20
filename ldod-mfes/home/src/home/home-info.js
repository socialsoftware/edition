/** @format */

import constants from './constants/constants';

export class HomeInfo extends HTMLElement {
	constructor() {
		super();
		if (!this.shadowRoot) {
			this.attachShadow({ mode: 'open' });
			this.render();
		}
	}

	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	set language(lang) {
		this.setAttribute('language', lang);
	}

	connectedCallback() {
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		if (oldV && oldV !== newV) {
			this.shadowRoot
				.querySelectorAll('[data-info-key]')
				.forEach(ele => (ele.innerHTML = constants[this.language][ele.dataset.infoKey]));
		}
	}

	addEventListeners() {
		this.shadowRoot.querySelectorAll('img').forEach(img => {
			img.src = getURL(img.id);
		});
	}

	async render() {
		this.shadowRoot.innerHTML = (await import('./home-info-html')).default(this.language);
		this.addEventListeners();
	}
}

function getURL(path) {
	const url = `${import.meta.env.VITE_BASE}resources/webp/${path}.webp`;
	return new URL(url, import.meta.url).href;
}

customElements.define('home-info', HomeInfo);
