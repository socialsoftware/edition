/** @format */

import constants from '../../constants';
import style from '../style.css?inline';
import { navigateTo, htmlRender } from '@core';
import { readingStore } from '../../store';
import RecommendationModal from '../components/recommendation-modal/recommendation-modal';
import readingReferences from '../../references';
import { textFragInter } from '../../external-deps';

const loadPopper = () =>
	import.meta.env.DEV ? import('@ui/tooltip.dev.js') : import('@ui/tooltip.js');

const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

export class LdodReadingEdition extends HTMLElement {
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
		return this.shadowRoot.querySelector('#reading-edition-wrapper');
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
		this.shadowRoot.appendChild(<div id="reading-edition-wrapper"></div>);
		if (this.expertEditions) this.render();
		this.addEventListeners();
	}

	render() {
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<div class="reading-grid">
					{this.expertEditions.map((edition, index) => {
						const transcript = edition.transcript;
						return (
							<>
								<div class={transcript ? 'reading-column-open' : 'reading-column'}>
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
									{edition.editorialInters.map(inter => {
										return (
											<div key={inter.externalId} class="reading-inter">
												<h2>
													<a
														is="nav-to"
														to={readingReferences.editionInterPath(
															inter.xmlId,
															inter.urlId
														)}>
														{`${inter.number}`}
													</a>
												</h2>
												<div style={{ display: 'flex', flexWrap: 'wrap' }}>
													<span
														is="ldod-span-icon"
														icon="arrow-left"
														fill="black"
														size="30px"
														class="icon"
														onClick={() =>
															navigateTo(
																readingReferences.editionInterPath(
																	inter.prevXmlId,
																	inter.prevUrlId
																)
															)
														}></span>
													<span
														is="ldod-span-icon"
														icon="arrow-right"
														fill="black"
														size="30px"
														class="icon"
														onClick={() =>
															navigateTo(
																readingReferences.editionInterPath(
																	inter.nextXmlId,
																	inter.nextUrlId
																)
															)
														}></span>
												</div>
											</div>
										);
									})}
								</div>

								{transcript && (
									<div
										class="reading-text"
										style={{ gridColumn: `${index + 2}/${index + 9}` }}>
										<h1>
											<a
												is="nav-to"
												content
												to={textFragInter(edition.xmlId, edition.urlId)}>
												{edition.transcriptTitle}
											</a>
										</h1>
										<div class="reading-transcript">
											{htmlRender(transcript)}
										</div>
									</div>
								)}
							</>
						);
					})}
					<div class="reading-column">
						<span
							id="reading-recommendation"
							data-reading-key="recommendation"
							onClick={this.onRecommendation}>
							{this.getConstants('recommendation')}
						</span>
						<div class="reading-inter-current">
							<a
								is="nav-to"
								to={readingReferences.editionInterPath(
									this.currentInter.xmlId,
									this.currentInter.urlId
								)}>
								<h3>{this.currentInter.acronym}</h3>
								<h2>{`${this.currentInter.number}`}</h2>
							</a>
							<span
								id="reading-recommendationTooltip"
								is="ldod-span-icon"
								size="30px"
								icon="circle-info"
								class="icon"></span>
						</div>
						{this.prevInter && (
							<div class="reading-inter">
								<a
									is="nav-to"
									to={readingReferences.editionInterPath(
										this.prevInter.xmlId,
										this.prevInter.urlId
									)}>
									<h3>{this.prevInter.acronym}</h3>
									<h2>{`${this.prevInter.number}`}</h2>
								</a>
								<span
									is="ldod-span-icon"
									icon="arrow-left"
									fill="black"
									size="30px"
									class="icon"
									onClick={() =>
										navigateTo(
											readingReferences.editionInterPath(
												this.prevInter.xmlId,
												this.prevInter.urlId
											)
										)
									}></span>
							</div>
						)}

						{this.recommendedInters.map(recomm => {
							return (
								<div key={recomm.externalId} class="reading-inter">
									<a
										is="nav-to"
										to={readingReferences.editionInterPath(
											recomm.xmlId,
											recomm.urlId
										)}>
										<h3>{recomm.acronym}</h3>
										<h2>{`${recomm.number}`}</h2>
									</a>
									<span
										is="ldod-span-icon"
										icon="arrow-right"
										fill="black"
										size="30px"
										class="icon"
										onClick={() =>
											navigateTo(
												readingReferences.editionInterPath(
													recomm.xmlId,
													recomm.urlId
												)
											)
										}></span>
								</div>
							);
						})}
					</div>
				</div>
				<RecommendationModal root={this} />
			</>
		);
	}

	updateData(data) {
		this.expertEditions = data.editions;
		this.recommendedInters = data.recommendedInters;
		this.currentInter = data.currentInter;
		this.prevInter = data.prevInter;
		readingStore.setState(state => ({
			...state,
			read: data.read || state.read,
		}));
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
		navigateTo(
			readingReferences.editionInterPath(this.currentInter.xmlId, this.currentInter.urlId)
		);
	};

	addEventListeners = () => {
		this.wrapper.addEventListener('pointerenter', loadPopper);
	};
}
!customElements.get('ldod-reading-edition') &&
	customElements.define('ldod-reading-edition', LdodReadingEdition);
