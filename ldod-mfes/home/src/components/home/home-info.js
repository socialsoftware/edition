import { parseHTML } from '@shared/utils.js';
import style from '../../../style/info.css';

const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

const loadConstants = async lang => (await import(`../../../resources/home/constants/constants-${lang}.js`)).default;

export class HomeInfo extends HTMLElement {
	constructor() {
		super();
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.adoptedStyleSheets = [sheet];
	}

	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	set language(lang) {
		this.setAttribute('language', lang);
	}

	connectedCallback() {
		this.setConstants().then(() => this.render());
	}

	attributeChangedCallback(name, oldV, newV) {
		if (oldV && oldV !== newV) {
			this.setConstants().then(() => {
				this.shadowRoot
					.querySelectorAll('.update-language')
					.forEach(ele => (ele.innerHTML = this.constants[ele.id]));
			});
		}
	}

	render() {
		const info = parseHTML(/*html*/ `
      <div class="container">
        <div id="info" class="bottom-info font-monospace update-language">
          ${this.constants.info}
        </div>
      </div>
    `);
		const bottomBar = parseHTML(html`<div class="bottom-bar"></div>`);
		this.shadowRoot.append(info, bottomBar);
	}

	async setConstants() {
		this.constants = await loadConstants(this.language);
	}
}

customElements.define('home-info', HomeInfo);
