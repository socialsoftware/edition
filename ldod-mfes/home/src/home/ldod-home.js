/** @format */
import style from './style/home.css?inline';
import containerCss from '../nav-bar/style/container.css?inline';
const styleSheet = new CSSStyleSheet();
styleSheet.replaceSync(style + containerCss);
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
		this.attributeChanged[name](oldV, newV);
	}

	attributeChanged = {
		language: (oldV, newV) => oldV !== newV && this.render(),
	};

	connectedCallback = () => this.render();

	render = () => (this.shadowRoot.innerHTML = ldodHomeHtml(this));
}

customElements.define('ldod-home', LdodHome);
