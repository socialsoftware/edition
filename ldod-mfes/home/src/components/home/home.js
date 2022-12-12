window.html = String.raw;
import { parseHTML } from 'shared/utils.js';
import './home-info.js';
import HomeContent, { boxUrl, boxUrlH } from './home-content.js';
import style from '../../../style/home.css';
const styleSheet = new CSSStyleSheet();
styleSheet.replaceSync(style);

const loadConstants = async (lang) =>
  (await import(`../../../resources/home/constants/constants-${lang}.js`))
    .default;

export default class HomeMFE extends HTMLElement {
  constructor() {
    super();
    this.constants = undefined;
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.adoptedStyleSheets = [styleSheet];
  }
  static get observedAttributes() {
    return ['language'];
  }

  get language() {
    return this.getAttribute('language');
  }

  connectedCallback() {
    this.setConstants().then(() => this.render());
  }

  languageUpdate = () =>
    this.shadowRoot
      .querySelectorAll('.language')
      .forEach((ele) => (ele.language = this.language));

  attributeChangedCallback(name, oldValue, newValue) {
    if (name === 'language') {
      this.setConstants().then(() => {
        this.componentsTextContextUpdate();
        this.boxesUpdate();
        this.languageUpdate();
      });
    }
  }

  componentsTextContextUpdate() {
    this.shadowRoot
      .querySelectorAll('.update-language')
      .forEach((ele) => (ele.innerHTML = this.constants[ele.id]));
  }

  boxesUpdate() {
    this.shadowRoot
      .querySelectorAll('div.div-link>img.not-hover')
      .forEach((img) =>
        img.setAttribute(
          'src',
          boxUrl(
            img.getAttribute('version'),
            img.getAttribute('key'),
            this.language
          )
        )
      );
    this.shadowRoot
      .querySelectorAll('div.div-link>img.hover')
      .forEach((img) =>
        img.setAttribute(
          'src',
          boxUrlH(
            img.getAttribute('version'),
            img.getAttribute('key'),
            this.language
          )
        )
      );
  }

  async setConstants() {
    this.constants = await loadConstants(this.language);
  }

  render() {
    this.shadowRoot.appendChild(HomeContent(this.language, this.constants));
  }
}

customElements.define('home-mfe', HomeMFE);

export const mount = (language, ref) => {
  const home = parseHTML(html`<home-mfe language=${language}></home-mfe>`);
  const node = document.querySelector(ref);
  node.appendChild(home);
};

export const unMount = () => {
  const home = document.querySelector('home-mfe');
  home?.remove();
};
