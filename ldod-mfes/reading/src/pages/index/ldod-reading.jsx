/** @format */

import constants from '../../constants';
import RecommendationModal from '../components/recommendation-modal/recommendation-modal';
import style from '../style.css?inline';
import { navigateTo } from '@core';
import readingReferences from '../../references';

const loadPopper = () =>
	import.meta.env.DEV ? import('@ui/tooltip.dev.js') : import('@ui/tooltip.js');

const sheet = new CSSStyleSheet();
sheet.replaceSync(style);
export class LdodReading extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.constants = constants;
	}

	get language() {
		return this.getAttribute('language');
	}

	get wrapper() {
		return this.shadowRoot.querySelector('#reading-wrapper');
	}

	get recommendationModal() {
		return this.shadowRoot.querySelector('#reading-recommendation--modal');
	}

	static get observedAttributes() {
		return ['language'];
	}

	getConstants(key) {
		return this.constants[this.language][key];
	}

	connectedCallback() {
		this.shadowRoot.appendChild(<div id="reading-wrapper"></div>);
		if (this.expertEditions) this.render();
		this.addEventListeners();
	}

	render() {
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<div class="reading-grid">
					<div class="reading-text reading-book-title" style={{ gridColumn: '1/8' }}>
						<h1 data-reading-key="title">{this.getConstants('title')}</h1>
					</div>
					{this.expertEditions.map(edition => {
						return (
							<div key={edition.externalId} class="reading-column">
								<h4>
									<a
										is="nav-to"
										to={readingReferences.editionInterPath(
											edition.xmlId,
											edition.urlId
										)}>
										{edition.editor}
									</a>
								</h4>
								<span
									is="ldod-span-icon"
									class="icon"
									icon="arrow-right"
									fill="black"
									size="30px"
									onClick={() => {
										navigateTo(
											readingReferences.editionInterPath(
												edition.xmlId,
												edition.urlId
											)
										);
									}}></span>
							</div>
						);
					})}
					<div class="reading-column">
						<span
							id="reading-recommendation"
							data-reading-key="recommendation"
							onClick={this.onRecommendation}>
							{this.getConstants('recommendation')}
						</span>
						<span
							id="reading-recommendationTooltip"
							is="ldod-span-icon"
							size="30px"
							icon="circle-info"
							class="icon"></span>
					</div>
				</div>

				<RecommendationModal root={this} />
			</>
		);
	}

	updateData(data) {
		this.expertEditions = data;
		this.render();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangeAttribute[name]();
	}

	onChangeAttribute = {
		language: () => this.onlanguageChange(),
	};

	onlanguageChange = () => {
		this.shadowRoot.querySelectorAll('[data-reading-key]').forEach(element => {
			element.firstChild.textContent = this.getConstants(element.dataset.readingKey);
		});
		this.shadowRoot.querySelectorAll('[data-reading-tooltip-key]').forEach(tooltip => {
			tooltip.setAttribute('content', this.getConstants(tooltip.dataset.readingTooltipKey));
		});
	};

	onRecommendation = async () => {
		this.recommendationModal.toggleAttribute('show', true);
	};

	onRecommendationSubmit = () => {
		this.recommendationModal.toggleAttribute('show');
	};

	addEventListeners = () => {
		this.wrapper.addEventListener('pointerenter', loadPopper);
	};
}
!customElements.get('ldod-reading') && customElements.define('ldod-reading', LdodReading);
