/** @format */
import style from './style.css?inline';
import toastCss from '@ui/bootstrap/toast-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import notificationHtml from './notification-html';

const sheet = new CSSStyleSheet();
sheet.replaceSync(toastCss + buttonsCss + style);

export class LdodNotification extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.toastHtml = notificationHtml();
	}

	get show() {
		return this.hasAttribute('show');
	}

	get theme() {
		return this.getAttribute('theme') || 'primary';
	}

	get toast() {
		return this.shadowRoot.querySelector('.toast');
	}

	static get observedAttributes() {
		return ['show'];
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handleAttributeChange[name](oldV, newV);
	}

	handleAttributeChange = {
		show: () => this.show && this.showNotification(),
	};

	connectedCallback() {
		this.addEventListener('pointerenter', this.clearTimeout);
		this.addEventListener('pointerleave', this.hideNotification);
	}

	render() {
		this.shadowRoot.innerHTML = this.toastHtml(this.theme);
		this.addEventListeners();
	}

	addEventListeners = () => {
		this.shadowRoot.querySelector('.btn-close').addEventListener('click', this.close);
	};

	toggle = () => this.toggleAttribute('show');

	clearTimeout = () => {
		window.clearTimeout(this.timeout);
		this.timeout = null;
	};

	showNotification = () => {
		this.clearTimeout();
		this.render();
		this.toast.classList.add('show');
		this.fadeTransition();
		this.toggleAttribute('show', false);
		this.hideNotification();
	};

	hideNotification = () => {
		this.timeout = setTimeout(() => {
			this.timeout && this.close();
		}, 3000);
	};

	fadeTransition = (close = false) => {
		this.toast.classList.add('showing');
		setTimeout(() => {
			this.toast.classList.remove('showing');
			close && this.toast.classList.remove('show');
			close && this.remove();
		}, 100);
	};

	close = () => {
		this.fadeTransition(true);
	};
}

!customElements.get('ldod-notification') &&
	customElements.define('ldod-notification', LdodNotification);
