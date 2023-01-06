import signinHtml from './signup-html';

export default class LdodSignup extends HTMLElement {
	constructor() {
		super();
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
