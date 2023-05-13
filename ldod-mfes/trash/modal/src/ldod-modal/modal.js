/** @format */

import modalStyle from './modal.css?inline';
import modalHtml from './modal-html';
const sheet = new CSSStyleSheet();
sheet.replaceSync(modalStyle);

export class LdodModal extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.modalHtml = modalHtml;
	}

	get show() {
		return this.hasAttribute('show');
	}

	get enableDocOverflow() {
		return this.hasAttribute('document-overflow');
	}

	get noFooter() {
		return this.hasAttribute('no-footer');
	}

	get dialogClass() {
		return this.getAttribute('dialog-class');
	}

	get zIndex() {
		return this.getAttribute('z-index');
	}

	static get observedAttributes() {
		return ['show'];
	}
	connectedCallback() {
		this.render();
	}

	render() {
		this.shadowRoot.innerHTML = this.modalHtml(this.dialogClass, this.noFooter);
		this.addEventListeners();
		this.handleToggleShow();
	}

	addEventListeners() {
		this.shadowRoot.querySelector('div#ldod-modal').style.zIndex = this.zIndex;
		this.shadowRoot.querySelector('button.btn-close').onclick = this.handleCloseModal;
	}

	attributeChangedCallback(name, prev, curr) {
		if (name === 'show' && prev !== 'null') this.handleToggleShow();
	}

	setBodyOverflow = () => document.body.classList.toggle('modal-open', this.show);

	handleToggleShow = () => {
		const modal = this.shadowRoot.querySelector('div#ldod-modal');
		if (!modal) return;
		this.setBodyOverflow();
		modal.setAttribute('aria-hidden', !this.show);
		!this.show && this.onClose();
	};

	handleCloseModal = () => {
		this.toggleAttribute('show', false);
	};

	onClose = () => {
		// !this.enableDocOverflow && this.setPageOverflow(false);
		this.dispatchEvent(
			new CustomEvent('ldod-modal-close', {
				detail: { id: this.id },
				bubbles: true,
				composed: true,
			})
		);
	};
}
!customElements.get('ldod-modal') && customElements.define('ldod-modal', LdodModal);
