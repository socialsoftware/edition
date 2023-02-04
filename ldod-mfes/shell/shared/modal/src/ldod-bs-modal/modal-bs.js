/** @format */

import modalCss from '@shared/bootstrap/modal-css.js';
import buttonsCss from '@shared/bootstrap/buttons-css.js';
import root from '@shared/bootstrap/root-css.js';

import style from './style.css?inline';
import { modal } from './modal-html';

const sheet = new CSSStyleSheet();
sheet.replaceSync(root + modalCss + buttonsCss + style);

export class LdodBsModal extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.shadowRoot.innerHTML = modal();
		this.modalBackdrop = this.shadowRoot.querySelector('#modal-backdrop');
	}

	get modal() {
		return this.shadowRoot.firstElementChild;
	}

	get dialog() {
		return this.modal.firstElementChild;
	}

	get static() {
		return this.hasAttribute('static');
	}
	get closeBtn() {
		return this.modal.querySelector('#modal-btn-close');
	}

	get dialogClass() {
		return this.getAttribute('dialog-class');
	}

	get show() {
		return this.hasAttribute('show');
	}

	static get observedAttributes() {
		return ['show', 'dialog-class'];
	}

	attributeChangedCallback(name) {
		this.handleAttributeChange[name]();
	}

	handleAttributeChange = {
		'dialog-class': () => this.setDialogClass(),
		show: () => (this.show ? this.showModal() : this.hideModal()),
	};

	connectedCallback() {
		this.dialog.addEventListener('click', e => e.stopPropagation());
		this.shadowRoot.querySelectorAll('slot').forEach(slot => {
			if (!slot.assignedNodes().length) slot.parentElement.hidden = true;
		});
	}

	addEventListeners() {
		this.closeBtn.addEventListener('click', this.toggle);
		this.addEventListener('click', this.onClickOutsiteContent);
		document.addEventListener('keydown', this.onEscape);
	}

	removeEventListeners() {
		this.closeBtn.addEventListener('click', this.toggle);
		this.removeEventListener('click', this.toggle);
		document.removeEventListener('keydown', this.onEscape);
	}
	onEscape = e => e.key === 'Escape' && this.onClickOutsiteContent();
	onClickOutsiteContent = () => {
		if (!this.static) return this.toggle();
		if (this.show) {
			this.modal.classList.toggle('modal-static');
			setTimeout(() => this.modal.classList.toggle('modal-static'), 500);
		}
	};

	toggle = () => this.toggleAttribute('show');

	showModal = () => {
		this.addEventListeners();
		this.modal.style.display = 'block';
		setTimeout(() => {
			this.modalBackdrop.className = 'modal-backdrop fade show';
			this.removeAttribute('aria-hidden');
			this.ariaModal = 'true';
			document.body.classList.add('modal-open');
			this.modal.classList.add('show');
		}, 10);
	};

	hideModal = () => {
		this.removeEventListeners();
		this.modalBackdrop.className = '';
		this.ariaHidden = 'true';
		this.removeAttribute('aria-modal');
		document.body.classList.remove('modal-open');
		this.modal.classList.remove('show');
		setTimeout(() => {
			this.modal.style.display = 'none';
		}, 300);
	};

	onHide = () =>
		this.dispatchEvent(
			new CustomEvent('ldod-modal-close', {
				detail: { id: this.id },
				bubbles: true,
				composed: true,
			})
		);

	setDialogClass() {
		this.dialog.classList.add(...this.dialogClass.split(' '));
	}
}

!customElements.get('ldod-bs-modal') && customElements.define('ldod-bs-modal', LdodBsModal);
