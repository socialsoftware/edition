/** @format */

import dialogHtml from './dialog-html';

class LdodDialog extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.innerHTML = dialogHtml;
		this.setSelectors();
		this.addListeners();
	}

	setSelectors() {
		this.instance = this.self.querySelector('dialog');
		this.openDialogBtn = this.self.querySelector('#dialog-open');
		this.openModalBtn = this.self.querySelector('#modal-open');
		this.closeBtn = this.self.querySelector('#close');
	}

	get self() {
		return this.shadowRoot;
	}

	get dialog() {
		return this.self.querySelector('dialog');
	}

	addListeners() {
		this.openModalBtn.onclick = this.openModal;
		this.openDialogBtn.onclick = this.openDialog;
		this.closeBtn.onclick = this.close;
	}
	openModal = () => {
		this.instance.showModal();
	};
	openDialog = () => {
		this.instance.show();
	};
	close = () => {
		this.instance.close();
	};
}

customElements.define('ldod-dialog', LdodDialog);
