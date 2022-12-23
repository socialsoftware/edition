import { ldodEventPublisher } from 'shared/ldod-events.js';
import langMenu from './lang-menu';
class LangMenu extends HTMLLIElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  connectedCallback() {
    !this.hasChildNodes && this.render();
    this.addEventListeners();
  }

  render() {
    this.innerHTML = langMenu;
    this.setActive();
  }

  addEventListeners = () => {
    Array.from(this.children).forEach((ele) =>
      ele.addEventListener('click', this.updateLanguage)
    );
  };

  updateLanguage() {
    const anchors = Array.from(this.parentNode.children);
    anchors.forEach((anchor) => anchor.classList.remove('active'));
    this.classList.add('active');
    ldodEventPublisher('language', this.id);
  }

  setActive() {
    this.querySelectorAll('li.nav-lang>a').forEach(
      (ele) => ele.id === this.language && ele.classList.add('active')
    );
  }
}

customElements.define('lang-menu', LangMenu, { extends: 'li' });
