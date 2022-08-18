const loadComponent = async (lang) =>
  (await import(`./components/Ack-${lang}.jsx`)).default();

export class LdodAck extends HTMLElement {
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
    return <div id="aboutWrapper" class="ldod-about"></div>;
  }

  async render() {
    this.querySelector('#aboutWrapper').innerHTML = '';
    this.querySelector('#aboutWrapper').appendChild(
      await loadComponent(this.language)
    );
  }
}
!customElements.get('ldod-ack') && customElements.define('ldod-ack', LdodAck);
