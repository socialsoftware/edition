/** @format */

import { ldodEventPublisher } from '@shared/ldod-events.js';
class LangMenu extends HTMLLIElement {
	constructor() {
		super();
	}

	get language() {
		return this.getAttribute('language');
	}

	get items() {
		return Array.from(this.children);
	}

	connectedCallback() {
		this.setActive();
		this.addEventListeners();
	}

	addEventListeners = () => {
		this.items.forEach(ele => ele.addEventListener('click', this.updateLanguage));
	};

	updateLanguage = e => {
		e.stopPropagation();
		this.setAttribute('language', e.target.id);
		this.setActive();
		ldodEventPublisher('language', e.target.id);
	};

	setActive() {
		this.items.forEach(ele => {
			ele.classList.remove('active');
			ele.id === this.language && ele.classList.add('active');
		});
	}
}

customElements.define('lang-menu', LangMenu, { extends: 'li' });
