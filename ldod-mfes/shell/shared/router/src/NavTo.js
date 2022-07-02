export default class NavTo extends HTMLAnchorElement {
  constructor() {
    super();
  }

  get to() {
    return this.getAttribute('to');
  }

  connectedCallback() {
    this.addEventListener('click', this.onclick);
  }

  onclick(e) {
    e.preventDefault();
    this.emittURLEvent();
  }

  emittURLEvent() {
    this.dispatchEvent(
      new CustomEvent('ldod-url-changed', {
        composed: true,
        bubbles: true,
        detail: { path: this.to },
      })
    );
  }
}

customElements.define('nav-to', NavTo, { extends: 'a' });
