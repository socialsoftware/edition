import constants from '@src/pages/constants';
import thisConstants from './constants';
import assistedContants from '../assisted/constants';
import style from './style.css?inline';
import ManualTable, { addTableRow } from './ManualTable';
import { saveReorderedVEInters } from '@src/restrictedApiRequests';

export class LdodVeManual extends HTMLElement {
  constructor() {
    super();
    this.history = [];
    this.constants = Object.entries(thisConstants).reduce(
      (prev, [key, value]) => {
        prev[key] =
          value instanceof Array
            ? value
            : { ...constants[key], ...assistedContants[key], ...value };
        return prev;
      },
      {}
    );
  }

  get language() {
    return this.getAttribute('language');
  }

  get show() {
    return this.hasAttribute('show');
  }

  get modal() {
    return this.querySelector('ldod-modal');
  }

  get table() {
    return this.querySelector('ldod-table#virtual-manualTable');
  }

  get intersId() {
    return this.inters.map(({ externalId }) => externalId);
  }

  get tableBody() {
    return this.querySelector('tbody');
  }

  get rows() {
    return Array.from(this.tableBody.querySelectorAll('tr'));
  }

  get visibleRows() {
    return this.tableBody.querySelectorAll('tr:not([hidden])');
  }

  get visibleFragments() {
    return Array.from(this.visibleRows).map(
      (row) =>
        this.inters.find((inter) => inter.externalId === row.id)?.xmlId || []
    );
  }

  getRowById = (id) => this.tableBody.querySelector(`tr[id="${id}"]`);

  getVisibleRowIndex = (id) =>
    Array.from(this.visibleRows)
      .map((row) => row.id)
      .indexOf(id);

  getFirstVisibleRowIndex = () =>
    this.intersId.indexOf(this.visibleRows.item(0).id);

  getLastVisibleRowIndex = () =>
    this.intersId.indexOf(Array.from(this.visibleRows).at(-1).id);

  getNextVisibleRowIndex = (id) =>
    this.intersId.indexOf(
      this.visibleRows.item(this.getVisibleRowIndex(id) + 1).id
    );

  getPrevVisibleRowIndex = (id) => {
    return this.getVisibleRowIndex(id)
      ? this.intersId.indexOf(
          this.visibleRows.item(this.getVisibleRowIndex(id) - 1).id
        )
      : 0;
  };

  static get observedAttributes() {
    return ['data', 'show'];
  }

  getConstants(key) {
    return this.constants[this.language][key];
  }

  connectedCallback() {
    this.addEventListener('ldod-modal-close', this.onCloseModal);
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangedAttribute[name](oldV, newV);
  }

  disconnectedCallback() {}

  undoButton = () => (
    <button
      id="virtual-undoManualSort"
      type="button"
      class="btn btn-primary"
      onClick={this.onUndo}
      disabled={!this.history.length}>
      {this.getConstants('undo')}
      <span class="icon undo-icon"></span>
    </button>
  );

