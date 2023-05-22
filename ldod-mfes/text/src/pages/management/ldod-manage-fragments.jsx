/** @format */

import FragsTable from './components/fragments-table.jsx';
import Title from './components/title.jsx';
import constants from './constants.js';
import { ldodEventBus } from '@core';
import { removeFragmentById, removeAllFragments, dataProxy } from '@src/api-requests';

import UploadButtons from './components/upload-buttons.jsx';
import ExportButtons from './components/export-buttons.jsx';

import('@ui/buttons.js').then(({ ldodButton }) => ldodButton());

async function loadToolip() {
	await import('@ui/tooltip.js');
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
		return ['language'];
	}

	getSelectedFrags() {
		return JSON.stringify(this.selectedFrags);
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	async connectedCallback() {
		this.fragments = await dataProxy.adminFragments;
		this.appendChild(<div id="manageFragmentsWrapper"></div>);
		this.render();
	}

	render() {
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
						data-button-key="removeAll"
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
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handeChangedAttribute[name](oldV, newV);
	}

	handeChangedAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) {
				this.querySelectorAll('[data-key]').forEach(node => {
					return (node.firstChild.textContent = node.dataset.args
						? this.getConstants(node.dataset.key, JSON.parse(node.dataset.args))
						: this.getConstants(node.dataset.key));
				});
				this.querySelectorAll('[data-tooltipkey]').forEach(ele => {
					ele.setAttribute('content', this.getConstants(ele.dataset.tooltipkey));
				});
				this.querySelectorAll('[data-button-key]').forEach(btn => {
					btn.setAttribute('title', this.getConstants(btn.dataset.buttonKey));
				});
			}
		},
	};

	addEventListeners() {
		this.wrapper.addEventListener('pointerenter', loadToolip);
		this.exportHeadElement?.addEventListener('click', this.unselectAll);
		this.addEventListener('ldod:file-uploaded', this.handleFileUploaded);
		this.addEventListener('ldod-table-searched', this.updateTitle);
	}

	updateTitle = ({ detail }) => {
		this.querySelector('h3#title').firstChild.textContent = this.getConstants(
			'manageFragments',
			detail.size
		);
	};

	unselectAll = () => {
		this.selectedRows.forEach(tr => tr.toggleAttribute('selected'));
		this.exportSelectedElement.body = [];
	};

	handleFileUploaded = ({ detail: { payload } }) => {
		if (!payload) return;
		if ('ok' in payload) return this.handleMessageOnUploadedFile(payload.ok, payload.message);
		dataProxy.reset = true;
		const nl = document.createElement('br').outerHTML;
		const p = document.createElement('p').outerHTML;

		payload.forEach(fragment => {
			const { xmlId, uploaded, overwritten } = fragment;
			if (uploaded) {
				if (overwritten) this.removeFragmentByXmlId(xmlId);
				this.mutateFragments(this.addFragment(fragment));
			}
		});

		const uploadedFrags = payload.filter(frag => frag.uploaded);
		const notUploadedFrags = payload.filter(frag => !frag.uploaded);

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
		ldodEventBus.publish(
			'ldod:message',
			uploadedFragsResult.concat(`${p}`, notUploadedFragsResult)
		);
	};

	handleMessageOnUploadedFile = (ok, message) => {
		ldodEventBus.publish(ok ? 'ldod:message' : 'ldod:error', message);
	};

	handleRemoveAll = async () => {
		if (!confirm('Are you sure you want to remove all fragments?')) return;
		const res = await removeAllFragments();
		if (res.ok) this.mutateFragments([]);
		const type = res.ok ? 'ldod:message' : 'ldod:error';
		ldodEventBus.publish(type, res.message);
	};

	handleRemoveFragment = async ({ target }) => {
		const id = target.dataset.id;
		const res = await removeFragmentById(target.dataset.id);
		if (res.ok) return this.mutateFragments(this.removeFragmentById(id));
		ldodEventBus.publish('ldod:error', res.message);
	};

	removeFragmentById(id) {
		return this.fragments.filter(frag => frag.externalId !== id);
	}

	removeFragmentByXmlId(xmlId) {
		this.fragments = this.fragments.filter(frag => frag.xmlId !== xmlId);
	}

	addFragment(fragment) {
		this.fragments.reverse().push(fragment);
		return this.fragments.reverse();
	}

	mutateFragments(newFragments) {
		this.fragments = newFragments;
		this.render();
	}
}
!customElements.get('ldod-manage-fragments') &&
	customElements.define('ldod-manage-fragments', LdodManageFragments);
