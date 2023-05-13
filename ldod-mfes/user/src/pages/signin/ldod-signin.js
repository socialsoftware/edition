/** @format */

import signinHtml from './signin-html';
import constants from '../constants';
import formsCss from '@ui/bootstrap/forms-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import hostCss from '../host.css?inline';
import style from './style.css?inline';

import '@core-ui';
import { signinRequest } from '../../api-requests';
import { onAuthFail, onAuthSuccess, resetForm } from '../common-functions';

const sheet = new CSSStyleSheet();
sheet.replaceSync(formsCss + buttonsCss + hostCss + style);
export default class LdodSignin extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
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
		this.shadowRoot.appendChild(signinHtml(this.language, this));
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

	onSubmit = async event => {
		event.preventDefault();
		event.stopPropagation();
		this.form.classList.add('was-validated');
		if (this.form.checkValidity()) {
			await signinRequest(Object.fromEntries(new FormData(this.form)))
				.then(res => res && res.accessToken && onAuthSuccess(res.accessToken))
				.catch(onAuthFail);
			resetForm(this.form);
		}
	};
}

!customElements.get('ldod-signin') && customElements.define('ldod-signin', LdodSignin);
