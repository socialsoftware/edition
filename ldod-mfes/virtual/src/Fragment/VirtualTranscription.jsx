import {
  getVirtualFragmentInter,
  getVirtualFragmentInters,
} from './apiRequests';
import Transcription from './components/Transcription';
import VirtualIntersCompare from './components/VirtualIntersCompare';
import constants from './constants';

const getStyle = async () => (await import('./style.css?inline')).default;

export class VirtualTranscription extends HTMLElement {
  constructor() {
    super();
    this.inter = undefined;
    this.taxonomy = undefined;
    this.inters = [];
    this.attachShadow({ mode: 'open' });
    this.constants = constants;
  }
  get wrapper() {
    return this.shadowRoot.querySelector('div#virtual-transcriptionWrapper');
  }
  get xmlId() {
    return this.getAttribute('xmlid');
  }

  get urlId() {
    return this.getAttribute('urlId');
  }

  get language() {
    return this.getAttribute('language');
  }

  static get observedAttributes() {
    return ['language'];
  }
  getConstants(key) {
    return this.constants[this.language][key];
  }

  getIntersChecked = () =>
    Array.from(
      this.getRootNode()
        .querySelector('virtual-navigation')
        .shadowRoot.querySelectorAll("td>input[type='checkbox']")
    ).filter((input) => input.checked);

  fetchVirtualFragmentInter = (xmlId, urlId) =>
    getVirtualFragmentInter(xmlId, urlId)
      .then((data) => {
        this.inter = data.inters?.[0];
        this.taxonomy = data.taxonomies?.[0];
      })
      .catch(this.onError);

  fetchVirtualFragmentInters = (xmlId, intersId) =>
    getVirtualFragmentInters(xmlId, intersId)
      .then((data) => (this.inters = data))
      .catch(this.onError);

  fetchData = async () => {
    if (!this.xmlId) return;
    if (!this.urlId && !this.getIntersChecked().length) return;
    if (this.urlId || this.getIntersChecked().length === 1)
      return this.fetchVirtualFragmentInter(
        this.xmlId,
        this.urlId || this.getIntersChecked()[0].name
      );

    return this.fetchVirtualFragmentInters(
      this.xmlId,
      this.getIntersChecked().map((input) => input.id)
    );
  };

  async connectedCallback() {
    await this.fetchData();
    this.shadowRoot.appendChild(<style>{await getStyle()}</style>);
    this.shadowRoot.appendChild(<div id="virtual-transcriptionWrapper"></div>);
    this.render();
  }

  render() {
    console.log(this.inters);
    this.wrapper.innerHTML = '';
    this.inter && this.wrapper.appendChild(<Transcription node={this} />);
    this.inters.length &&
      this.wrapper.appendChild(<VirtualIntersCompare node={this} />);
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
    this.shadowRoot
      .querySelectorAll('[language')
      .forEach((node) => node.setAttribute('language', this.language));
    this.shadowRoot.querySelectorAll('[data-virtual-key]').forEach((node) => {
      node.firstChild.textContent = this.getConstants(node.dataset.virtualKey);
    });
  };

  onError = (error) => {
    console.error(error);
    this.dispatchCustomEvent('ldod-error', { message: error?.message });
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
!customElements.get('virtual-transcription') &&
  customElements.define('virtual-transcription', VirtualTranscription);
