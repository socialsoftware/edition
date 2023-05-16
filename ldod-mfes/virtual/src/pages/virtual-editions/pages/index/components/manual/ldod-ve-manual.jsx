/** @format */

import constants from '@src/pages/constants';
import { saveReorderedVEInters } from '@src/restricted-api-requests';
import { errorPublisher } from '../../../../../../event-module';
import assistedContants from '../assisted/constants';
import AddFragmentsModal from './add-fragments';
import thisConstants from './constants';
import ManualTable, { addTableRow } from './manual-table';
import manualStyle from './manual.css?inline';
import style from '../style.css?inline';
import formsCss from '@ui/bootstrap/forms-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';

if (typeof window !== 'undefined') {
	import('search')
		.then(({ loadSearchSimple }) => loadSearchSimple())
		.catch(console.error);
}

const sheet = new CSSStyleSheet();
sheet.replaceSync(style + manualStyle + formsCss + buttonsCss);

export class LdodVeManual extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.history = [];
		this.constants = Object.entries(thisConstants).reduce((prev, [key, value]) => {
			prev[key] =
				value instanceof Array
					? value
					: { ...constants[key], ...assistedContants[key], ...value };
			return prev;
		}, {});
	}

	get language() {
		return this.getAttribute('language');
	}

	get show() {
		return this.hasAttribute('show');
	}

	get modal() {
		return this.shadowRoot.querySelector('#virtual-ve-manual');
	}

	get addFragsModal() {
		return this.shadowRoot.querySelector('#virtual-add-fragments-modal');
	}

	get table() {
		return this.shadowRoot.querySelector('ldod-table#virtual-manualTable');
	}

	get intersId() {
		return this.inters.map(({ externalId }) => externalId);
	}

	get tableBody() {
		return this.shadowRoot.querySelector('tbody');
	}

	get rows() {
		return Array.from(this.tableBody.querySelectorAll('tr'));
	}

	get visibleRows() {
		return this.tableBody.querySelectorAll('tr:not([hidden])');
	}

	get visibleFragments() {
		return Array.from(this.visibleRows).map(
			row => this.inters.find(inter => inter.externalId === row.id)?.xmlId || []
		);
	}

	getRowById = id => this.tableBody.querySelector(`tr[id="${id}"]`);

	getVisibleRowIndex = id =>
		Array.from(this.visibleRows)
			.map(row => row.id)
			.indexOf(id);

	getFirstVisibleRowIndex = () => this.intersId.indexOf(this.visibleRows.item(0).id);

	getLastVisibleRowIndex = () => this.intersId.indexOf(Array.from(this.visibleRows).at(-1).id);

	getNextVisibleRowIndex = id =>
		this.intersId.indexOf(this.visibleRows.item(this.getVisibleRowIndex(id) + 1).id);

	getPrevVisibleRowIndex = id => {
		return this.getVisibleRowIndex(id)
			? this.intersId.indexOf(this.visibleRows.item(this.getVisibleRowIndex(id) - 1).id)
			: 0;
	};

	getConstants(key) {
		return this.constants[this.language][key];
	}

	// open/close behavior
	addModalCloseEventListener() {
		this.addEventListener('ldod-modal-close', this.onCloseModal);
	}

	removeModalCloseEventListener() {
		this.removeEventListener('ldod-modal-close', this.onCloseModal);
	}

	connectedCallback() {
		this.wrapper = this.shadowRoot.appendChild(<div id="'virtual-manual-ve-wrapper"></div>);
		this.addModalCloseEventListener();
	}

	onCloseModal = ({ detail }) => {
		this.closeModal[detail.id.replace('virtual-', '')]();
	};

	closeModal = {
		've-manual': () => {
			this.toggleAttribute('show', false);
		},
		'add-fragments-modal': () => {
			this.addFragsModal.toggleAttribute('show', false);
			this.modal.toggleAttribute('show', true);
		},
	};

	static get observedAttributes() {
		return ['show'];
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangedAttribute[name](oldV, newV);
	}

	onChangedAttribute = {
		show: () => {
			if (this.hasAttribute('show')) {
				this.render();
				this.modal.toggleAttribute('show', true);
			} else this.resetComponentState();
		},
	};

	resetComponentState = () => {
		this.history = [];
		this.inters = null;
	};

	render() {
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<ldod-bs-modal
					id="virtual-ve-manual"
					dialog-class="modal-xl modal-dialog-scrollable">
					<h5 slot="header-slot">
						<span>{this.edition?.title} - </span>
						<span>{this.getConstants('manualSort')}</span>
					</h5>
					<div slot="body-slot">
						<div id="virtual-manual-table-wrapper"></div>
					</div>
					<div slot="footer-slot">
						<div style={{ display: 'flex', gap: '10px' }}>
							<div id="virtual-undoManualSort"></div>
							<button type="button" class="btn btn-primary" onClick={this.onSave}>
								{this.getConstants('save')}
								<span class="icon save-icon"></span>
							</button>
						</div>
					</div>
				</ldod-bs-modal>
				<AddFragmentsModal node={this} />
			</>
		);
		this.renderParcial();
	}

	renderParcial = () => {
		this.renderTable();
		this.renderUndoButton();
	};

	renderTable = () => {
		this.shadowRoot
			.querySelector('#virtual-manual-table-wrapper')
			.replaceWith(<ManualTable node={this} />);
		this.hydrateRows();
	};

	hydrateRows() {
		const rows = this.table.allRows;
		rows.forEach(row => this.setFirstCellEditable(row));
		rows.forEach(row => this.setTableRowsDraggable(row));
	}

	renderUndoButton() {
		this.shadowRoot.querySelector('#virtual-undoManualSort').replaceWith(this.undoButton());
	}

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

	onUndo = () => {
		this.stepBack(this.history.pop());
	};

	onSave = async () => {
		const inters = await saveReorderedVEInters(
			this.edition.externalId,
			Array.from(this.visibleRows).map(({ id }) => id)
		);
		this.resetComponentState();
		this.inters = inters;
		this.renderParcial();
	};

	rowHide = ({ externalId }) => {
		let row = this.getRowById(externalId);
		row.toggleAttribute('hidden', true);
		this.updateHistory(externalId, null, null, row.hasAttribute('changed'), true);
		this.updateInters();
		this.updateIndexCell();
		this.renderUndoButton();
	};

	rowAdd = inter => {
		const interData = getInterData(inter);
		this.inters = [interData, ...this.inters];
		const rowData = addTableRow(this, interData, 0);
		const tableRow = this.table.getRow(rowData, this.table);
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
		this.updateHistory(externalId, oldIndex, newIndex, row.hasAttribute('changed'));
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

	rollbackNewRow = row => {
		this.inters = this.inters.filter(inter => inter.externalId !== row.id);
		row.remove();
		this.updateInters();
		this.updateIndexCell();
		this.renderUndoButton();
	};

	stepBack = change => {
		if (!change) return;
		const { externalId, oldIndex, changed, hidden, newRow } = change;
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
		(this.inters = this.rows.map(row =>
			this.inters.find(({ externalId }) => externalId === row.id)
		));

	handleNewIndex = ({ target }) => {
		let oldIndex = Array.from(this.visibleRows).indexOf(target.parentNode);
		const newContent = target.textContent;
		if (this.isInvalidInput(newContent)) return (target.textContent = oldIndex + 1);
		this.changePosition(target.parentNode.id, oldIndex, +newContent - 1);
	};

	isInvalidInput = input => isNaN(+input) || +input < 1 || +input > this.inters.length;

	changePosition = (externalId, oldIndex, newIndex) => {
		if (oldIndex === newIndex) return;
		this.rowUpdate({
			externalId,
			newIndex,
			oldIndex,
			changed: true,
		});
	};

	// Drag feature

	setFirstCellEditable = row => {
		const cell = row.querySelector('td:nth-child(1)');
		cell.setAttribute('contenteditable', 'true');
		cell.onblur = this.handleNewIndex;
	};

	setTableRowsDraggable = row => {
		row.setAttribute('draggable', 'true');
		this.addRowDragEventListeners(row);
	};

	onDragOver = e => {
		e.preventDefault();
		e.target.parentNode.toggleAttribute('dragging', true);
	};

	onDragLeave = e => {
		e.preventDefault();
		e.target.parentNode.toggleAttribute('dragging', false);
	};

	onDragStart = e => {
		e.dataTransfer.setData(
			'target',
			JSON.stringify({
				index: this.intersId.indexOf(e.target.id),
				externalId: e.target.id,
			})
		);
	};

	onDrop = e => {
		e.target.parentNode.toggleAttribute('dragging', false);
		const { index, externalId } = JSON.parse(e.dataTransfer.getData('target'));
		const newIndex = this.intersId.indexOf(e.target.parentNode.id);
		this.changePosition(externalId, index, newIndex);
	};

	addRowDragEventListeners = row => {
		row.addEventListener('dragover', this.onDragOver);
		row.addEventListener('dragenter', this.onDragOver);
		row.addEventListener('dragleave', this.onDragLeave);
		row.addEventListener('dragstart', this.onDragStart);
		row.addEventListener('drop', this.onDrop);
	};
	removeRowDragEventListeners = row => {
		row.removeEventListener('dragover', this.onDragOver);
		row.removeEventListener('dragenter', this.onDragOver);
		row.removeEventListener('dragleave', this.onDragLeave);
		row.removeEventListener('dragstart', this.onDragStart);
		row.removeEventListener('drop', this.onDrop);
	};

	//Add fragments
	onAddFragments = inters => {
		console.log(inters);
		const duplicatedFrags = getDuplicatedFrags(this, inters);
		const fragsToAdd = getFragsToAdd(this, inters);
		if (duplicatedFrags.length) notifyForDuplicatedFrags(duplicatedFrags);
		fragsToAdd.forEach(frag => this.rowAdd(frag));
	};

	onOpenAddFragsModal = () => {
		this.removeModalCloseEventListener();
		this.modal.toggleAttribute('show', false);
		this.addModalCloseEventListener();
		this.addFragsModal.toggleAttribute('show', true);
	};
}
!customElements.get('ldod-ve-manual') && customElements.define('ldod-ve-manual', LdodVeManual);

const notifyForDuplicatedFrags = duplicatedFrags => {
	const message = `The following Fragments are duplicated: ${duplicatedFrags.join(', ')}`;
	errorPublisher(message);
};

const getDuplicatedFrags = (node, inters) => {
	return [
		...new Set(
			inters
				.filter(
					inter =>
						node.visibleFragments.includes(inter.xmlId) ||
						inters.filter(({ xmlId }) => xmlId === inter.xmlId).length > 1
				)
				.map(inter => `\n${inter.xmlId} - ${inter.title}`)
		),
	];
};

const getFragsToAdd = (node, inters) => {
	return [
		...new Set(
			inters.filter(
				inter =>
					!node.visibleFragments.includes(inter.xmlId) &&
					inters.filter(({ xmlId }) => xmlId === inter.xmlId).length === 1
			)
		),
	];
};

const getInterData = inter => ({
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
