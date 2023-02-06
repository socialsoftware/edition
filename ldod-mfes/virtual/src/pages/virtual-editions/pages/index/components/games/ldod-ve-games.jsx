/** @format */

import '@shared/modal-bs.js';
import constants from './constants';
import GamesTable from './games-table';
import style from './games.css?inline';
import CreateGame from './create-game-form';

export class LdodVeGames extends HTMLElement {
	constructor() {
		super();
	}

	get language() {
		return this.getAttribute('language');
	}

	get show() {
		return this.hasAttribute('show');
	}

	get modal() {
		return this.querySelector('ldod-bs-modal');
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
		//this.onChangedAttribute.show();
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
		this.innerHTML = '';
		this.appendChild(<style>{style}</style>);
		this.appendChild(
			<ldod-bs-modal
				id="virtual-games-modal"
				dialog-class="modal-xl modal-fullscreen-lg-down modal-dialog-scrollable"
				static>
				<h4 slot="header-slot">{this.edition?.title}</h4>
				<div slot="body-slot">
					<div id="virtual-createCGContainer" class="mb-5">
						<CreateGame node={this} />
					</div>
					<div class="mb-5">
						<GamesTable node={this} />
					</div>
				</div>
			</ldod-bs-modal>
		);
	}

	onChangedAttribute = {
		data: function () {
			this.show();
		},
		show: () => {
			this.render();
			this.modal.toggleAttribute('show', this.show);
		},
	};
}
!customElements.get('ldod-ve-games') && customElements.define('ldod-ve-games', LdodVeGames);
