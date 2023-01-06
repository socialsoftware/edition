import signinHtml from './signin-html';
import constants from '../constants';
import '@shared/ldod-icons.js';

import('@shared/icons/eye.js').then(data => console.log(data));

export default class LdodSignin extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
	}

	get language() {
		return this.getAttribute('language');
	}

	get form() {
		return this.shadowRoot.querySelector('form');
	}

	static get observedAttributes() {
		return ['language'];
	}

	connectedCallback() {
		this.render();
	}

	render() {
		this.shadowRoot.innerHTML = signinHtml(this.language);
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.onChangedAttribute[name](oldValue, newValue);
	}

	onChangedAttribute = {
		language: (oldV, newV) => this.onLanguage(oldV, newV),
	};

	onLanguage = (oldValue, newValue) => {
		if (oldValue !== newValue)
			this.shadowRoot.querySelectorAll('[data-user-key]').forEach(ele => {
				ele.firstChild.textContent = constants[this.language][ele.dataset.userKey];
			});
	};

	addEventListeners() {
		this.form.addEventListener('submit', this.onSubmit);
	}

	onSubmit(event) {
		event.preventDefault();
		event.stopPropagation();
		this.classList.add('was-validated');
		if (this.checkValidity()) {
		}
	}
}

!customElements.get('ldod-signin') && customElements.define('ldod-signin', LdodSignin);
