/** @format */

import { errorPublisher } from '../event-module';
import { computeSelectPureHeight } from '../utils';
import {
	associateTagsRequest,
	dissociateTagRequest,
	getVirtualFragmentInter,
	getVirtualFragmentInters,
} from './api-requests';
import style from './style.css?inline';
import constants from './constants';

const annotatorService = new (await customElements.whenDefined('annotator-service'))();
export class VirtualTranscription extends HTMLElement {
	constructor() {
		super();
		this.taxonomy = undefined;
		this.inters = [];
		this.constants = constants;
	}

	get xmlId() {
		return this.getAttribute('xmlid');
	}

	get urlId() {
		return this.getAttribute('urlId');
	}

	get singleInter() {
		return this.inters[0];
	}

	get language() {
		return this.getAttribute('language');
	}

	get associateTagModal() {
		return document.body.querySelector('ldod-bs-modal#virtual--associate-tag-modal');
	}

	get associateTagModalSelect() {
		return this.associateTagModal.querySelector('select-pure#virtual-associateTag');
	}

	static get observedAttributes() {
		return ['language'];
	}
	getConstants(key) {
		return this.constants[this.language][key];
	}

	getIntersChecked = () => {
		return customElements.get('virtual-frag-nav').instance.intersChecked;
	};

	fetchVirtualFragmentInter = (xmlId, urlId) =>
		getVirtualFragmentInter(xmlId, urlId)
			.then(data => {
				this.inters = [data.inter];
				this.taxonomy = data.taxonomy;
				this.annotations = data.annotations;
			})
			.catch(this.onError);

	fetchVirtualFragmentInters = (xmlId, intersId) =>
		getVirtualFragmentInters(xmlId, intersId)
			.then(data => (this.inters = data))
			.catch(this.onError);

	fetchData = async () => {
		if (!this.xmlId) return;
		if (!this.urlId && !this.getIntersChecked().length) return;
		if (this.urlId || this.getIntersChecked().length === 1)
			return this.fetchVirtualFragmentInter(
				this.xmlId,
				this.urlId || this.getIntersChecked()[0].urlId
			);

		return this.fetchVirtualFragmentInters(
			this.xmlId,
			this.getIntersChecked().map(input => input.externalId)
		);
	};

	async connectedCallback() {
		await this.fetchData();
		this.innerHTML = /*html*/ `<style>${style}</style><div id="virtual-transcriptionWrapper"></div>`;
		this.wrapper = this.querySelector('div#virtual-transcriptionWrapper');
		this.render();
		this.addEventListeners();
	}

	async render() {
		this.wrapper.innerHTML = '';
		if (this.inters.length > 1)
			return this.wrapper.appendChild(await getIntersComparison(this));
		if (this.singleInter) {
			this.wrapper.appendChild(
				await getInterTranscription(this, this.singleInter, this.taxonomy)
			);
			if (!annotatorService) return;
			annotatorService.annotate({
				id: this.singleInter.externalId,
				selector: 'div#virtual-nodeReference',
			});
		}
	}

	addEventListeners() {
		this.addEventListener('virtual:associate-tag', this.associateTag);
		this.addEventListener('annotator:annotation-update', async () => {
			await this.fetchData();
			this.render();
		});
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
		this.querySelectorAll('[language]').forEach(node =>
			node.setAttribute('language', this.language)
		);
		this.querySelectorAll('[data-virtual-key]').forEach(node => {
			node.firstChild.textContent = this.getConstants(node.dataset.virtualKey);
		});
	};

	onError = error => {
		console.error(error);
		errorPublisher(error?.message);
	};

	associateTag = async () => {
		document.body.appendChild(await getAssociateModal(this, this.singleInter));
		//document.body.addEventListener('click', this.computeSelectHeight);
		document.body.addEventListener('ldod-modal-close', this.removeModal);
		this.associateTagModal.toggleAttribute('show', true);
	};

	handleAssociateTag = () => this.associateTag();

	computeSelectHeight = () => {
		computeSelectPureHeight(this.associateTagModalSelect, 80);
	};

	dissociateTag = async ({ target }) => {
		await dissociateTagRequest(target.dataset.interId, target.dataset.catId)
			.then(data => {
				this.inters = [data.inter];
				this.taxonomy = data.taxonomy;
			})
			.catch(this.onError);
		this.render();
	};

	onAssociateTags = async () => {
		const body = [...new Set(this.associateTagModal?.querySelector('select-pure').values)];
		await associateTagsRequest(this.singleInter.externalId, body)
			.then(data => {
				this.inters = [data.inter];
				this.taxonomy = data.taxonomy;
				this.removeModal();
				this.render();
			})
			.catch(this.onError);
	};

	removeModal = (e = {}) => {
		if (e.detail && e.detail.id !== 'virtual--associate-tag-modal') return;
		//document.body.removeEventListener('click', this.computeSelectHeight);
		document.body.removeEventListener('ldod-modal-close', this.removeModal);
		this.associateTagModal?.remove();
	};
}
!customElements.get('virtual-transcription') &&
	customElements.define('virtual-transcription', VirtualTranscription);

async function getAssociateModal(root, inter) {
	return (await import('./components/associate-tag-modal/associate-tag-modal')).default({
		root,
		inter,
	});
}

async function getInterTranscription(root, inter, taxonomy) {
	return (await import('./components/transcription')).default({ root, inter, taxonomy });
}

async function getIntersComparison(root) {
	return (await import('./components/virtual-inters-compare')).default({ root });
}

export async function loadAnnotator(interId, referenceNode) {
	if (!window.mfes.includes('annotations')) return;
	if (!annotatorService) {
		annotatorService = import.meta.env.DEV
			? (await import('annotations.dev').catch(e => console.error(e))).annotatorService
			: (await import('annotations').catch(e => console.error(e))).annotatorService;
	}
	annotatorService({ interId, referenceNode });
}
