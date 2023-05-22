/** @format */

import modalCss from '@ui/bootstrap/modal-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';

import style from './style.css?inline';
import { modal } from './modal-html';

const sheet = new CSSStyleSheet();
sheet.replaceSync(modalCss + buttonsCss + style);

export class LdodBsModal extends HTMLElement {
	static modalsOpened = 0;
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.self.innerHTML = modal();
		this.modalBackdrop = this.self.querySelector('#modal-backdrop');
	}

	get self() {
		return this.shadowRoot || this;
	}

	get modal() {
		return this.self.firstElementChild;
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
		this.self.querySelectorAll('slot').forEach(slot => {
			if (!slot.assignedNodes().length) slot.parentElement.hidden = true;
		});
	}

	disconnectedCallback() {
		this.removeEventListeners();
	}

	addEventListeners() {
		this.closeBtn.addEventListener('click', this.toggle);
		this.addEventListener('click', this.onClickOutsiteContent);
		document.addEventListener('keydown', this.onEscape);
	}

	removeEventListeners() {
		this.closeBtn.removeEventListener('click', this.toggle);
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
		this.ariaModal = 'true';
		this.removeAttribute('aria-hidden');
		setTimeout(() => {
			document.body.classList.add('modal-open');
			++LdodBsModal.modalsOpened;
			this.modalBackdrop.className = 'modal-backdrop show';
			this.modal.classList.add('show');
		}, 100);
	};

	hideModal = () => {
		this.removeEventListeners();
		this.modalBackdrop.className = '';
		this.ariaHidden = 'true';
		this.removeAttribute('aria-modal');
		this.modal.classList.remove('show');
		this.onHide();
		setTimeout(() => {
			this.modal.style.display = 'none';
			if (LdodBsModal.modalsOpened === 1) {
				--LdodBsModal.modalsOpened;
				document.body.classList.remove('modal-open');
			}
		}, 100);
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
