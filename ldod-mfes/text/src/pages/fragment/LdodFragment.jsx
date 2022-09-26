import { getFragmentInters, updateFragmentInter } from '../../apiRequests';
import EditorialInter from './components/EditorialInter';
import SourceInter from './components/SourceInter';
import TextNavigation from './components/TextNavigation';
import Title from './components/Title';
import constants from './constants';
import fragmentsConstants from '../fragments/constants';
import style from './style.css?inline';
import {
  checkBoxes,
  isLineByLine,
  isSideBySide,
  isSingleAndEditorial,
  isSingleAndSourceInter,
} from './utils';
import SideBySideTranscriptions from './components/SideBySideTranscriptions';
import LineByLineTranscriptions from './components/LineByLineTranscriptions';

const loadTooltip = () => import('shared/tooltip.js');

export class LdodFragment extends HTMLElement {
  constructor(lang, data, xmlId, urlId) {
    super();
    this.language = lang;
    this.xmlId = xmlId;
    this.updateFragmentState(data, urlId);
    this.editorialInters = ['JPC', 'TSC', 'RZ', 'JP'].map(
      (acrn) => data.expertsInterMap[acrn] ?? false
    );
    this.transcriptionCheckboxes = checkBoxes;
  }

  updateFragmentState = (data, urlId) => {
    this.data = data;
    this.urlId = urlId;
    this.inters = new Set(
      this.hasInters()
        ? [...data.inters.map(({ externalId }) => externalId)]
        : []
    );
  };

  get wrapper() {
    return this.querySelector('div#fragmentWrapper');
  }

  get language() {
    return this.getAttribute('language');
  }

  set language(lang) {
    this.setAttribute('language', lang);
  }

  hasInters() {
    return this.urlId || this.intersSize();
  }

  getConstants(key, ...args) {
    const constant =
      constants[this.language][key] ?? fragmentsConstants[this.language][key];
    return args.length ? constant(...args) : constant;
  }

  getRequestBody() {
    this.updateTranscriptCheckboxesValue();
    return {
      inters: Array.from(this.inters),
      ...checkBoxes,
    };
  }

  static get observedAttributes() {
    return ['language'];
  }

  connectedCallback() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<div id="fragmentWrapper"></div>);
    this.render();
  }

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <br />
        <div id="fragInterContainer">
          <div id="transcription">
            {this.hasInters() && isSingleAndSourceInter(this.data.inters) && (
              <SourceInter node={this} inter={this.data.inters[0]} />
            )}
            {this.hasInters() && isSingleAndEditorial(this.data.inters) && (
              <EditorialInter node={this} inter={this.data.inters[0]} />
            )}
            {this.hasInters() && isSideBySide(this.data) && (
              <SideBySideTranscriptions node={this} inters={this.data.inters} />
            )}
            {this.hasInters() && isLineByLine(this.data) && (
              <LineByLineTranscriptions node={this} inters={this.data.inters} />
            )}
            {!this.hasInters() && <Title title={this.data.title} />}
          </div>
          <div id="navigation">
            <TextNavigation node={this} />
          </div>
        </div>
      </>
    );
    this.addEventListeners();
  }
  attributeChangedCallback(name, oldV, newV) {
    this.handeChangedAttribute[name](oldV, newV);
  }

  handeChangedAttribute = {
    language: (oldV, newV) => {
      oldV && oldV !== newV && this.handleChangedLanguage();
    },
    urlid: (oldV, newV) => {
      oldV && oldV !== newV && this.render();
    },
  };

  intersSize = () => this.inters?.size;

  addRemoveInter(externalId) {
    return this.inters.has(externalId)
      ? this.inters.delete(externalId)
      : this.inters.add(externalId);
  }

  removeAllInters() {
    this.selectedInters.clear();
  }

  addEventListeners = () => {
    this.wrapper
      .querySelectorAll('[data-tooltipkey]')
      .forEach((ele) =>
        ele.parentNode.addEventListener('pointerenter', loadTooltip)
      );
  };

  updateTranscriptCheckboxesValue = (def) => {
    if (def) return (this.transcriptionCheckboxes = checkBoxes);
    this.querySelectorAll(
      "div#text-checkBoxesContainer input[type='checkbox']"
    ).forEach((input) => {
      this.transcriptionCheckboxes[input.name] = input.checked;
    });
  };

  handleChangedLanguage = () => {
    this.querySelectorAll('[data-key]').forEach(
      (node) =>
        (node.firstChild.textContent = this.getConstants(node.dataset.key))
    );
    this.querySelectorAll('[data-tooltipkey]').forEach((node) =>
      node.setAttribute('content', this.getConstants(node.dataset.tooltipkey))
    );
  };

  handleInterCheckboxChange = async (externalId) => {
    this.addRemoveInter(externalId);
    if (!this.intersSize()) this.urlId = '';
    if (this.intersSize() === 1)
      this.data = await getFragmentInters(this.xmlId, this.getRequestBody());
    if (this.intersSize() > 1)
      this.data = await getFragmentInters(this.xmlId, this.getRequestBody());
    this.updateTranscriptCheckboxesValue(true);
    this.updateFragmentState(this.data, this.urlId);
    this.render();
  };

  handleTranscriptionCheckboxChange = async () => {
    console.log(this.transcriptionCheckboxes);
    this.updateTranscriptCheckboxesValue();
    const data = await updateFragmentInter(
      this.xmlId,
      this.urlId,
      this.getRequestBody()
    );
    this.updateFragmentState(data, this.urlId);
    this.render();
  };

  handleChangeFac = ({ page }) => {
    const surface = this.data.inters[0].surfaceDetailsList[page];
    this.transcriptionCheckboxes.pbText = surface.pbText;
    updateFragmentInter(this.xmlId, this.urlId, this.getRequestBody()).then(
      (data) => {
        this.data = data;
        this.querySelector('div#transcriptionContainer').innerHTML =
          data.transcriptions[0];
      }
    );
  };

  disconnectedCallback() {}
}
!customElements.get('ldod-fragment') &&
  customElements.define('ldod-fragment', LdodFragment);
