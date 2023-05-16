/** @format */
// Include the typedefs file
/// <reference path="../../typedef.js" />

import { getUsersList, removeUserRequest, updateUserRequest } from '../../api-requests';
import ManageUsersTable from './manage-users-table';
import { exportButton, uploadButton } from '@ui/buttons.js';
import UpdateModal from './update-user-modal.js';

import style from './manage-users-style.css?inline';
import rootCss from '@ui/bootstrap/root-css.js';
import formsCss from '@ui/bootstrap/forms-css.js';
import buttonsCss from '@ui/bootstrap/buttons-css.js';
import switchCss from './switch.css?inline';
import constants from './constants';
import { errorPublisher, messagePublisher } from '../../events-modules';
import { getUser, setState } from '../../store';

const sheet = new CSSStyleSheet();
sheet.replaceSync(rootCss + formsCss + buttonsCss + switchCss + style);

exportButton();
uploadButton();

async function loadToolip() {
	await import('@ui/tooltip.js');
}

export class LdodManageUsers extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
	}

	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	get usersLength() {
		return this.usersData?.userList?.length;
	}

	get usersEditModal() {
		return this.shadowRoot.querySelector('ldod-bs-modal');
	}

	getData() {
		return Promise.resolve(
			getUsersList()
				.then(users => (this.usersData = users))
				.catch(e => console.error(e))
		);
	}

	getConstant(key) {
		return constants[this.language][key];
	}

	connectedCallback() {
		this.getData().then(() => {
			if (!this.usersData) return;
			this.shadowRoot.innerHTML = /*html*/ `
				<div class="container"></div>
				${UpdateModal(this)}
			`;
			this.render();
		});
	}

	render() {
		this.shadowRoot.firstElementChild.replaceWith(ManageUsersTable(this));
		this.addEventListeners();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handlers[name](oldV, newV);
	}

	handlers = {
		language: (oldV, newV) => {
			if (oldV && newV !== oldV) this.handleChangeLanguage();
		},
	};

	handleChangeLanguage() {
		this.shadowRoot.querySelectorAll('[data-key]').forEach(ele => {
			ele.textContent = this.getConstant(ele.dataset.key);
		});
		this.shadowRoot.querySelectorAll('[data-users-key]').forEach(ele => {
			ele.textContent = this.getConstant(ele.dataset.usersKey);
		});
		this.shadowRoot.querySelectorAll('[title]').forEach(ele => {
			ele.setAttribute('title', this.getConstant(ele.dataset.usersKey));
		});
		this.shadowRoot.querySelectorAll('[data-users-tooltip-key]').forEach(ele => {
			ele.setAttribute('content', this.getConstant(ele.dataset.usersTooltipKey));
		});

		this.shadowRoot
			.querySelectorAll('[language]')
			.forEach(ele => ele.setAttribute('language', this.language));
	}

	addEventListeners() {
		this.addEventListener('ldod:file-uploaded', this.handleUsersUpload);
		this.shadowRoot
			.querySelector('div.container')
			.addEventListener('pointerenter', loadToolip, { once: true });
		this.addEventListener('ldod-table-searched', this.updateUsersListTitleOnSearch);
	}

	handleUsersUpload = e => {
		e.stopPropagation();
		const payload = e.detail.payload;
		if (!payload.ok) return errorPublisher(payload.message);
		this.getData().then(() => {
			this.render();
			messagePublisher(payload.message);
		});
	};

	updateUsersListTitleOnSearch = ({ detail }) => {
		if (detail.id === 'user-users-list-table') {
			const size = this.shadowRoot.querySelectorAll(
				'table#user-users-list-table tr[searched]'
			).length;
			this.shadowRoot.querySelector(
				'h2#title :not([data-users-key])'
			).innerHTML = `&nbsp;(${size})`;
		}
	};

	getMode() {
		return this.usersData.ldoDAdmin ? 'admin' : 'user';
	}

	setMode(mode) {
		this.usersData.ldoDAdmin = mode;
	}

	onSwitchTopic = e => {
		this.shadowRoot.querySelectorAll('div[topic]').forEach(ele => ele.toggleAttribute('show'));
		e.target.textContent = `Switch to ${
			this.shadowRoot.querySelector('div[topic]:not([show])')?.id
		}`;
	};

	onDeleteUser = ({ target }) => {
		if (!confirm(`Delete user ${target.dataset.username} ?`)) return;
		removeUserRequest(target.dataset.id)
			.then(res => {
				this.usersData.userList = res.userList;
				this.render();
			})
			.catch(e => console.error(e));
	};

	/**
	 *
	 * @param {SubmitEvent} e
	 */
	onUpdate = e => {
		e.preventDefault();
		const userData = Object.fromEntries(new FormData(e.target));
		updateUserRequest(userData)
			.then(({ userList }) => {
				this.usersData.userList = userList;
				checkUserLogged(userData, userList);
				this.usersEditModal.toggleAttribute('show');
				this.render();
			})
			.catch(e => console.error(e));
	};
}

!customElements.get('ldod-manage-users') &&
	customElements.define('ldod-manage-users', LdodManageUsers);

/**
 *
 * @param {User} userUpdated
 * @param {Array<User>} usersList
 */
function checkUserLogged(userUpdated, usersList) {
	const user = usersList.find(u => u.username === userUpdated.newUsername);
	if (getUser().username === userUpdated.newUsername) setState(state => ({ ...state, user }));
}
