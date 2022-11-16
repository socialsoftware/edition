import { getVirtualEditions } from '@src/apiRequests';
import {
  createVirtualEdition,
  getEditionGames,
  getVeIntersForManual,
  getVeIntersWithRecommendation,
  getVirtualEdition,
  veAdminDelete,
} from '@src/restrictedApiRequests';
import constants from '../../../constants';
import CreateButton from './components/createVE/CreateButton';
import VETable from './components/VETable';
import Title from './components/Title';
const loadPopper = () => import('shared/tooltip.js');
const CreateVeModal = async (node) =>
  (await import('./components/createVE/CreateVEModal')).default({
    node,
  });

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
    this.sortVirtualEditions();
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
        <ldod-popover></ldod-popover>
        <CreateButton node={this} />
        <div id="virtual-virtualEditionsContainer">
          <VETable node={this} constants={constants} />
        </div>
        <ldod-ve-editors language={this.language}></ldod-ve-editors>
        <ldod-ve-games language={this.language}></ldod-ve-games>
        <ldod-ve-edit language={this.language}></ldod-ve-edit>
        <ldod-ve-assisted language={this.language}></ldod-ve-assisted>
        <ldod-ve-manual language={this.language}></ldod-ve-manual>
        <ldod-ve-taxonomy language={this.language}></ldod-ve-taxonomy>
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
    this.addEventListeners();
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
    this.sortVirtualEditions();
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
    this.wrapper.addEventListener('pointerenter', this.createVeLazyLoad, {
      once: true,
    });
  };

  createVeLazyLoad = async () =>
    this.wrapper.appendChild(await CreateVeModal(this));

  dispatchCustomEvent = (event, detail, emmiter = this) => {
    emmiter.dispatchEvent(
      new CustomEvent(event, {
        detail,
        bubbles: true,
        composed: true,
      })
    );
  };

  //actions

  onCreateVE = (e) => {
    e.preventDefault();
    const veDto = Object.fromEntries(new FormData(e.target));
    createVirtualEdition(veDto)
      .then(({ virtualEditions, user }) => {
        this.virtualEditions = virtualEditions;
        this.user = user;
        this.sortVirtualEditions();
      })
      .catch((error) => {
        this.dispatchCustomEvent('ldod-error', {
          message: error?.message || '',
        });
      });
  };

  onGamesModal = async () => {
    await import('./components/games/LdodVeGames');
    const ldodVeGames = this.querySelector('ldod-ve-games');
    ldodVeGames.edition = this.edition;
    getEditionGames(this.edition.externalId)
      .then((data) => {
        ldodVeGames.updateData(data);
        ldodVeGames.parent = this;
        ldodVeGames.toggleAttribute('show');
      })
      .catch((error) => this.dispatchCustomEvent('ldod-error', error));
  };

  onTaxonomy = async () => {
    await import('./components/taxonomy/LdodVeTaxonomy');
    const ldodVeTaxonomy = this.querySelector('ldod-ve-taxonomy');
    ldodVeTaxonomy.parent = this;
    ldodVeTaxonomy.toggleAttribute('show');
  };

  onManualModal = async () => {
    await import('./components/manual/LdodVeManual');
    const ldodVeManual = this.querySelector('ldod-ve-manual');
    ldodVeManual.edition = this.edition;
    getVeIntersForManual(this.edition.externalId)
      .then((data) => {
        ldodVeManual.parent = this;
        ldodVeManual.initialInters = Array.from(data);
        ldodVeManual.updateData(data);
      })
      .catch((error) => this.dispatchCustomEvent('ldod-error', error));
  };

  onAssistModal = async () => {
    await import('./components/assisted/LdodVeAssisted');
    const ldodVeAssisted = this.querySelector('ldod-ve-assisted');
    ldodVeAssisted.edition = this.edition;
    getVeIntersWithRecommendation(this.edition.externalId)
      .then((data) => {
        ldodVeAssisted.parent = this;
        ldodVeAssisted.updateData(data);
      })
      .catch((error) => this.dispatchCustomEvent('ldod-error', error));
  };

  onEditorsModal = async () => {
    await import('./components/editors/LdodVeEditors');
    const element = this.querySelector('ldod-ve-editors');
    getVirtualEdition(this.edition.externalId)
      .then((data) => {
        element.edition = data;
        element.parent = this;
        element.toggleAttribute('show');
      })
      .catch((error) => this.dispatchCustomEvent('ldod-error', error));
  };

  onEditVe = async () => {
    await import('./components/editVE/LdodVeEdit.jsx');
    const ldodVeEdit = this.querySelector('ldod-ve-edit');
    ldodVeEdit.edition = this.edition;
    ldodVeEdit.parent = this;
    ldodVeEdit.toggleAttribute('show');
  };

  onRemoveVE = async () => {
    if (!confirm(`Delete ${this.edition.acronym} ?`)) return;
    veAdminDelete(this.edition.externalId)
      .then(({ virtualEditions, user }) => {
        this.virtualEditions = virtualEditions;
        this.user = user;
        this.sortVirtualEditions();
        this.edition = null;
      })
      .catch((message) => this.dispatchCustomEvent('ldod-error', message));
  };
}
!customElements.get('ldod-virtual-editions') &&
  customElements.define('ldod-virtual-editions', LdodVirtualEditions);
