import { selectedInters } from '../virtual';
import { getVirtualFragmentNavInters } from './apiRequests';
import Navigation from './components/Navigation';
import constants from './constants';
const getStyle = async () => (await import('./style.css?inline')).default;

const loadTooltip = () => import('shared/tooltip.js');

export class VirtualNavigation extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    this.selectedInters = [];
  }

  get wrapper() {
    return this.shadowRoot.querySelector('div#virtual-navigationWrapper');
  }

  get urlId() {
    return this.getAttribute('urlId');
  }

  get fragment() {
    return this.getAttribute('fragment');
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return constants[this.language][key];
  }

  updateSelectedInters = () => {
    if (this.urlId)
      this.selectedInters.push(
        Object.values(this.inters)
          .map(([inter]) => inter)
          .find((inter) => inter.urlId === this.urlId)?.externalId
      );
  };

  async connectedCallback() {
    this.fragment && (await this.fetchData());
    this.updateSelectedInters();
    this.shadowRoot.appendChild(<style>{await getStyle()}</style>);
    this.shadowRoot.appendChild(<div id="virtual-navigationWrapper"></div>);
    this.render();
  }

  fetchData = async () => {
    await getVirtualFragmentNavInters(this.fragment, selectedInters)
      .then((data) => (this.inters = data))
      .catch((error) => console.error(error));
  };

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
  };

  handleChangedLanguage = () => {
    this.shadowRoot.querySelectorAll('[data-virtual-key]').forEach((node) => {
      node.firstChild.textContent = this.getConstants(node.dataset.virtualKey);
    });
    this.shadowRoot
      .querySelectorAll('[data-virtual-tooltip-key]')
      .forEach((node) =>
        node.setAttribute(
          'content',
          this.getConstants(node.dataset.virtualTooltipKey)
        )
      );
  };

  addEventListeners = () => {
    this.wrapper.addEventListener('pointerenter', loadTooltip);
  };

  onCheckboxChange = ({ target }) => {
    this.updateInters(target.id);
    this.dispatchCustomEvent('ldod-virtual-selected', {
      inters: this.selectedInters,
    });
  };

  updateInters = (id) => {
    this.selectedInters =
      this.selectedInters.indexOf(id) !== -1
        ? this.selectedInters.filter((externalId) => externalId !== id)
        : [...this.selectedInters, id];
  };

  dispatchCustomEvent = (event, detail, emmiter = this) => {
    emmiter.dispatchEvent(
      new CustomEvent(event, {
        detail,
        bubbles: true,
        composed: true,
      })
    );
  };
}
!customElements.get('virtual-navigation') &&
  customElements.define('virtual-navigation', VirtualNavigation);
