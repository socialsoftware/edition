/** @format */

import { deleteSessionsRequest } from '@src/api-requests.js';
import SessionsListTable from './sessions-list-table';

export default ({ root, content }) => {
	const onDeleteSessions = () => {
		deleteSessionsRequest().then(data => {
			root.usersData.sessionList = data.sessionList;
			root.shadowRoot
				.querySelector('div#sessions-list')
				.replaceChildren(<SessionsListTable root={root} />);
		});
	};
	return (
		<div id="deleteSessions" class="row btn-row">
			<button
				tooltip-ref="delete-sessions-button"
				type="button"
				class="btn btn-danger ellipsis"
				onClick={onDeleteSessions}>
				<span class="icon icon-edit"></span>
				<span label data-key="deleteUserSessions">
					{content}
				</span>
			</button>
			<ldod-tooltip
				placement="top"
				data-ref="[tooltip-ref='delete-sessions-button']"
				data-users-tooltip-key="deleteUserSessions"
				content={content}></ldod-tooltip>
		</div>
	);
};