  render() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(
      <ldod-modal id="virtual-veManual" dialog-class="modal-fullscreen">
        <span slot="header-slot">{this.edition?.title}</span>
        <div slot="body-slot">
          <ManualTable node={this} />
        </div>
        <div slot="footer-slot">
          <div style={{ display: 'flex', gap: '10px' }}>
            {this.undoButton()}
            <button type="button" class="btn btn-primary" onClick={this.onSave}>
              {this.getConstants('save')}
              <span class="icon save-icon"></span>
            </button>
          </div>
        </div>
      </ldod-modal>
    );
  }

  renderChanges() {
    this.toggleAttribute('data');
    const rows = this.table.allRows;
    rows.forEach((row) => this.setFirstCellEditable(row));
    rows.forEach((row) => this.setTableRowsDraggable(row));
  }

  renderUndoButton() {
    this.querySelector('button#virtual-undoManualSort').replaceWith(
      this.undoButton()
    );
  }

  renderTable() {
    this.querySelector('ldod-table#virtual-manualTable').replaceWith(
      <ManualTable node={this} />
    );
  }

  onUndo = () => {
    this.stepBack(this.history.pop());
  };

  onSave = async () => {
    const data = await saveReorderedVEInters(
      this.edition.externalId,
      Array.from(this.visibleRows).map(({ id }) => id)
    );
    this.onCloseModal({ detail: { id: 'virtual-veManual' } });
    this.updateData(data);
  };

  onCloseModal = ({ detail }) => {
    if (detail.id !== 'virtual-veManual') return;
    this.toggleAttribute('show', false);
    this.history = [];
    this.inters = null;
    this.initialInters = null;
    this.innerHTML = '';
  };
  updateData = (inters) => {
    if (inters) this.inters = inters;
    this.renderChanges();
  };

  onChangedAttribute = {
    data: () => {
      if (this.hasChildNodes()) {
        this.renderTable();
        this.renderUndoButton();
        return;
      }
      this.render();
      this.toggleAttribute('show', true);
    },
    show: () => {
      this.modal?.toggleAttribute('show', this.show);
    },
  };

  rowHide = ({ externalId }) => {
    let row = this.getRowById(externalId);
    row.toggleAttribute('hidden', true);
    this.updateHistory(
      externalId,
      null,
      null,
      row.hasAttribute('changed'),
      true
    );
    this.updateInters();
    this.updateIndexCell();
    this.renderUndoButton();
  };

  rowAdd = (inter) => {
    const interData = getInterData(inter);
    this.inters = [interData, ...this.inters];
    const rowData = addTableRow(this, interData, 0);

    const tableRow = this.querySelector(
      'ldod-table#virtual-manualTable'
    ).getRow(rowData);
    const newRow = this.tableBody.insertRow(0);
    tableRow.toggleAttribute('changed');
    tableRow.id = inter.externalId;
    newRow.replaceWith(tableRow);
    this.updateHistory(
      inter.externalId,
      0,
      null,
      tableRow.hasAttribute('changed'),
      false,
      true
    );
    this.setFirstCellEditable(tableRow);
    this.setTableRowsDraggable(tableRow);
    this.addRowDragEventListeners(tableRow);
    this.updateInters();
    this.updateIndexCell();
    this.renderUndoButton();
  };

  rowUpdate = ({ externalId, newIndex, oldIndex, changed }) => {
    let row = this.getRowById(externalId);
    this.updateHistory(
      externalId,
      oldIndex,
      newIndex,
      row.hasAttribute('changed')
    );
    row.toggleAttribute('changed', changed);
    this.tableBody.insertBefore(
      this.tableBody.removeChild(row),
      this.tableBody.querySelectorAll('tr')[newIndex]
    );
    this.removeRowDragEventListeners(row);
    this.addRowDragEventListeners(row);
    this.updateInters();
    this.updateIndexCell();
    this.renderUndoButton();
  };

  rollbackNewRow = (row) => {
    this.inters = this.inters.filter((inter) => inter.externalId !== row.id);
    row.remove();
    this.updateInters();
    this.updateIndexCell();
    this.renderUndoButton();
  };

  stepBack = (change) => {
    if (!change) return;
    const { externalId, newIndex, oldIndex, changed, hidden, newRow } = change;
    let row = this.getRowById(externalId);
    if (newRow) return this.rollbackNewRow(row);
    row.toggleAttribute('changed', changed);
    if (!row.hasAttribute('hidden')) {
      this.tableBody.insertBefore(
        this.tableBody.removeChild(row),
        this.tableBody.querySelectorAll('tr')[oldIndex]
      );
      this.removeRowDragEventListeners(row);
      this.addRowDragEventListeners(row);
    }
    hidden && row.toggleAttribute('hidden');
    this.updateInters();
    this.updateIndexCell();
    this.renderUndoButton();
  };

  updateHistory = (externalId, oldIndex, newIndex, changed, hidden, newRow) => {
    this.history.push({
      externalId,
      oldIndex,
      newIndex,
      changed,
      hidden,
      newRow,
    });
  };

  updateIndexCell = () => {
    this.visibleRows.forEach(
      (row, index) => (row.querySelectorAll('td')[0].textContent = index + 1)
    );
  };

  updateInters = () =>
    (this.inters = this.rows.map((row) =>
      this.inters.find(({ externalId }) => externalId === row.id)
    ));

  handleNewIndex = ({ target }) => {
    let oldIndex = Array.from(this.visibleRows).indexOf(target.parentNode);
    const newContent = target.textContent;
    if (this.isInvalidInput(newContent))
      return (target.textContent = oldIndex + 1);
    this.changePosition(target.parentNode.id, oldIndex, +newContent - 1);
  };

  isInvalidInput = (input) =>
    isNaN(+input) || +input < 1 || +input > this.inters.length;

  changePosition = (externalId, oldIndex, newIndex) => {
    //  newIndex = this.rows.indexOf(this.visibleRows.item(newIndex));
    if (oldIndex === newIndex) return;

    this.rowUpdate({
      externalId,
      newIndex,
      oldIndex,
      changed: true,
    });
  };

  // Drag feature

  setFirstCellEditable = (row) => {
    const cell = row.querySelector('td:nth-child(1)');
    cell.setAttribute('contenteditable', 'true');
    cell.onblur = this.handleNewIndex;
  };

  setTableRowsDraggable = (row) => {
    row.setAttribute('draggable', 'true');
    this.addRowDragEventListeners(row);
  };

  onDragOver = (e) => {
    e.preventDefault();
    e.target.parentNode.toggleAttribute('dragging', true);
  };

  onDragLeave = (e) => {
    e.preventDefault();
    e.target.parentNode.toggleAttribute('dragging', false);
  };

  onDragStart = (e) => {
    e.dataTransfer.setData(
      'target',
      JSON.stringify({
        index: this.intersId.indexOf(e.target.id),
        externalId: e.target.id,
      })
    );
  };

  onDrop = (e) => {
    e.target.parentNode.toggleAttribute('dragging', false);
    const { index, externalId } = JSON.parse(e.dataTransfer.getData('target'));
    const newIndex = this.intersId.indexOf(e.target.parentNode.id);
    this.changePosition(externalId, index, newIndex);
  };

  addRowDragEventListeners = (row) => {
    row.addEventListener('dragover', this.onDragOver);
    row.addEventListener('dragenter', this.onDragOver);
    row.addEventListener('dragleave', this.onDragLeave);
    row.addEventListener('dragstart', this.onDragStart);
    row.addEventListener('drop', this.onDrop);
  };
  removeRowDragEventListeners = (row) => {
    row.removeEventListener('dragover', this.onDragOver);
    row.removeEventListener('dragenter', this.onDragOver);
    row.removeEventListener('dragleave', this.onDragLeave);
    row.removeEventListener('dragstart', this.onDragStart);
    row.removeEventListener('drop', this.onDrop);
  };

  //Add fragments
  onAddFragments = (inters) => {
    const duplicatedFrags = getDuplicatedFrags(this, inters);
    const fragsToAdd = getFragsToAdd(this, inters);
    if (duplicatedFrags.length) notifyForDuplicatedFrags(this, duplicatedFrags);
    fragsToAdd.forEach((frag) => this.rowAdd(frag));
  };
}
!customElements.get('ldod-ve-manual') &&
  customElements.define('ldod-ve-manual', LdodVeManual);

