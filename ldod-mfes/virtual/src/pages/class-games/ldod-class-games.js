/** @format */

import style from './style.css?inline';
import rootCss from '@ui/bootstrap/reboot-css.js';
const sheet = new CSSStyleSheet();
sheet.replaceSync(style + rootCss);

customElements.define(
	'ldod-class-games',
	class extends HTMLElement {
		constructor() {
			super();
			this.attachShadow({ mode: 'open' });
			this.shadowRoot.adoptedStyleSheets = [sheet];
		}

		get language() {
			return this.getAttribute('language');
		}
		static get observedAttributes() {
			return ['language'];
		}
		connectedCallback() {
			this.render();
		}

		async render() {
			this.shadowRoot.innerHTML = (await import(`./class-games-${this.language}.js`)).default;
		}

		attributeChangedCallback(name, oldV, newV) {
			this.handleChangedAttribute[name](oldV, newV);
		}

		handleChangedAttribute = {
			language: (oldV, newV) => oldV && oldV !== newV && this.render(),
		};
	}
);
