import signinHtml from './signin-html';
import constants from '../constants';
import formsStyle from '@shared/bootstrap/forms.js';
import buttonsStyle from '@shared/bootstrap/buttons.js';
import style from './style.css?inline';

import '@shared/ldod-icons.js';
import { signinRequest } from '../../api-requests';
import { onAuthFail, onAuthSuccess, resetForm } from '../common-functions';

const sheet = new CSSStyleSheet();
sheet.replaceSync(formsStyle + buttonsStyle + style);
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
				.then(res => 'accessToken' in res && onAuthSuccess(res.accessToken))
				.catch(error => onAuthFail(error?.message));
			resetForm(this.form);
		}
	};
}

!customElements.get('ldod-signin') && customElements.define('ldod-signin', LdodSignin);
