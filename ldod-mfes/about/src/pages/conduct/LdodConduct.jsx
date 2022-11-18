import { hideHomeInfo, showHomeInfo } from '../../aboutRouter';

const loadComponent = async (lang) =>
  (await import(`./components/Conduct-${lang}.jsx`)).default();

const conductTitle = {
  en: 'Code of Conduct',
  es: 'Código de Conducta',
  pt: 'Código de Conduta',
};

export class LdodConduct extends HTMLElement {
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

  getTitle() {
    if (!this.title) return;
    return (
      <>
        <h1 class="text-center">{conductTitle[this.language]}</h1>
        <p>&nbsp;</p>
      </>
    );
  }

  wrapper() {
    return <div id="aboutWrapper" class="ldod-about"></div>;
  }

  async render() {
    const wrapper = this.querySelector('#aboutWrapper');
    wrapper.appendChild(
      <div>
        {this.getTitle()}
        {await loadComponent(this.language)}
      </div>
    );
    wrapper.children;
    wrapper.childNodes.length > 1 && wrapper.firstChild.remove();
  }
}
!customElements.get('ldod-conduct') &&
  customElements.define('ldod-conduct', LdodConduct);
