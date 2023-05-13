/** @format */

import { ldodEventBus } from './ldod-event-bus';

class LangDrop extends HTMLLIElement {
	constructor() {
		super();
	}

	get items() {
		return Array.from(this.children);
	}

	connectedCallback() {
		this.unsub = ldodEventBus.subscribe('ldod:language', true, ({ payload }) => {
			this.language = payload;
			this.setActive();
			this.addEventListeners();
		}).unsubscribe;
		this.unsub();
	}

	addEventListeners = () => {
		this.items.forEach(ele => ele.addEventListener('click', this.updateLanguage));
	};

	updateLanguage = e => {
		e.stopPropagation();
		this.language = e.target.id;
		this.setActive();
		ldodEventBus.publish('ldod:language', this.language);
	};

	setActive() {
		this.items.forEach(ele => {
			ele.classList.remove('active');
			ele.id === this.language && ele.classList.add('active');
		});
	}
}

customElements.define('lang-drop', LangDrop, { extends: 'li' });
