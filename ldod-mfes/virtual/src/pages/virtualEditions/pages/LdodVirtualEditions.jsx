import { getVirtualEditions } from '@src/apiRequests';
import constants from '../../constants';
import CreateButton from '../components/createVE/CreateButton';
import ManagePopover from '../components/table/editionActions/ManagePopover';
import VETable from '../components/table/VETable';
import Title from '../components/Title';
const loadPopper = () => import('shared/tooltip.js');

import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export class LdodVirtualEditions extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  get wrapper() {
    return this.querySelector('div#virtualEditionsWrapper');
  }

  get veTable() {
    return this.querySelector('div#virtual-virtualEditionsContainer');
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return constants[this.language][key];
  }

  connectedCallback() {
    this.appendChild(<div id="virtualEditionsWrapper"></div>);
    this.render();
    this.addEventListeners();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangedAttribute[name](oldV, newV);
  }

  disconnectedCallback() {}

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <Title node={this} />
        <ldod-popover element={ManagePopover(this)}></ldod-popover>
        <CreateButton node={this} />
        <div id="virtual-virtualEditionsContainer">
          <VETable node={this} constants={constants} />
        </div>
        <ldod-ve-editors language={this.language}></ldod-ve-editors>
        <ldod-ve-games language={this.language}></ldod-ve-games>
        <ldod-ve-edit language={this.language}></ldod-ve-edit>
        <ldod-ve-assisted language={this.language}></ldod-ve-assisted>
        <ldod-ve-manual language={this.language}></ldod-ve-manual>
      </>
    );
  }

  sortVirtualEditions = () => {
    this.virtualEditions
      .sort((ed1, ed2) => ed1.acronym.localeCompare(ed2.acronym))
      .sort((ed1, ed2) => +ed2.member?.pending - +ed1.member?.pending)
      .sort((ed1, ed2) => +ed2.member?.active - +ed1.member?.active)
      .sort((ed1, ed2) => +ed2.selected - +ed1.selected);
    this.render();
  };

  updateEdition = (edition) => {
    const index = this.virtualEditions
      .map((ve) => ve.externalId)
      .indexOf(edition.externalId);
    this.virtualEditions[index] = {
      ...this.virtualEditions[index],
      ...edition,
    };

    this.sortVirtualEditions();
  };

  updateTable = async () => {
    const data = await getVirtualEditions();
    this.virtualEditions = data.virtualEditions;
    this.user = data.user;
    this.veTable.innerHTML = '';
    this.veTable.appendChild(<VETable node={this} constants={constants} />);
  };

  onChangedAttribute = {
    language: (oldV, newV) =>
      oldV && oldV !== newV && this.onChangedLanguage(oldV, newV),
  };

  onChangedLanguage = () => {
    this.querySelectorAll('[language]').forEach((ele) =>
      ele.setAttribute('language', this.language)
    );
    this.querySelectorAll('[data-virtualkey]').forEach((node) => {
      return (node.firstChild.textContent = this.getConstants(
        node.dataset.virtualkey
      ));
    });
    this.querySelectorAll('[data-virtualtooltipkey]').forEach((tooltip) =>
      tooltip.setAttribute(
        'content',
        this.getConstants(tooltip.dataset.virtualtooltipkey)
      )
    );
  };

  addEventListeners = () => {
    this.wrapper.addEventListener('pointerenter', loadPopper, { once: true });
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
!customElements.get('ldod-virtual-editions') &&
  customElements.define('ldod-virtual-editions', LdodVirtualEditions);
