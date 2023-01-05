import '@shared/modal.js';
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
		return this.querySelector('ldod-modal');
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
		this.toggleAttribute('data');
	};

	connectedCallback() {
		this.addEventListener('ldod-modal-close', this.onCloseModal);
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangedAttribute[name](oldV, newV);
	}

	disconnectedCallback() {}

	onCloseModal = () => {
		this.toggleAttribute('show', false);
	};

	render() {
		this.innerHTML = '';
		this.appendChild(<style>{style}</style>);
		this.appendChild(
			<ldod-modal id="virtual-gamesModal" dialog-class="modal-fullscreen" no-footer>
				<span slot="header-slot">{this.edition?.title}</span>
				<div slot="body-slot">
					<div id="virtual-createCGContainer" class="mb-5">
						<CreateGame node={this} />
					</div>
					<div class="mb-5">
						<GamesTable node={this} />
					</div>
				</div>
			</ldod-modal>
		);
	}

	onChangedAttribute = {
		data: () => {
			this.render();
			this.modal?.toggleAttribute('show', this.show);
		},
		show: (oldV, newV) => {
			this.render();
			this.modal?.toggleAttribute('show', this.show);
		},
	};
}
!customElements.get('ldod-ve-games') && customElements.define('ldod-ve-games', LdodVeGames);
