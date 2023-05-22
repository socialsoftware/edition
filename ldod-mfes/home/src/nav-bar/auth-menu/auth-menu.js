/** @format */

import { ldodEventBus } from '../ldod-event-bus';
import authMenuHtml from './auth-menu-html';
import navbarAuthSchema from '../json-schemas/navbar-auth-schema';

class UserAuthMenu extends HTMLLIElement {
	constructor() {
		super();
		ldodEventBus.register('navbar:auth-status', navbarAuthSchema);
		ldodEventBus.subscribe('navbar:auth-status', this.render);
	}

	static observedAttributes = ['language'];

	get language() {
		return this.getAttribute('language');
	}

	attributeChangedCallback(name, oldV, newV) {
		if (name === 'language' && oldV && oldV !== newV) this.onLanguage();
	}

	onLanguage = () => {
		this.querySelectorAll('[data-key]').forEach(element => {
			element.textContent = this.constants?.[this.language]?.[element.dataset.key] ?? key;
		});
	};

	render = ({ payload }) => {
		this.constants = payload.data.constants;
		this.innerHTML = authMenuHtml(payload, this.language);
	};
}

customElements.define('user-auth-menu', UserAuthMenu, { extends: 'li' });
