/** @format */

import style from '@src/style.css?inline';
import thisStyle from './style.css?inline';

import constants from './constants';
import { advancedSearch, getAdvSearchDto } from '../../api-requests';
import './criteria-form';
import AdvancedSearchTableResult from './components/AdvancedSearchTableResult';

export class LdodSearchAdvanced extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.constants = constants;
		this.sequentialId = 1;
		this.mode = 'and';
	}

	get language() {
		return this.getAttribute('language');
	}

	get resultTable() {
		return this.shadowRoot.querySelector('ldod-table#search-advanced');
	}

	static get observedAttributes() {
		return ['language'];
	}

	getConstants(key) {
		return constants(this.fragsNumber, this.intersNumber)[this.language][key];
	}

	CriteriaForm = () => <form is="criteria-form" root={this}></form>;

	fetchData = async () => {
		getAdvSearchDto()
			.then(data => (this.data = data))
			.catch(e => console.error(e));
	};

	async connectedCallback() {
		await this.fetchData();
		this.shadowRoot.appendChild(
			<style>
				{style}
				{thisStyle}
			</style>
		);
		this.render();
	}

	render() {
		this.shadowRoot.appendChild(
			<>
				<h3 class="text-center" data-search-key="advancedSearch">
					{this.getConstants('advancedSearch')}
				</h3>
				<div id="main">
					<select
						class="form-select"
						name="mode"
						onChange={({ target }) => (this.mode = target.value)}>
						<option value="and" data-search-key="criteriaAnd">
							{this.getConstants('criteriaAnd')}
						</option>
						<option value="or" data-search-key="criteriaOr">
							{this.getConstants('criteriaOr')}
						</option>
					</select>
				</div>
				<div id="criterias-container">
					<this.CriteriaForm />
				</div>
				<hr></hr>
				<div id="actions">
					<button class="btn btn-outline-secondary" onClick={this.onSearch}>
						<span class="icon icon-magnifying-glass"></span>
						<span data-search-key="search">{this.getConstants('search')}</span>
					</button>
					<button
						class="btn btn-outline-secondary"
						id="add-criteria"
						onClick={this.addCriteria}>
						<span class="icon icon-plus"></span>
					</button>
				</div>
			</>
		);
	}

	addCriteria = () => {
		++this.sequentialId;
		this.shadowRoot.querySelector('#criterias-container').appendChild(<this.CriteriaForm />);
	};

	attributeChangedCallback(name, oldV, newV) {
		this.onChangeAttribute[name]();
	}

	onChangeAttribute = {
		language: () => this.onLanguage(),
	};

	onLanguage = () => {
		this.shadowRoot.querySelectorAll('[data-search-key]').forEach(element => {
			element.firstChild.textContent =
				this.getConstants(element.dataset.searchKey) || element.dataset.searchKey;
		});
	};

	wrapDate = body => {
		let begin = body.begin;
		let end = body.end;
		let option = body.option;
		body = { ...body, date: { type: 'date', option, begin, end } };
		delete body.begin, delete body.end, delete body.option;
		return body;
	};

	wrapHeteronym = body => {
		let heteronym = body.heteronym;
		delete body.heteronym;
		body = { ...body, heteronym: { type: 'heteronym', heteronym } };
		return body;
	};

	wrapDateAndHeteronymElements = body => {
		if (body.type === 'date' || body.type === 'heteronym') return body;
		if (Object.keys(body).includes('option')) body = this.wrapDate(body);
		if (Object.keys(body).includes('heteronym')) body = this.wrapHeteronym(body);
		return body;
	};

	onSearch = async () => {
		const forms = Array.from(this.shadowRoot.querySelectorAll('form'));
		if (forms.some(form => !form.reportValidity())) return;
		let body = forms.map(form =>
			this.wrapDateAndHeteronymElements(Object.fromEntries(new FormData(form)))
		);
		advancedSearch({ options: body, mode: this.mode })
			.then(data => {
				this.inters = data;
				this.criteriaNumber = body.map((c, i) => i);
				this.fragsNumber = new Set(data.map(frag => frag.xmlId)).size;
				this.intersNumber = data.length;
				this.renderTableResult();
			})
			.catch(error => console.log(error));
	};

	renderTableResult = () => {
		this.resultTable && this.resultTable.remove();
		this.shadowRoot.appendChild(<AdvancedSearchTableResult root={this} />);
	};

	disconnectedCallback() {}
}
!customElements.get('ldod-search-adv') &&
	customElements.define('ldod-search-adv', LdodSearchAdvanced);
