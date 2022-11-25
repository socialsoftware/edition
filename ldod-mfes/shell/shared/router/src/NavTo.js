export default class NavTo extends HTMLAnchorElement {
  get to() {
    let toAttr = this.getAttribute('to');
    toAttr = typeof toAttr === 'string' ? toAttr.trim() : toAttr;
    return toAttr;
  }

  get mfe() {
    return this.to?.split('/')[1];
  }

  get hasTo() {
    return this.hasAttribute('to');
  }

  get publishedMfes() {
    return window.mfes || [];
  }

  connectedCallback() {
    this.checkIfMfesIsPublished();
    if (this.target) {
      this.href = this.to;
      return;
    }
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

  checkIfMfesIsPublished = () => {
    if (this.target || !this.hasTo) return;
    if (!this.publishedMfes.includes(this.mfe)) this.style.display = 'none';
  };
}

customElements.define('nav-to', NavTo, { extends: 'a' });
