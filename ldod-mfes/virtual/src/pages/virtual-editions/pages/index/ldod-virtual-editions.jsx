/** @format */

import { getVirtualEditions } from '@src/api-requests';
import {
	createVirtualEdition,
	getEditionGames,
	getVeIntersForManual,
	getVeIntersWithRecommendation,
	getVirtualEdition,
	veAdminDelete,
} from '@src/restricted-api-requests';
import constants from '../../../constants';
import CreateButton from './components/create-ve/create-button';
import VETable from './components/ve-table';
import Title from './components/title';
import { errorPublisher, selectedVEs } from '../../../../event-module';
import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

let gamesModal;
let taxonomy;

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
				<ldod-bs-modal id="virtual-ve-create-modal" dialog-class="modal-lg">
					<h4 data-virtual-key="modalTitle" slot="header-slot">
						{this.getConstants('modalTitle')}
					</h4>
					<div slot="body-slot">
						<create-ve-form node={this}></create-ve-form>
					</div>
				</ldod-bs-modal>
			</>
		);
	}

	sortVirtualEditions = () => {
		this.virtualEditions
			.sort((ed1, ed2) => ed1.acronym.localeCompare(ed2.acronym))
			.sort((ed1, ed2) => +ed2.member?.pending - +ed1.member?.pending)
			.sort((ed1, ed2) => +ed2.member?.active - +ed1.member?.active)
			.sort(
				(ed1, ed2) =>
					+(ed2.selected || selectedVEs.includes(ed2.acronym)) -
					(+ed1.selected || selectedVEs.includes(ed1.acronym))
			);
		this.render();
		this.addEventListeners();
	};

	updateEdition = edition => {
		const index = this.virtualEditions.map(ve => ve.externalId).indexOf(edition.externalId);
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
		language: (oldV, newV) => oldV && oldV !== newV && this.onChangedLanguage(oldV, newV),
	};

	onChangedLanguage = () => {
		this.querySelectorAll('[language]').forEach(ele =>
			ele.setAttribute('language', this.language)
		);

		this.querySelectorAll('[data-virtual-key]').forEach(node => {
			node.firstChild.textContent = this.getConstants(node.dataset.virtualKey);
		});

		// replace all by the above
		this.querySelectorAll('[data-virtualkey]').forEach(node => {
			node.firstChild.textContent = this.getConstants(node.dataset.virtualkey);
		});

		this.querySelectorAll('[data-virtual-tooltip-key]').forEach(tooltip => {
			console.log(tooltip.dataset);
			tooltip.setAttribute('content', this.getConstants(tooltip.dataset.virtualTooltipKey));
		});

		// replace all by the above
		this.querySelectorAll('[data-virtualtooltipkey]').forEach(tooltip =>
			tooltip.setAttribute('content', this.getConstants(tooltip.dataset.virtualtooltipkey))
		);
	};

	addEventListeners = () => {
		this.wrapper.addEventListener('pointerenter', onWrapperEnter, { once: true });
	};

	//actions

	onCreateVE = e => {
		e.preventDefault();
		const veDto = Object.fromEntries(new FormData(e.target));
		createVirtualEdition(veDto)
			.then(({ virtualEditions, user }) => {
				this.virtualEditions = virtualEditions;
				this.user = user;
				this.sortVirtualEditions();
			})
			.catch(error => onError(error?.message || ''));
	};

	onGamesModal = async () => {
		if (!gamesModal) gamesModal = await import('./components/games/ldod-ve-games');
		const ldodVeGames = this.querySelector('ldod-ve-games');
		ldodVeGames.edition = this.edition;
		getEditionGames(this.edition.externalId)
			.then(data => {
				ldodVeGames.parent = this;
				ldodVeGames.updateData(data);
				ldodVeGames.show();
			})
			.catch(onError);
	};

	onTaxonomy = async () => {
		if (!taxonomy) taxonomy = await import('./components/taxonomy/ldod-ve-taxonomy');
		const ldodVeTaxonomy = this.querySelector('ldod-ve-taxonomy');
		ldodVeTaxonomy.parent = this;
		ldodVeTaxonomy.toggleAttribute('show');
	};

	onManualModal = async () => {
		await import('./components/manual/ldod-ve-manual');
		const ldodVeManual = this.querySelector('ldod-ve-manual');
		ldodVeManual.edition = this.edition;
		getVeIntersForManual(this.edition.externalId)
			.then(data => {
				ldodVeManual.parent = this;
				ldodVeManual.inters = data;
				ldodVeManual.toggleAttribute('show', true);
			})
			.catch(onError);
	};

	onAssistModal = async () => {
		await import('./components/assisted/ldod-ve-assisted');
		const ldodVeAssisted = this.querySelector('ldod-ve-assisted');
		ldodVeAssisted.edition = this.edition;
		getVeIntersWithRecommendation(this.edition.externalId)
			.then(data => {
				ldodVeAssisted.parent = this;
				ldodVeAssisted.updateData(data);
				ldodVeAssisted.toggleAttribute('show', true);
			})
			.catch(onError);
	};

	onEditorsModal = async () => {
		await import('./components/editors/ldod-ve-editors');
		const element = this.querySelector('ldod-ve-editors');
		getVirtualEdition(this.edition.externalId)
			.then(data => {
				element.edition = data;
				element.parent = this;
				element.toggleAttribute('show');
			})
			.catch(onError);
	};

	onEditVe = async () => {
		await import('./components/edit-ve/ldod-ve-edit.jsx');
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
			.catch(onError);
	};
}
!customElements.get('ldod-virtual-editions') &&
	customElements.define('ldod-virtual-editions', LdodVirtualEditions);

function onWrapperEnter() {
	import('@ui/modal-bs.js');
	import('@ui/tooltip.js');
	import('./components/create-ve/create-ve-form/create-ve-form.js');
}

function onError(error) {
	console.error(error);
	errorPublisher(error.message);
}
