/** @format */

import { switchMemberRole, removeParticipant } from '@src/restricted-api-requests';
import references from '@src/references';
import constants from './constants';
const isAdmin = node => node.edition?.member?.admin;

const onSwitchRole = async (node, username) => {
	node.edition = await switchMemberRole(node.edition.externalId, username);
	node.toggleAttribute('data');
};

const onRemove = async (node, username) => {
	node.edition = await removeParticipant(node.edition.externalId, username);
	node.toggleAttribute('data');
};

const getTableData = node => {
	return node.edition.participants
		.filter(({ active }) => active)
		.map(participant => {
			return {
				externalId: participant.externalId,
				username: (
					<a is="nav-to" to={references.user(participant.username)}>
						{participant.username}
					</a>
				),
				firstname: participant.firstname,
				lastname: participant.lastname,
				email: participant.email,
				role:
					isAdmin(node) && participant.canSwitchRole ? (
						<button
							class="btn btn-primary btn-sm"
							type="button"
							onClick={() => onSwitchRole(node, participant.username)}>
							<span
								is="ldod-span-icon"
								icon="rotate"
								fill="#fff"
								size="14px"
								style={{ marginRight: '8px' }}></span>
							{node.getConstants(participant.admin ? 'admin' : 'member')}
						</button>
					) : (
						node.getConstants(participant.admin ? 'admin' : 'member')
					),
				remove: participant.canBeRemoved && (
					<div class="center-container">
						<span
							class="icon-size icon-minus"
							onClick={() => onRemove(node, participant.username)}></span>
					</div>
				),

				search: JSON.stringify(participant),
			};
		});
};

export default ({ node }) => {
	return (
		<ldod-table
			language={node.language}
			id="virtual-veMembers"
			classes="table  table-hover"
			headers={constants.membersHeaders}
			data={getTableData(node)}
			constants={constants}
			data-searchkey="externalId"></ldod-table>
	);
};
