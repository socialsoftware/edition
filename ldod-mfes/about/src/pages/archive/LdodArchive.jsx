const loadComponent = async (lang) =>
  (await import(`./components/Archive-${lang}.jsx`)).default();

export class LdodArchive extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }
  static get observedAttributes() {
    return ['language'];
  }

  async connectedCallback() {
    this.appendChild(this.wrapper());
    await this.render();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handlers[name](oldV, newV);
  }

  disconnectedCallback() {}

  handlers = {
    language: (oldV, newV) => {
      if (!oldV || oldV === newV) return;
      this.render();
    },
  };

  wrapper() {
    return (
      <div class="ldod-default">
        <div id="aboutWrapper" class="ldod-about"></div>
      </div>
    );
  }

  async render() {
    this.querySelector('#aboutWrapper').innerHTML = '';
    this.querySelector('#aboutWrapper').appendChild(
      await loadComponent(this.language)
    );
  }
}
!customElements.get('ldod-archive') &&
  customElements.define('ldod-archive', LdodArchive);
