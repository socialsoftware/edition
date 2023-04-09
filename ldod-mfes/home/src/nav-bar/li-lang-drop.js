/** @format */

import { ldodEventPublisher } from '@shared/ldod-events.js';
class LangDrop extends HTMLLIElement {
	constructor() {
		super();
	}

	get items() {
		return Array.from(this.children);
	}

	connectedCallback() {
		this.language = this.getAttribute('language');
		this.setActive();
		this.addEventListeners();
	}

	addEventListeners = () => {
		this.items.forEach(ele => ele.addEventListener('click', this.updateLanguage));
	};

	updateLanguage = e => {
		e.stopPropagation();
		this.language = e.target.id;
		this.setActive();
		ldodEventPublisher('language', this.language);
	};

	setActive() {
		this.items.forEach(ele => {
			ele.classList.remove('active');
			ele.id === this.language && ele.classList.add('active');
		});
	}
}

customElements.define('lang-drop', LangDrop, { extends: 'li' });