const notifyForDuplicatedFrags = (node, duplicatedFrags) => {
  node.dispatchEvent(
    new CustomEvent('ldod-error', {
      detail: {
        message: `The following Fragments are duplicated: ${duplicatedFrags.join(
          ', '
        )}`,
      },
      bubbles: true,
      composed: true,
    })
  );
};

const getDuplicatedFrags = (node, inters) => {
  return [
    ...new Set(
      inters
        .filter(
          (inter) =>
            node.visibleFragments.includes(inter.xmlId) ||
            inters.filter(({ xmlId }) => xmlId === inter.xmlId).length > 1
        )
        .map((inter) => `<br />${inter.xmlId} - ${inter.title}`)
    ),
  ];
};

const getFragsToAdd = (node, inters) => {
  return [
    ...new Set(
      inters.filter(
        (inter) =>
          !node.visibleFragments.includes(inter.xmlId) &&
          inters.filter(({ xmlId }) => xmlId === inter.xmlId).length === 1
      )
    ),
  ];
};

const getInterData = (inter) => ({
  ...inter,
  number: '',
  usedList: [
    {
      externalId: inter.externalId,
      xmlId: inter.xmlId,
      urlId: inter.urlId,
      shortName: inter.shortName,
      title: inter.interTitle,
      number: '',
    },
  ],
});
