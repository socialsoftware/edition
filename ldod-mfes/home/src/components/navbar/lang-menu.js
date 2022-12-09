import { ldodEventBus } from "shared/ldod-events.js";
class LangMenu extends HTMLLIElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  connectedCallback() {
    this.render();
    this.setActive();
  }

  render() {
    this.classList.add('nav-lang');
    this.innerHTML = html`
      <a class="" id="pt">PT</a>
      <a class="" id="en">EN</a>
      <a class="" id="es">ES</a>
    `;
    Array.from(this.children).forEach((ele) =>
      ele.addEventListener('click', this.updateLanguage)
    );
  }

  updateLanguage() {
    const anchors = Array.from(this.parentNode.children);
    anchors.forEach((anchor) => anchor.classList.remove('active'));
    this.classList.add('active');
    ldodEventBus.publish("ldod:language", this.id)
  }

  setActive() {
    this.querySelectorAll('li.nav-lang>a').forEach(
      (ele) => ele.id === this.language && ele.classList.add('active')
    );
  }
}

customElements.define('lang-menu', LangMenu, { extends: 'li' });
