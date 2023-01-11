import signinHtml from './signup-html';
import constants from '../constants';
import formsStyle from '@shared/bootstrap/forms.js';
import buttonsStyle from '@shared/bootstrap/buttons.js';
const sheet = new CSSStyleSheet();
sheet.replaceSync(formsStyle + buttonsStyle);
export default class LdodSignup extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}
	static get observedAttributes() {
		return ['language'];
	}
	connectedCallback() {
		this.render();
	}

	render() {
		this.innerHTML = signinHtml;
	}

	attributeChangedCallback(name, oldValue, newValue) {}
}

!customElements.get('ldod-signup') && customElements.define('ldod-signup', LdodSignup);
