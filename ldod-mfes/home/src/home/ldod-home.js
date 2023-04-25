/** @format */

import { getReferences } from '../external-deps.js';
import './home-info.js';
export default class LdodHome extends HTMLElement {
	constructor() {
		super();
		if (!this.shadowRoot) {
			this.attachShadow({ mode: 'open' });
			this.render();
		} else {
			this.addEventListeners();
		}
	}
	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	attributeChangedCallback(name, oldV, newV) {
		this.attributeChanged[name](oldV, newV);
	}

	attributeChanged = {
		language: (oldV, newV) => oldV && oldV !== newV && this.render(),
	};

	connectedCallback = () => {
		this.addEventListeners();
	};

	async render() {
		this.shadowRoot.innerHTML = /*html*/ `
		${(await import('./ldod-home-html.js')).default(this.language)} 
        <home-info language="${this.language}"></home-info>
		`;
		this.addEventListeners();
	}

	addEventListeners() {
		this.shadowRoot.querySelectorAll('img').forEach(img => {
			img.src = getUrl(img.id);
		});

		this.shadowRoot.querySelectorAll('a[is="nav-to"]').forEach(a => {
			const params = JSON.parse(a.dataset.mfeKeyParams);
			a.setAttribute('to', getReferences(a.dataset.mfe)?.[a.dataset.mfeKey]?.(...params));
		});
	}
}

!customElements.get('ldod-home') && customElements.define('ldod-home', LdodHome);

function getUrl(path) {
	const url = `${import.meta.env.VITE_BASE}resources/svg/${path}.svg`;
	return new URL(url, import.meta.url).href;
}
