/** @format */

import { navigateTo } from '@core';
import { getState, setState, store, userFullName } from '../store';
import { userReferences } from '../user-references';
import { loginSubscriber, logoutPublisher, logoutSubscriber } from '../events-modules';
import authenticatedComponent from './auth-component';
import nonAuthenticatedComponent from './non-auth-component';
import constants from './constants';

export class UserComponent extends HTMLLIElement {
	constructor(language) {
		super();
		if (language) this.language = language;
	}
	get language() {
		return this.getAttribute('language') ?? 'en';
	}

	set language(language) {
		this.setAttribute('language', language);
	}

	static get observedAttributes() {
		return ['language'];
	}

	connectedCallback() {
		this.addListeners();
		this.render();
	}

	disconnectedCallback() {
		this.loginUnsub();
		this.logoutUnsub();
		this.userUpdateUnsub();
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this.onChangedAttribute[name](oldValue, newValue);
	}

	onChangedAttribute = {
		language: (oldValue, newValue) => {
			if (oldValue && newValue && oldValue !== newValue) this.onLanguage();
		},
	};

	onLanguage() {
		this.querySelectorAll('[data-user-key]').forEach(ele => {
			ele.textContent = constants[this.language][ele.dataset.userKey];
		});
	}

	render() {
		this.innerHTML = getState().user
			? authenticatedComponent(userFullName(), this.language, this.logoutHandler)
			: nonAuthenticatedComponent(this.language);
		this.addLogoutClickEventListener();
	}

	updateComponent = () => {
		this.innerHTML = '';
		this.render();
	};

	addListeners() {
		this.loginUnsub = loginSubscriber(this.updateComponent);
		this.logoutUnsub = logoutSubscriber(this.onUserLogout);
		this.userUpdateUnsub = store.subscribe(this.updateComponent, 'user');
	}

	addLogoutClickEventListener = () => {
		this.querySelector('a#user-logout')?.addEventListener('click', this.logoutHandler);
	};

	onUserLogin = () => {
		this.updateComponent();
	};

	onUserLogout = () => {
		this.logoutHandler();
		this.updateComponent();
	};

	logoutHandler = () => {
		if (!(getState().user || getState().token)) return;
		setState({ token: '', user: '' });
		logoutPublisher('handler');
		navigateTo(userReferences.signin());
	};
}

!customElements.get('user-component') &&
	customElements.define('user-component', UserComponent, { extends: 'li' });
