import { renderInfo } from '../../aboutRouter';

const loadComponent = async (lang) =>
  (await import(`./components/Tutorials-${lang}.jsx`)).default();

export class LdodTutorials extends HTMLElement {
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
    renderInfo();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handlers[name](oldV, newV);
  }

  disconnectedCallback() {
    renderInfo();
  }

  handlers = {
    language: (oldV, newV) => {
      if (!oldV || oldV === newV) return;
      this.render();
    },
  };

  wrapper() {
    return <div id="aboutWrapper" class="ldod-about"></div>;
  }

  async render() {
    const wrapper = this.querySelector('#aboutWrapper');
    wrapper.appendChild(<div>{await loadComponent(this.language)}</div>);
    wrapper.children;
    wrapper.childNodes.length > 1 && wrapper.firstChild.remove();
  }
}
!customElements.get('ldod-tutorials') &&
  customElements.define('ldod-tutorials', LdodTutorials);
