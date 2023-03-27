/** @format */
import createVeFormHtml from './create-ve-form-html';
import formStyle from '@shared/bootstrap/forms-css.js';
import buttonsStyle from '@shared/bootstrap/buttons-css.js';
import style from './create-ve-form-style.css?inline';

const sheet = new CSSStyleSheet();
sheet.replaceSync(style + buttonsStyle + formStyle);

class CreateVeForm extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}

	connectedCallback() {
		this.shadowRoot.innerHTML = createVeFormHtml({ node: this.node });
		this.form = this.shadowRoot.querySelector('form#create-ve-form');
		this.addEventListeners();
	}

	addEventListeners() {
		this.form.onsubmit = this.node.onCreateVe;
	}
}
!customElements.get('create-ve-form') && customElements.define('create-ve-form', CreateVeForm);
