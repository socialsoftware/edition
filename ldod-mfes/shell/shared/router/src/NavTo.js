export default class NavTo extends HTMLAnchorElement {
  get to() {
    let toAttr = this.getAttribute('to');
    toAttr = typeof toAttr === 'string' ? toAttr.trim() : toAttr;
    return toAttr;
  }

  connectedCallback() {
    this.addEventListener('click', this.onclick);
  }

  onclick(e) {
    e.preventDefault();
    this.emitURLEvent();
  }

  emitURLEvent() {
    if (!this.to) return;
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
