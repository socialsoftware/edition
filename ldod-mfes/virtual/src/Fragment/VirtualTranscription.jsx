import { computeSelectPureHeight } from '../utils';
import {
  associateTagsRequest,
  dissociateTagRequest,
  getVirtualFragmentInter,
  getVirtualFragmentInters,
} from './apiRequests';
import Transcription from './components/Transcription';
import VirtualIntersCompare from './components/VirtualIntersCompare';
import constants from './constants';

const getStyle = async () => (await import('./style.css?inline')).default;
const AssociateModal = async (node) =>
  (await import('./components/associate-tag-modal/AssociateTagModal')).default({
    node,
  });
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

  get associateTagModal() {
    return document.body.querySelector('ldod-modal#virtual-associateTagModal');
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
        this.inter = data.inter;
        this.taxonomy = data.taxonomy;
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

  associateTag = async () => {
    document.body.appendChild(await AssociateModal(this));
    document.body.addEventListener('click', this.computeSelectHeight);
    document.body.addEventListener('ldod-modal-close', this.removeModal);
    this.associateTagModal.toggleAttribute('show', true);
  };

  computeSelectHeight = () => {
    computeSelectPureHeight(undefined, undefined, 120);
  };

  dissociateTag = async ({ target }) => {
    await dissociateTagRequest(target.dataset.interId, target.dataset.catId)
      .then((data) => {
        this.inter = data.inter;
        this.taxonomy = data.taxonomy;
      })
      .catch(this.onError);
    this.render();
  };

  onAssociateTags = async () => {
    const body = [
      ...new Set(this.associateTagModal.querySelector('select-pure').values),
    ];
    await associateTagsRequest(this.inter.externalId, body)
      .then((data) => {
        this.inter = data.inter;
        this.taxonomy = data.taxonomy;
        this.removeModal();
        this.render();
      })
      .catch(this.onError);
  };

  removeModal = (e = {}) => {
    if (e.detail && e.detail.id !== 'virtual-associateTagModal') return;
    document.body.removeEventListener('click', this.computeSelectHeight);
    document.body.removeEventListener('ldod-modal-close', this.removeModal);
    this.associateTagModal.remove();
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
