import FragsTable from './components/FragsTable.jsx';
import Title from './components/Title.jsx';
import constants from './constants.js';
import { dispatchCustomEvent } from '@src/utils.js';
import { removeFragmentById, removeAllFragments } from '@src/apiRequests';
import UploadButtons from './components/uploadButtons.jsx';
import ExportButtons from './components/exportButtons.jsx';
import('shared/buttons.js').then(({ ldodButton }) => ldodButton());

async function loadToolip() {
  await import('shared/tooltip.js');
}

export class LdodManageFragments extends HTMLElement {
  constructor() {
    super();
  }

  get selectedRows() {
    return this.querySelectorAll('table>tbody>tr[selected]');
  }

  get exportSelectedElement() {
    return this.querySelector('ldod-export[id="exportSelected"]');
  }

  get exportHeadElement() {
    return this.querySelector("thead>tr>th[data-key='export']");
  }

  get language() {
    return this.getAttribute('language');
  }
  get wrapper() {
    return this.querySelector('div#manageFragmentsWrapper');
  }

  get numberOfFragments() {
    return this.fragments.length;
  }

  static get observedAttributes() {
    return ['language', 'data'];
  }

  getSelectedFrags() {
    return JSON.stringify(this.selectedFrags);
  }

  getConstants(key, ...args) {
    const constant = constants[this.language][key];
    return args.length ? constant(...args) : constant;
  }

  connectedCallback() {
    this.appendChild(<div id="manageFragmentsWrapper"></div>);
    this.render();
    this.addEventListeners();
  }

  render() {
    if (!this.hasAttribute('data')) return;
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <Title
          title={this.getConstants('manageFragments', this.numberOfFragments)}
          numberOfFragments={this.numberOfFragments}
        />
        <div id="removeAllContainer" class="text-center">
          <ldod-button
            class="btn btn-danger"
            data-buttonkey="removeAll"
            title={this.getConstants('removeAll')}
            onClick={this.handleRemoveAll}></ldod-button>
        </div>
        <div class="buttons-column">
          <UploadButtons
            uploadSingle={this.getConstants('uploadSingle')}
            uploadMultiple={this.getConstants('uploadMultiple')}
            uploadCorpus={this.getConstants('uploadCorpus')}
          />
          <ExportButtons
            exportAll={this.getConstants('exportAll')}
            exportSelected={this.getConstants('exportSelected')}
            exportRandom={this.getConstants('exportRandom')}
            node={this}
          />
        </div>
        <FragsTable node={this} constants={constants} />
      </>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handeChangedAttribute[name](oldV, newV);
  }

  handeChangedAttribute = {
    language: (oldV, newV) => {
      if (oldV && oldV !== newV) {
        this.querySelectorAll('[data-key]').forEach((node) => {
          return (node.firstChild.textContent = node.dataset.args
            ? this.getConstants(node.dataset.key, JSON.parse(node.dataset.args))
            : this.getConstants(node.dataset.key));
        });
        this.querySelectorAll('[data-tooltipkey]').forEach((ele) => {
          ele.setAttribute(
            'content',
            this.getConstants(ele.dataset.tooltipkey)
          );
        });
        this.querySelectorAll('[data-buttonkey]').forEach((btn) => {
          btn.setAttribute('title', this.getConstants(btn.dataset.buttonkey));
        });
      }
    },
    data: () => {
      this.render();
      this.exportHeadElement?.addEventListener('click', this.unselectAll);
      this.querySelectorAll('[tooltip-ref]').forEach((tooltipped) => {
        tooltipped.parentNode.addEventListener('pointerenter', loadToolip);
      });
    },
  };

  addEventListeners() {
    this.addEventListener('ldod-file-uploaded', this.handleFileUploaded);
    this.addEventListener('ldod-table-searched', this.updateTitle);
  }

  updateTitle = ({ detail }) => {
    this.querySelector('h3#title').firstChild.textContent = this.getConstants(
      'manageFragments',
      detail.size
    );
  };

  unselectAll = () => {
    this.selectedRows.forEach((tr) => tr.toggleAttribute('selected'));
    this.exportSelectedElement.body = [];
  };

  handleFileUploaded = ({ detail: { data } }) => {
    const nl = document.createElement('br').outerHTML;
    const p = document.createElement('p').outerHTML;

    data.forEach((fragment) => {
      const { externalId, uploaded, overwritten } = fragment;
      if (uploaded) {
        overwritten && this.removeFragment(externalId);
        this.mutateFragments(this.addFragment(fragment));
      }
    });

    const uploadedFrags = data.filter((frag) => frag.uploaded);
    const notUploadedFrags = data.filter((frag) => !frag.uploaded);

    const uploadedFragsResult = uploadedFrags.reduce(
      (accumulated, { xmlId, title, overwritten }) => {
        return `${accumulated}${nl}[${xmlId}(${title})]${
          overwritten ? ' (overwritten)' : ''
        }`;
      },
      `New uploaded fragments: ${uploadedFrags.length}`
    );

    const notUploadedFragsResult = notUploadedFrags.reduce(
      (accumulated, { xmlId, title, overwritten }) => {
        return `${accumulated}${nl}[${xmlId}(${title})]${
          overwritten ? ' (overwritten)' : ''
        }`;
      },
      `\nAlready uploaded fragments: ${notUploadedFrags.length}`
    );

    window.dispatchEvent(
      new CustomEvent('ldod-message', {
        detail: {
          message: uploadedFragsResult.concat(`${p}`, notUploadedFragsResult),
        },
      })
    );
  };

  handleRemoveAll = async () => {
    if (!confirm('Are you sure you want to remove all fragments?')) return;
    const res = await removeAllFragments();
    if (res.ok) this.mutateFragments([]);
    dispatchCustomEvent(
      this,
      { message: res.message },
      { type: res.ok ? 'message' : 'error', bubbles: true, composed: true }
    );
  };

  handleRemoveFragment = async ({ target }) => {
    const id = target.dataset.id;
    const res = await removeFragmentById(target.dataset.id);
    if (res.ok) return this.mutateFragments(this.removeFragment(id));
    dispatchCustomEvent(
      this,
      { message: res.message },
      { type: 'error', bubbles: true, composed: true }
    );
  };

  removeFragment(id) {
    return this.fragments.filter((frag) => frag.externalId !== id);
  }

  addFragment(fragment) {
    this.fragments.reverse().push(fragment);
    return this.fragments.reverse();
  }

  mutateFragments(newFragments) {
    this.toggleAttribute('data', false);
    this.fragments = newFragments;
    this.toggleAttribute('data', true);
  }
}
!customElements.get('ldod-manage-fragments') &&
  customElements.define('ldod-manage-fragments', LdodManageFragments);
