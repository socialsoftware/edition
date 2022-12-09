import { parseHTML } from 'shared/utils.js';
import style from '../../../style/info.css' assert { type: 'css' };

export class HomeInfo extends HTMLElement {
  constructor() {
    super();
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.adoptedStyleSheets = [style];
    this.constants = undefined;
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

  async connectedCallback() {
    await this.setConstants();
    this.render();
  }
  async attributeChangedCallback(name, oldV, newV) {
    if (oldV && oldV !== newV) {
      await this.setConstants();
      this.shadowRoot
        .querySelectorAll('.update-language')
        .forEach((ele) => (ele.innerHTML = this.constants[ele.id]));
    }
  }

  render() {
    const info = parseHTML(html`
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
    this.constants = await this.loadConstants(this.language);
  }

  loadConstants = async (lang) =>
    (await import(`../../../resources/home/constants/constants-${lang}.js`))
      .default;
}

customElements.define('home-info', HomeInfo);
