/** @format */

import { changeActiveRequest } from '@src/api-requests.js';
import '@ui/modal-bs.js';
import '@ui/table.js';
import constants from '../constants.js';
import UptadeUserForm from './update-user-form.jsx';

const onChangeActive = async (externalId, root) => {
	changeActiveRequest(externalId).then(res => {
		const user = root.usersData.userList.find(user => user.externalId === externalId);
		user.active = res.ok;
		replaceActive(root, user);
	});
};

const replaceActive = (root, user) => {
	const newActive = getUsersListActive(root, user);
	root.shadowRoot.querySelector(`#active-${user.externalId}`).replaceWith(newActive);
};

const onEditUser = async ({ target }, root) => {
	const user = root.usersData.userList.find(({ externalId }) => externalId === target.dataset.id);
	const modal = root.usersEditModal;
	if (!modal) return;
	const bodySlot = modal.querySelector("div[slot='body-slot']");
	bodySlot.innerHTML = '';
	bodySlot.appendChild(<UptadeUserForm user={user} root={root} />);
	modal.toggleAttribute('show');
};

const getUsersListActive = (root, user) => {
	const active = user.active;
	const id = user.externalId;
	return (
		<div id={`active-${id}`} class="text-center">
			<button
				id={`button-active-${id}`}
				class={`btn btn-sm`}
				onClick={() => onChangeActive(id, root)}>
				<span
					is="ldod-span-icon"
					icon={active ? 'circle-check' : 'circle-xmark'}
					fill={active ? '#198754' : '#dc3545'}
					size="1.25rem"></span>
			</button>
		</div>
	);
};

const getUsersListActions = (root, user) => {
	const username = user.username;
	const id = user.externalId;
	return (
		<div class="text-center">
			<div id="row-user-actions">
				<span
					id={`edit-icon-${id}`}
					data-id={id}
					is="ldod-span-icon"
					icon="pen-to-square"
					fill="#0d6efd"
					size="1.25rem"
					onClick={e => onEditUser(e, root)}></span>
				<span
					id={`trash-icon-${id}`}
					data-id={id}
					data-username={username}
					is="ldod-span-icon"
					icon="trash-can"
					fill="#dc3545"
					size="1.25rem"
					onClick={root.onDeleteUser}></span>
			</div>
		</div>
	);
};

export default ({ root }) => {
	const usersData = root.usersData;
	const language = root.language;
	return (
		<div>
			<div id="users-list" class="row">
				<ldod-table
					id="user-users-list-table"
					classes="table table-responsive-sm table-striped table-bordered"
					headers={constants.usersListHeaders}
					data={usersData.userList.map(user => ({
						...user,
						enabled: (
							<div data-users-key={String(user.enabled).toUpperCase()}>
								{root.getConstant(String(user.enabled).toUpperCase())}
							</div>
						),
						active: getUsersListActive(root, user),
						actions: getUsersListActions(root, user),
						search: Object.values(user).reduce((prev, curr) => {
							return prev.concat(String(curr), ',');
						}, ''),
					}))}
					constants={constants[language]}
					data-searchkey="externalId"></ldod-table>
				<ldod-tooltip
					data-ref="table>thead>tr>th[data-key='actions']"
					data-users-tooltip-key="tooltipActions"
					placement="top"
					content={root.getConstant('tooltipActions')}></ldod-tooltip>
				<ldod-tooltip
					placement="top"
					data-ref="table>thead>tr>th[data-key='active']"
					data-users-tooltip-key="toggleActiveMode"
					content={root.getConstant('toggleActiveMode')}></ldod-tooltip>
			</div>
		</div>
	);
};
