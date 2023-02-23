/** @format */

import { hideHomeInfo, showHomeInfo } from '@src/home-info';

const loadComponent = async lang => {
	const node = await import(`./components/conduct-${lang}.js`);
	return node.default;
};

const conductTitle = {
	en: 'Code of Conduct',
	es: 'Código de Conducta',
	pt: 'Código de Conduta',
};

export class LdodConduct extends HTMLElement {
	constructor() {
		super();
	}

	get language() {
		return this.getAttribute('language');
	}

	get title() {
		return this.hasAttribute('title');
	}

	static get observedAttributes() {
		return ['language'];
	}

	connectedCallback() {
		this.innerHTML = /*html*/ `<div id="about-wrapper" class="ldod-about"></div>`;
		this.wrapper = this.querySelector('div#about-wrapper');
		this.render().then(() => showHomeInfo());
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handlers[name](oldV, newV);
	}

	disconnectedCallback() {
		hideHomeInfo();
	}

	handlers = {
		language: (oldV, newV) => {
			if (!oldV || oldV === newV) return;
			this.render();
		},
	};

	getTitle() {
		if (!this.title) return '';
		return /*html*/ `
			<h1 class="text-center">${conductTitle[this.language]}</h1>
			<p>&nbsp;</p>
		`;
	}

	async render() {
		this.wrapper.innerHTML = /*html*/ `
		<div>
			${this.getTitle()} ${await loadComponent(this.language)}
		</div>
		`;
	}
}
!customElements.get('ldod-conduct') && customElements.define('ldod-conduct', LdodConduct);
