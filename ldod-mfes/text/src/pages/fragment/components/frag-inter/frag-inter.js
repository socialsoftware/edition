/** @format */

import { parseRawHTML } from '../../../../utils';
import {
	checkBoxes,
	isLineByLine,
	isSideBySide,
	isSingleAndEditorial,
	isSingleAndSourceInter,
	isVirtualInter,
} from '../../utils';
import constants from '../../constants';
import fragmentsConstants from '../../../fragments/constants';
import virtualTranscription from '../virtual/virtual-transcription';
import style from './frag-inter.css?inline';
import { getFragmentInters, updateFragmentInter } from '../../../../api-requests';

class FragInter extends HTMLElement {
	constructor() {
		super();
		this.transcriptionCheckboxes = checkBoxes;
		FragInter.instance = this;
	}

	get language() {
		return this.getAttribute('language');
	}

	get hasVirtualInters() {
		return this.dataset.virtualInters;
	}
	get hasTextInters() {
		return this.dataset.textInters;
	}
	get hasNoInters() {
		return !this.hasTextInters && !this.hasVirtualInters;
	}

	get type() {
		const { urlId, textInters, virtualInters } = this.dataset;
		if (!urlId) return virtualInters ? 'virtual' : 'text';
		if (isVirtualInter(urlId) && (this.hasNoInters || virtualInters)) return 'virtual';
		return this.hasNoInters || textInters ? 'text' : 'virtual';
	}
	static get observedAttributes() {
		return ['language'];
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
		this.querySelectorAll('[language]').forEach(ele =>
			ele.setAttribute('language', this.language)
		);
		this.querySelectorAll('[data-key]').forEach(
			node => (node.firstChild.textContent = this.getConstants(node.dataset.key))
		);
		this.querySelectorAll('[data-tooltip-key]').forEach(node =>
			node.setAttribute('content', this.getConstants(node.dataset.tooltipKey))
		);
	};

	getConstants(key, ...args) {
		const constant = constants[this.language][key] ?? fragmentsConstants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	async render() {
		this.innerHTML = /*html*/ `<style>${style}</style>`;
		if (+this.hasTextInters >= 1)
			this.data = await getFragmentInters(this.dataset.xmlId, this.getRequestBody(true));

		this.onRender[this.type]();
	}

	onRender = {
		virtual: () => {
			this.insertAdjacentHTML(
				'beforeend',
				virtualTranscription({
					language: this.language,
					xmlId: this.dataset.xmlId,
					urlId: this.dataset.urlId,
				})
			);
		},
		text: async () => {
			let element;
			if (this.hasNoInters)
				element = parseRawHTML(/*html*/ `<h4 class="text-center">${this.data.title}</h4>`);
			else if (isSingleAndSourceInter(this.data.inters || []))
				element = await sourceInter({ root: this, inter: this.data.inters[0] });
			else if (isSingleAndEditorial(this.data.inters ?? []))
				element = await editorialInter({ root: this, inter: this.data.inters[0] });
			else if (isSideBySide(this.data))
				element = await sideBySideTranscriptions({ root: this, inters: this.data.inters });
			else if (isLineByLine(this.data))
				element = await lineByLineTranscriptions({ root: this, inters: this.data.inters });

			element && this.appendChild(element);
		},
	};

	renderTextFragInterpretation = {
		empty: () => console.log('empty'),
		transcription: () => console.log(this.data),
	};

	getRequestBody(def) {
		this.updateTranscriptCheckboxesValue(def);
		return {
			inters: this.textIntersSelected.map(item => item.externalId),
			...checkBoxes,
		};
	}

	updateTranscriptCheckboxesValue = def => {
		if (def) return (this.transcriptionCheckboxes = checkBoxes);
		this.querySelectorAll("div#text-checkBoxesContainer input[type='checkbox']").forEach(
			input => (this.transcriptionCheckboxes[input.name] = input.checked)
		);
	};

	handleTranscriptionCheckboxChange = async () => {
		this.data = await updateFragmentInter(
			this.dataset.xmlId,
			this.dataset.urlId,
			this.getRequestBody()
		);
		this.render();
	};

	onChangeFac = ({ page }) => {
		const surface = this.data.inters[0].surfaceDetailsList[page];
		this.transcriptionCheckboxes.pbText = surface.pbText;
		updateFragmentInter(this.dataset.xmlId, this.dataset.urlId, this.getRequestBody()).then(
			data => {
				this.data = data;
				this.querySelector('div#transcriptionContainer').innerHTML = data.transcriptions[0];
			}
		);
	};
}
customElements.define('frag-inter', FragInter);

async function editorialInter({ root, inter }) {
	return (await import('../editorial-inter')).default({ root, inter });
}

async function sourceInter({ root, inter }) {
	return (await import('../source-inter.js')).default({ root, inter });
}

async function sideBySideTranscriptions({ root, inters }) {
	return (await import('../side-by-side-transcriptions')).default({ root, inters });
}

async function lineByLineTranscriptions({ root, inters }) {
	return (await import('../line-by-line-transcriptions')).default({ root, inters });
}
