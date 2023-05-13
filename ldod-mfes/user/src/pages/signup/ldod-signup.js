/** @format */

import signupHtml from './signup-html';
import constants from '../constants';
import formsCss from '@ui/bootstrap/forms-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import hostCss from '../host.css?inline';
import style from './style.css?inline';

import '@core-ui';
import { signupRequest } from '../../api-requests';
import { errorPublisher } from '../../events-modules';
import { onSignup, resetForm } from '../common-functions';

const sheet = new CSSStyleSheet();
sheet.replaceSync(formsCss + buttonsCss + hostCss + style);

export default class LdodSignup extends HTMLElement {
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

	get conductCodeModal() {
		return this.shadowRoot.querySelector('ldod-modal#modal-conduct-code');
	}

	connectedCallback() {
		this.render();
		if (Object.keys(history.state).length) this.loadState();
	}

	loadState() {
		this.shadowRoot.querySelectorAll('input').forEach(input => {
			if (input.name in history.state) input.value = history.state[input.name];
		});
	}

	render() {
		this.shadowRoot.appendChild(signupHtml(this.language, this));
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

	onSubmit = async event => {
		event.preventDefault();
		event.stopPropagation();
		const form = event.target;
		form.classList.add('was-validated');
		if (form.checkValidity()) {
			await signupRequest(Object.fromEntries(new FormData(form)))
				.then(res => {
					if (res.ok) {
						onSignup(res.message);
						resetForm(form);
					} else errorPublisher(res.message);
				})
				.catch(error => errorPublisher(error?.message));
		}
	};
}

!customElements.get('ldod-signup') && customElements.define('ldod-signup', LdodSignup);
