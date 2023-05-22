/** @format */

import { approveParticipant } from '@src/restricted-api-requests';
import references from '@src/references';
import constants from './constants';
const isAdmin = node => node.edition?.member?.admin;

const onAddParticipant = async (node, username) => {
	node.edition = await approveParticipant(node.edition.externalId, username);
	node.toggleAttribute('data', true);
};

const getTableData = node => {
	return node.edition.participants
		.filter(({ pending }) => pending)
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

				add: isAdmin(node) && (
					<div class="center-container">
						<span
							class="icon-size icon-add"
							onClick={() => onAddParticipant(node, participant.username)}></span>
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
			id="virtual-vePendents"
			classes="table table-hover"
			headers={constants.pendentHeaders}
			data={getTableData(node)}
			constants={constants}
			data-searchkey="externalId"></ldod-table>
	);
};
