import constants from '../resources/constants';
import Title from '../../../common/title';
import ExpertEditionTable from './components/expert-edition-table';

export class LdodEditionAcrn extends HTMLElement {
	constructor(lang, data) {
		super();
		this.language = lang;
		this.data = data;
		this.editor = data?.[0]?.editor;
	}

	get ldodTable() {
		return this.querySelector('ldod-table#expertEditionTable');
	}

	get language() {
		return this.getAttribute('language');
	}

	set language(lang) {
		this.setAttribute('language', lang);
	}

	get wrapper() {
		return this.querySelector('div#expertEditionFragsWrapper');
	}

	static get observedAttributes() {
		return ['language'];
	}

	getConstants(key, ...args) {
		const constant = constants[this.language][key];
		return args.length ? constant(...args) : constant;
	}

	connectedCallback() {
		this.appendChild(<div id="expertEditionFragsWrapper"></div>);
		this.addEventListeners();
		this.render();
	}

	render() {
		let titleArgs = {
			editor: this.editor,
			value: this.data.length,
		};
		this.wrapper.innerHTML = '';
		this.wrapper.appendChild(
			<>
				<Title args={JSON.stringify(titleArgs)} title={this.getConstants('edition', titleArgs)} key="edition" />
				<ExpertEditionTable node={this} constants={constants} />
			</>
		);
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handeChangedAttribute[name](oldV, newV);
	}

	handeChangedAttribute = {
		language: (oldV, newV) => {
			if (oldV && oldV !== newV) this.handleChangedLanguage();
		},
	};

	addEventListeners = () => {
		this.addEventListener('ldod-table-searched', this.updateTitle);
	};

	updateTitle = ({ detail }) => {
		let titleArgs = {
			editor: this.editor,
			value: detail.size,
		};
		if (this.ldodTable.isFullyLoaded)
			this.querySelector('h3#title').firstChild.textContent = this.getConstants('edition', titleArgs);
	};

	handleChangedLanguage = () => {
		this.querySelectorAll('[data-key]').forEach(node => {
			return (node.firstChild.textContent = node.dataset.args
				? this.getConstants(node.dataset.key, JSON.parse(node.dataset.args))
				: this.getConstants(node.dataset.key));
		});
	};
	disconnectedCallback() {}
}
!customElements.get('ldod-edition-acrn') && customElements.define('ldod-edition-acrn', LdodEditionAcrn);
