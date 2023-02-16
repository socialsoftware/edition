/** @format */
import style from './style/home.css?inline';
import containerCss from '../navbar/style/container.css?inline';
const styleSheet = new CSSStyleSheet();
styleSheet.replaceSync(style + containerCss);
import constants from '../navbar/constants';
import ldodHomeHtml from './ldod-home-html';

export default class LdodHome extends HTMLElement {
	constructor() {
		super();
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.adoptedStyleSheets = [styleSheet];
	}
	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	attributeChangedCallback(name, oldV, newV) {
		this.attributeChanged[name]();
	}

	attributeChanged = {
		language: (oldV, newV) => {
			if (oldV !== newV) {
				this.componentsTextContextUpdate();
				this.boxesUpdate();
				this.languageUpdate();
			}
		},
	};

	connectedCallback() {
		this.render();
	}

	render() {
		this.shadowRoot.innerHTML = ldodHomeHtml(this);
	}

	languageUpdate = () =>
		this.shadowRoot
			.querySelectorAll('[language]')
			.forEach(ele => ele.setAttribute('language', this.language));

	componentsTextContextUpdate() {
		this.shadowRoot
			.querySelectorAll('[data-home-key]')
			.forEach(ele => (ele.textContent = constants[this.language][ele.dataset.homeKey]));
	}
}

customElements.define('ldod-home', LdodHome);
