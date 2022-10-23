import { getVeGame } from '../../../../apiRequests';
import constants from '../../../constants';

export class LdodVeClassGame extends HTMLElement {
  constructor(lang) {
    super();
    this.language = lang;
    this.constants = constants;
  }

  set language(lang) {
    this.setAttribute('language', lang);
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }

  fetchData = async () => {
    await getVeGame(history.state?.gameId)
      .then((data) => (this.game = data))
      .catch((error) => console.error(error));
  };

  getConstants = (key) => this.constants[this.language][key];

  async connectedCallback() {
    await this.fetchData();
    console.log(this.game);
    this.render();
  }

  render = () => {};

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name](oldV, newV);
  }

  onChangeAttribute = {
    language: (oldV, newV) =>
      oldV && oldV !== newV && this.handleChangeLanguage(),
  };

  handleChangeLanguage = () => {
    this.querySelectorAll('[data-virtual-key]').forEach((ele) => {
      ele.firstChild.textContent = this.getConstants(ele.dataset.virtualKey);
    });
  };

  disconnectedCallback() {}
}
!customElements.get('ldod-ve-class-game') &&
  customElements.define('ldod-ve-class-game', LdodVeClassGame);
