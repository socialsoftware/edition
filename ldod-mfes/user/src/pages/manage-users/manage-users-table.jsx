/** @format */

import UsersTable from './components/users-list-table.jsx';
import UsersTitle from './components/users-title';
import AdminModeButton from './components/admin-mode-button';
import DeleteSessionsButton from './components/delete-sessions-button';
import SessionsListTable from './components/sessions-list-table';

export default root => {
	return (
		<div class="container">
			<>
				<button
					style={{ margin: '1em 0' }}
					class="btn btn-secondary"
					type="button"
					onClick={root.onSwitchTopic}>
					Switch to sessions
				</button>
				<div id="users" topic="users" show>
					<UsersTitle root={root} title={root.getConstant('users')} />
					<div class="upload-export-users">
						<ldod-upload
							data-users-key="uploadUsers"
							title={root.getConstant('uploadUsers')}
							data-url={`/admin/user/upload-users`}></ldod-upload>
						<ldod-export
							file-prefix="users"
							data-users-key="exportUsers"
							title={root.getConstant('exportUsers')}
							data-url={`/admin/user/export-users`}></ldod-export>
					</div>
					<UsersTable root={root} />
				</div>
				<div id="sessions" topic="sessions">
					<h2 class="text-center" data-users-key="sessions">
						{root.getConstant('sessions')}
					</h2>
					<div id="sessions-actions">
						<AdminModeButton
							root={root}
							buttonLabel={root.getConstant(`${root.getMode()}Mode`)}
							tooltipContent={root.getConstant('changeLdodMode')}
						/>
						<DeleteSessionsButton
							root={root}
							content={root.getConstant('deleteUserSessions')}
						/>
					</div>
					<SessionsListTable root={root} />
				</div>
			</>
		</div>
	);
};
