/** @format */

import buttonsCss from '@ui/bootstrap/buttons-css.js';
import formsCss from '@ui/bootstrap/forms-css.js';
import rootCss from '@ui/bootstrap/root-css.js';
import style from './style.css?inline';

import UploadComponent from './ldod-upload.js';
import { uploadEvent } from '../events-module';
import { requestProxy } from '@core';

export class LdodUpload extends HTMLElement {
	constructor() {
		super();
		this.controller = new AbortController();
		this.attachShadow({ mode: 'open' });
		this.sheet = new CSSStyleSheet();
		this.sheet.replaceSync(rootCss + buttonsCss + formsCss + style);
		this.shadowRoot.adoptedStyleSheets = [this.sheet];
	}

	static get observedAttributes() {
		return ['title', 'width'];
	}

	get title() {
		return this.getAttribute('title');
	}

	get isMultiple() {
		return this.hasAttribute('multiple');
	}
	get width() {
		return this.getAttribute('width') ?? '';
	}

	get form() {
		return this.shadowRoot.querySelector('form');
	}

	connectedCallback() {
		this.render();
	}

	disconnectedCallback() {
		this.controller.abort();
	}

	render() {
		this.shadowRoot.innerHTML = UploadComponent({
			title: this.title,
			isMultiple: this.isMultiple,
		});
		this.addEventListeners();
	}

	addEventListeners = () => {
		this.form.onsubmit = this.handleSubmit;
		this.form.oninput = this.handleInput;
	};

	handleSubmit = async e => {
		e.preventDefault();
		const data = new FormData(e.target);
		const path = this.dataset.url;
		const signal = this.controller.signal;
		const res = await requestProxy.upload({ path, signal }, data).catch(e => console.error(e));
		this.responseData = res;
		this.dispatchEvent(uploadEvent(this.id, res));
	};

	handleInput = e => {
		const toggleDisabled = value =>
			this.shadowRoot.querySelector('#loadBtn').toggleAttribute('disabled', value);
		if (e.target.value.endsWith('.xml') || e.target.value.endsWith('.XML')) {
			return toggleDisabled(false);
		}
		toggleDisabled(true);
	};

	attributeChangedCallback(name, oldV, newV) {
		this.handleChangeAttribute[name](oldV, newV);
	}

	handleChangeAttribute = {
		title: (oldV, newV) => {
			if (oldV && oldV !== newV)
				this.shadowRoot.querySelector('button#loadBtn>span[label]').textContent =
					this.title;
		},
		width: () => {
			this.sheet.insertRule(`form > div {width: ${this.width};}`);
			this.shadowRoot.adoptedStyleSheets = [this.sheet];
		},
	};
}
!customElements.get('ldod-upload') && customElements.define('ldod-upload', LdodUpload);
