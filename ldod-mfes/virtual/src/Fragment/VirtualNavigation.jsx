import { selectedInters } from '../selectedInters';
import { addInterRequest, getVirtualFragmentNavInters } from './apiRequests';
import Navigation from './components/Navigation';
import constants from './constants';
const getStyle = async () => (await import('./style.css?inline')).default;

const loadTooltip = () =>
  import.meta.env.DEV
    ? import('shared/tooltip.dev.js')
    : import('shared/tooltip.js');

export class VirtualNavigation extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    this.intersChecked = [];
    this.veId = '';
    this.interId = '';
  }

  get wrapper() {
    return this.shadowRoot.querySelector('div#virtual-navigationWrapper');
  }

  get urlId() {
    return this.getAttribute('urlid');
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
      this.intersChecked.push(
        this.inters
          .map((obj) => obj.inters)
          .map(([inter]) => inter)
          .find((inter) => inter?.urlId === this.urlId)?.externalId
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
    await getVirtualFragmentNavInters(this.fragment, {
      inters: selectedInters,
      currentInterId:
        this.intersChecked.length === 1 ? this.intersChecked[0] : null,
      urlId: this.urlId,
    })
      .then((data) => (this.inters = data))
      .catch((error) => console.error(error));
  };

  fetchAddInter = async () => {
    await addInterRequest(this.fragment, this.veId, this.interId, {
      inters: selectedInters,
      currentInterId: this.intersChecked.length === 1 && this.intersChecked[0],
    })
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
    this.wrapper.addEventListener('pointerenter', loadTooltip, { once: true });
  };

  onCheckboxChange = async ({ target }) => {
    this.updateInters(target.id);
    this.dispatchCustomEvent('ldod-virtual-selected', {
      inters: this.intersChecked,
    });
    if (!this.intersChecked.length) return;
    await this.fetchData();
    this.render();
  };

  updateInters = (id) => {
    this.intersChecked =
      this.intersChecked.indexOf(id) !== -1
        ? this.intersChecked.filter((externalId) => externalId !== id)
        : [...this.intersChecked, id];
  };

  addInterToVe = async ({ target }) => {
    this.veId = target.dataset.veId;
    this.interId = target.dataset.interId;
    await this.fetchAddInter();
    this.render();
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
