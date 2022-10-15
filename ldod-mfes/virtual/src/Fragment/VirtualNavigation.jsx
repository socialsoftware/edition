import { fetcher } from 'shared/fetcher.js';
import Navigation from './components/Navigation';
import constants from './constants';
import style from './style.css?inline';
const HOST = import.meta.env.VITE_HOST;

const loadTooltip = () => import('shared/tooltip.js');

const getVirtualFragment = async (xmlId) =>
  await fetcher.get(`${HOST}/virtual/fragment/${xmlId}`, null);

export class VirtualNavigation extends HTMLElement {
  constructor() {
    super();
  }

  get wrapper() {
    return this.querySelector('div#virtual-navigationWrapper');
  }

  get fragment() {
    return this.getAttribute('fragment');
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language', 'fragment'];
  }

  getConstants(key) {
    return constants[this.language][key];
  }

  connectedCallback() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<div id="virtual-navigationWrapper"></div>);
    this.render();
  }

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(<Navigation node={this} />);
    this.addEventListeners();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handeChangedAttribute[name](oldV, newV);
  }

  handeChangedAttribute = {
    language: (oldV, newV) => {
      oldV && oldV !== newV && this.handleChangedLanguage();
    },
    fragment: (oldV, newV) => {
      oldV !== newV && this.handleChangeFragment();
    },
  };

  handleChangedLanguage = () => {
    this.querySelectorAll('[data-virtualkey]').forEach((node) => {
      node.firstChild.textContent = this.getConstants(node.dataset.virtualkey);
    });
    this.querySelectorAll('[data-virtualtooltipkey]').forEach((node) =>
      node.setAttribute(
        'content',
        this.getConstants(node.dataset.virtualtooltipkey)
      )
    );
  };

  handleChangeFragment = async () => {
    const data = await getVirtualFragment(this.fragment);
    console.log(data);
  };

  disconnectedCallback() {}

  addEventListeners = () => {
    this.wrapper
      .querySelectorAll('[data-virtualtooltipkey]')
      .forEach((ele) =>
        ele.parentNode.addEventListener('pointerenter', loadTooltip)
      );
  };
}
!customElements.get('virtual-navigation') &&
  customElements.define('virtual-navigation', VirtualNavigation);
