/** @format */

import constants from '../constants.js';

export default ({ root }) => {
	return (
		<div id="sessions-list" class="row">
			<ldod-table
				id="user-sessionsListTable"
				classes="table table-responsive-sm table-striped table-bordered"
				headers={constants.sessionListHeaders}
				data={root.usersData.sessionList.map(row => ({
					...row,
					search: Object.values(row).reduce((prev, curr) => {
						return prev.concat(String(curr), ',');
					}, ''),
				}))}
				language={root.language}
				constants={constants}
				data-searchkey="sessionId"></ldod-table>
		</div>
	);
};
