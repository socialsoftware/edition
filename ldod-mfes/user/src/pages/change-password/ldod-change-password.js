/** @format */

import changePasswordHtml from './change-password-html';
import constants from '../constants';
import formsCss from '@ui/bootstrap/forms-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import hostCss from '../host.css?inline';

import '@core-ui';

import { errorPublisher } from '../../events-modules';
import { changePasswordRequest } from '../../api-requests';
import { onChangePassword, onChangePasswordFail, redirectToHome } from '../common-functions';
import { getState } from '../../store';

const sheet = new CSSStyleSheet();
sheet.replaceSync(formsCss + buttonsCss + hostCss);

export default class LdodChangePassword extends HTMLElement {
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
		if (!getState().user) return redirectToHome();
		this.render();
	}

	render() {
		this.shadowRoot.appendChild(changePasswordHtml(this.language, this));
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
		this.shadowRoot
			.querySelectorAll('[language]')
			.forEach(element => element.setAttribute('language', this.language));
	};

	passwordsValidation(formData) {
		if (!formData.username) return false;
		if (formData.currentPassword === formData.newPassword) {
			errorPublisher(constants[this.language].currentNew);
			return false;
		}
		if (formData.newPassword !== formData.retypedPassword) {
			errorPublisher(constants[this.language].confirmPattern);
			return false;
		}
		return true;
	}

	onSubmit = async event => {
		event.preventDefault();
		event.stopPropagation();
		const form = event.target;
		form.classList.add('was-validated');
		if (form.checkValidity()) {
			const formData = Object.fromEntries(new FormData(form));
			if (!this.passwordsValidation(formData)) return;
			await changePasswordRequest(formData)
				.then(onChangePassword)
				.catch(onChangePasswordFail);
		}
	};
}

!customElements.get('ldod-change-password') &&
	customElements.define('ldod-change-password', LdodChangePassword);
