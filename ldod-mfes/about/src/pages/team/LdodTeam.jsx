import { hideHomeInfo, showHomeInfo } from '../../aboutRouter';

const loadComponent = async (lang) =>
  (await import(`./components/Team-${lang}.jsx`)).default();

export class LdodTeam extends HTMLElement {
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
    return <div id="aboutWrapper" class="ldod-about"></div>;
  }

  async render() {
    const wrapper = this.querySelector('#aboutWrapper');
    wrapper.appendChild(<div>{await loadComponent(this.language)}</div>);
    wrapper.children;
    wrapper.childNodes.length > 1 && wrapper.firstChild.remove();
  }
}
!customElements.get('ldod-team') &&
  customElements.define('ldod-team', LdodTeam);
