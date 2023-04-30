/** @format */

import { getFragmentInters, updateFragmentInter } from '../../api-requests';
import constants from './constants';
import fragmentsConstants from '../fragments/constants';
import style from './style.css?inline';
import {
	checkBoxes,
	isLineByLine,
	isSideBySide,
	isSingleAndEditorial,
	isSingleAndSourceInter,
	isVirtualInter,
} from './utils';
import fragNav from './components/frag-nav/frag-nav-container';
import virtualFragNav from './components/virtual/virtual-frag-nav';
import virtualTranscription from './components/virtual/virtual-transcription';
const EditorialInter = async ({ node, inter }) =>
	(await import('./components/editorial-inter')).default({ node, inter });

const SourceInter = async ({ node, inter }) =>
	(await import('./components/source-inter')).default({ node, inter });

const SideBySideTranscriptions = async ({ node, inters }) =>
	(await import('./components/side-by-side-transcriptions')).default({
		node,
		inters,
	});

const LineByLineTranscriptions = async ({ node, inters }) =>
	(await import('./components/line-by-line-transcriptions')).default({
		node,
		inters,
	});

const loadTooltip = () => import('@shared/tooltip.js');

export class LdodFragment extends HTMLElement {
	constructor(lang, data, xmlId, urlId) {
		super();
		this.language = lang;
		this.xmlId = xmlId;
		this.updateFragmentState(data, urlId, data.inters);
		this.editorialInters = Object.values(data.expertsInterMap);
		this.transcriptionCheckboxes = checkBoxes;
		LdodFragment.instance = this;
	}

	updateFragmentState = (data, urlId, inters) => {
		this.data = data;
		this.urlId = urlId;
		this.inters = new Set(
			inters?.length ? [...inters.map(({ externalId }) => externalId)] : []
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

	get transcriptionContainer() {
		return this.querySelector('div#fragment-transcription-container');
	}

	get navigationContainer() {
		return this.querySelector('div#fragment-navigation-container');
	}

	hasInters() {
		return this.urlId || this.intersSize();
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key] ?? fragmentsConstants[this.language][key];
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

	async connectedCallback() {
		this.innerHTML = /*html*/ `
		<style>${style}</style>
		<div id="fragmentWrapper"></div>
		`;
		await this.render();
		this.addEventListeners();
	}

	async render() {
		this.wrapper.innerHTML = /*html*/ `
			<div id="fragInterContainer">
				<div id="fragment-transcription-container"></div>
				<div id="fragment-navigation-container">
			</div>
		`;
		this.renderNavigationPanels();
		this.renderVirtualFragNav();
		await this.renderFragmentTranscription();
	}

	renderNavigationPanels() {
		this.navigationContainer.querySelectorAll('frag-nav-panel').forEach(ele => ele.remove());
		this.navigationContainer.insertAdjacentHTML('afterbegin', fragNav(this));
	}

	renderVirtualFragNav = () =>
		this.navigationContainer.insertAdjacentHTML(
			'beforeend',
			virtualFragNav({ language: this.language, xmlId: this.xmlId, urlId: this.urlId })
		);

	appendRawHTML = {
		false: (node, element) => node.appendChild(element),
		true: (node, string) => node.insertAdjacentHTML('afterbegin', string),
	};

	renderFragmentTranscription = async () => {
		this.transcriptionContainer.innerHTML = '';
		const element = await this.getFragmentTranscription();
		this.appendRawHTML[typeof element === 'string'](this.transcriptionContainer, element);
	};

	getVirtualTranscription = () =>
		virtualTranscription({ language: this.language, xmlId: this.xmlId, urlId: this.urlId });

	getFragmentTranscription = async () => {
		if (!this.hasInters()) {
			if (this.virtualInters) return this.getVirtualTranscription();
			return /*html*/ `<h4 class="text-center">${this.data.title}</h4>`;
		}

		if (isVirtualInter(this.urlId)) return this.getVirtualTranscription();

		if (isSingleAndSourceInter(this.data.inters ?? []))
			return await SourceInter({ node: this, inter: this.data.inters[0] });

		if (isSingleAndEditorial(this.data.inters ?? []))
			return await EditorialInter({ node: this, inter: this.data.inters[0] });

		if (isSideBySide(this.data))
			return await SideBySideTranscriptions({
				node: this,
				inters: this.data.inters,
			});

		if (isLineByLine(this.data))
			return await LineByLineTranscriptions({
				node: this,
				inters: this.data.inters,
			});
	};

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

	handleChangedLanguage = () => {
		this.renderNavigationPanels();
		this.renderVirtualFragNav();
		this.querySelectorAll('[language]').forEach(ele =>
			ele.setAttribute('language', this.language)
		);
		this.querySelectorAll('[data-key]').forEach(
			node => (node.firstChild.textContent = this.getConstants(node.dataset.key))
		);
		this.querySelectorAll('[data-tooltipkey]').forEach(node =>
			node.setAttribute('content', this.getConstants(node.dataset.tooltipkey))
		);
	};

	intersSize = () => this.inters?.size;

	addRemoveInter(externalId) {
		return this.inters.has(externalId)
			? this.inters.delete(externalId)
			: this.inters.add(externalId);
	}

	clearInters() {
		this.inters.clear();
		this.renderNavigationPanels();
	}

	addEventListeners = () => {
		this.wrapper
			.querySelectorAll('[data-tooltipkey]')
			.forEach(ele =>
				ele.parentNode.addEventListener('pointerenter', loadTooltip, { once: true })
			);
		['witnesses', 'experts'].forEach(id => {
			this.addEventListener(`${id}:changed`, ({ detail }) => {
				this.handleInterCheckboxChange(detail.id);
			});
		});
		this.addEventListener('virtual:inter-selected', this.handleVirtualIntersSelection);
	};

	handleVirtualIntersSelection = async ({ detail }) => {
		this.clearInters();
		this.urlId = '';
		this.virtualInters = detail.virtualInters;
		await this.renderFragmentTranscription();
	};

	updateTranscriptCheckboxesValue = def => {
		if (def) {
			this.transcriptionCheckboxes = checkBoxes;
			return;
		}
		this.querySelectorAll("div#text-checkBoxesContainer input[type='checkbox']").forEach(
			input => (this.transcriptionCheckboxes[input.name] = input.checked)
		);
	};
}
!customElements.get('ldod-fragment') && customElements.define('ldod-fragment', LdodFragment);
