/** @format */

import homeInfoHtml from './home-info-html';
import containerCss from '../navbar/style/container.css?inline';
import style from './style/info.css?inline';
import constants from './constants/constants';
const sheet = new CSSStyleSheet();
sheet.replaceSync(containerCss + style);

export class HomeInfo extends HTMLElement {
	constructor() {
		super();
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.adoptedStyleSheets = [sheet];
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
		this.render();
	}

	attributeChangedCallback(name, oldV, newV) {
		if (oldV && oldV !== newV) {
			this.shadowRoot
				.querySelectorAll('[data-info-key]')
				.forEach(ele => (ele.innerHTML = constants[this.language][ele.dataset.infoKey]));
		}
	}

	render() {
		this.shadowRoot.innerHTML = homeInfoHtml(this);
	}
}

customElements.define('home-info', HomeInfo);
