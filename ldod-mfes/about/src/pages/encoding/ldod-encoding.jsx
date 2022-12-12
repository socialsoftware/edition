import { hideHomeInfo, showHomeInfo } from '@src/home-info';
const loadComponent = async (lang) =>
  (await import(`./components/encoding-${lang}.jsx`)).default();

export class LdodEncoding extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  get title() {
    return this.getAttribute('title');
  }

  static get observedAttributes() {
    return ['language'];
  }

  async connectedCallback() {
    this.appendChild(this.wrapper());
    await this.render();
    showHomeInfo();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handlers[name](oldV, newV);
  }

  disconnectedCallback() {
    hideHomeInfo();
  }

  handlers = {
    language: (oldV, newV) => {
      if (!oldV || oldV === newV) return;
      this.render();
    },
  };

  wrapper() {
    return <div id="about-wrapper" class="ldod-about"></div>;
  }

  async render() {
    const wrapper = this.querySelector('#about-wrapper');
    wrapper.appendChild(<div>{await loadComponent(this.language)}</div>);
    wrapper.childNodes.length > 1 && wrapper.firstChild.remove();
  }
}
!customElements.get('ldod-encoding') &&
  customElements.define('ldod-encoding', LdodEncoding);
