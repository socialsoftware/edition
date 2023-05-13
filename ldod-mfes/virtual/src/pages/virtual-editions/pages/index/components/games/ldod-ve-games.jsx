/** @format */

import '@ui/modal-bs.js';
import constants from './constants';
import GamesTable from './games-table';
import gameStyle from './games.css?inline';
import style from '../style.css?inline';
import CreateGame from './create-game-form';
import formStyle from '@ui/bootstrap/forms-css.js';
import buttonsStyle from '@ui/bootstrap/buttons-css.js';

const sheet = new CSSStyleSheet();
sheet.replaceSync(buttonsStyle + formStyle + style + gameStyle);

export class LdodVeGames extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}

	get language() {
		return this.getAttribute('language');
	}

	get hasShow() {
		return this.hasAttribute('show');
	}

	get modal() {
		return this.shadowRoot.querySelector('ldod-bs-modal');
	}

	static get observedAttributes() {
		return ['data', 'show'];
	}

	getConstants(key) {
		return constants[this.language][key];
	}

	updateData = ({ games, inters, publicAnnotation }) => {
		this.games = games;
		this.inters = inters;
		this.publicAnnotation = publicAnnotation;
	};

	connectedCallback() {
		this.addEventListener('ldod-modal-close', this.onCloseModal);
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangedAttribute[name](oldV, newV);
	}

	onCloseModal = e => {
		this.toggleAttribute('show', false);
	};

	render() {
		this.shadowRoot.innerHTML = '';
		this.shadowRoot.appendChild(
			<ldod-bs-modal
				id="virtual-games-modal"
				dialog-class="modal-xl modal-fullscreen-lg-down modal-dialog-scrollable"
				static>
				<h5 slot="header-slot">
					<span>{this.edition?.title} - </span>
					<span>{this.getConstants('game')}</span>
				</h5>
				<div slot="body-slot">
					<div id="virtual-createCGContainer" class="mb-5">
						<CreateGame node={this} />
					</div>
					<div id="games-table" class="mb-5">
						<GamesTable node={this} />
					</div>
				</div>
			</ldod-bs-modal>
		);
	}

	updateTable(data) {
		data && this.updateData(data);
		const gamesTable = this.shadowRoot.querySelector('#games-table');
		gamesTable.innerHTML = '';
		gamesTable.appendChild(<GamesTable node={this} />);
	}

	onChangedAttribute = {
		show: () => {
			if (this.hasShow) this.render();
			this.modal.toggleAttribute('show', this.hasShow);
		},
	};

	show() {
		if (this.hasShow) this.updateTable();
		else this.toggleAttribute('show', true);
	}
}
!customElements.get('ldod-ve-games') && customElements.define('ldod-ve-games', LdodVeGames);
