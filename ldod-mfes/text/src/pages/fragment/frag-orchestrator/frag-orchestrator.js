/** @format */
import { navigateTo } from '@core';
import { getFragment } from '../../../api-requests';
import { getNewFragInter as getNewFragInter, isVirtualInter } from '../utils';
import fragNav from '../components/frag-nav/frag-nav-container';
import '../components/frag-inter/frag-inter';
import fragOrchestratorHtml from './frag-orchestrator-html';
import virtualFragNav from '../components/virtual/virtual-frag-nav';

function getExternalId(inter) {
	return inter.externalId;
}

customElements.define(
	'frag-orchestrator',
	class FragOrchestrator extends HTMLElement {
		constructor() {
			super();
			this.textIntersSelected = [];
			this.innerHTML = fragOrchestratorHtml;
			this.textNavContainer = this.querySelector('#text-fragment-nav-container');
			this.virtualNavContainer = this.querySelector('#virtual-fragment-nav-container');
			this.interContainer = this.querySelector('#fragment-inter-container');
			this.xmlId = history.state.xmlId;
			this.urlId = history.state.urlId;
		}

		static get observedAttributes() {
			return ['language'];
		}

		get language() {
			return this.getAttribute('language');
		}
		get isVirtualInter() {
			return isVirtualInter(this.urlId);
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
			this.renderFragNavContainer();
			this.querySelectorAll('[language]').forEach(ele =>
				ele.setAttribute('language', this.language)
			);
		};

		async connectedCallback() {
			this.addEventListeners();
			if (!this.xmlId) return navigateTo('/');
			await this.fetchData();
			this.render();
		}

		async fetchData() {
			this.data =
				this.urlId && !this.isVirtualInter
					? await getNewFragInter(this.xmlId, this.urlId)
					: await getFragment(this.xmlId);
			this.textIntersSelected = this.data.inters || [];
		}

		render() {
			this.renderFragNavContainer();
			this.renderInterContainer();
		}

		renderFragNavContainer() {
			this.renderTextNavContainer();
			this.renderVirtualNavContainer();
		}

		renderInterContainer() {
			this.interContainer.innerHTML = /*html*/ `
			<frag-inter
				language="${this.language}"
				data-url-id="${this.urlId || ''}"
				data-xml-id="${this.xmlId || ''}"
				data-virtual-inters="${this.virtualInters || ''}"
				data-text-inters='${this.textIntersSelected.length || ''}'
			></frag-inter>
			`;
			const instance = customElements.get('frag-inter').instance;
			instance.data = this.data;
			instance.textIntersSelected = this.textIntersSelected;
			instance.render();
		}

		renderTextNavContainer() {
			this.textNavContainer.innerHTML = fragNav(
				this.data,
				this.textIntersSelected.map(getExternalId),
				this.language
			);
		}

		renderVirtualNavContainer() {
			this.virtualNavContainer.innerHTML = virtualFragNav({
				language: this.language,
				xmlId: this.xmlId,
				urlId: this.textIntersSelected.length ? '' : this.urlId,
			});
		}

		renderTextInter() {
			this.interContainer.insertAdjacentHTML('afterbegin', '');
		}

		addEventListeners() {
			this.addEventListener('experts:changed', this.onTextInterSelection);
			this.addEventListener('witnesses:changed', this.onTextInterSelection);
			this.addEventListener('virtual:inter-selected', this.onVirtualInterSelection);
		}

		onVirtualInterSelection(e) {
			this.virtualInters = e.detail.virtualInters;
			this.textIntersSelected = [];
			this.urlId = '';
			this.renderTextNavContainer();
			this.renderInterContainer();
		}

		async onTextInterSelection({ detail }) {
			this.updadeTextIntersSelection({ externalId: detail.id, urlId: detail.name });
			this.virtualInters = '';
			this.renderVirtualNavContainer();
			this.renderInterContainer();
		}

		updadeTextIntersSelection(inter) {
			this.textIntersSelected.map(item => item.externalId).includes(inter.externalId)
				? this.removeFromIntersSelected(inter)
				: this.addToIntersSelected(inter);
		}

		addToIntersSelected(inter) {
			this.textIntersSelected.push(inter);
		}

		removeFromIntersSelected(inter) {
			this.textIntersSelected = this.textIntersSelected.filter(
				item => item.externalId !== inter.externalId
			);
		}
	}
);
