/** @format */

import '@shared/modal.js';
import constants from './constants';
import MembersTable from './members-table';
import style from './editors.css?inline';
import formsCss from '@shared/bootstrap/forms-css.js';
import buttonsCss from '@shared/bootstrap/buttons-css.js';
import PendentTable from './pendent-table';
import { addParticipant } from '@src/restricted-api-requests';
import { errorPublisher } from '../../../../../../event-module';
export class LdodVeEditors extends HTMLElement {
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
		return this.querySelector('ldod-bs-modal#virtual-editors-modal');
	}

	get participantUsername() {
		return this.querySelector("input[name='participantUsername']").value;
	}

	static get observedAttributes() {
		return ['data', 'show'];
	}

	getConstants(key) {
		return constants[this.language][key];
	}

	connectedCallback() {
		this.addEventListener('ldod-modal-close', this.onCloseModal);
	}

	attributeChangedCallback(name, oldV, newV) {
		this.onChangedAttribute[name](oldV, newV);
	}

	render() {
		this.innerHTML = '';
		this.appendChild(<style>{formsCss + buttonsCss + style}</style>);
		this.appendChild(
			<ldod-bs-modal id="virtual-editors-modal" dialog-class="modal-xl">
				<h4 slot="header-slot">
					<span>{this.edition?.title} - </span>
					<span>{this.getConstants('editors')}</span>
				</h4>
				<div slot="body-slot">
					{this.edition?.member?.admin && (
						<form onSubmit={this.onAdd}>
							<div class="input-group mb-5">
								<input
									style={{ height: 'initial', maxWidth: '300px' }}
									type="text"
									class="form-control"
									placeholder="username"
									name="participantUsername"
									aria-label="participant username"
									aria-describedby="add-participant-username"
									required
								/>
								<button
									class="btn btn-primary"
									type="submit"
									id="add-participant-username">
									{this.getConstants('addMember')}
								</button>
							</div>
						</form>
					)}
					<div class="mb-5">
						<h4 class="text-center">{this.getConstants('editionMembers')}</h4>
						<MembersTable node={this} />
					</div>
					<div class="mb-5">
						{this.edition.pendingMembers.length && (
							<>
								<h4 class="text-center">{this.getConstants('pendingRequests')}</h4>
								<PendentTable node={this} />
							</>
						)}
					</div>
				</div>
			</ldod-bs-modal>
		);
	}
	isMemberActive = () => this.edition.member?.active;

	onChangedAttribute = {
		data: () => {
			this.render();
			return this.isMemberActive()
				? this.modal?.toggleAttribute('show', true)
				: this.onCloseModal();
		},
		show: (oldV, newV) => {
			this.render();
			this.modal?.toggleAttribute('show', this.show);
		},
	};

	onCloseModal = () => {
		this.toggleAttribute('show', false);
		this.parent.updateTable();
	};

	onAdd = async e => {
		e.preventDefault();
		addParticipant(this.edition.externalId, this.participantUsername)
			.then(data => {
				this.edition = data;
				this.toggleAttribute('data');
			})
			.catch(error => {
				console.error(error);
				errorPublisher(error?.message);
			});
	};
}
!customElements.get('ldod-ve-editors') && customElements.define('ldod-ve-editors', LdodVeEditors);
