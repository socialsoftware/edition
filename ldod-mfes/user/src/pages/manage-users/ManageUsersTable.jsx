import UsersTable from './components/UsersListTable.jsx';
import UsersTitle from './components/UsersTitle';
import AdminModeButton from './components/AdminModeButton';
import DeleteSessionsButton from './components/DeleteSessionsButton';
import SessionsListTable from './components/SessionsListTable';

export default ({ node }) => {
  return (
    <div class="container">
      {node.usersData && (
        <>
          <button
            class="btn btn-secondary"
            type="button"
            onClick={node.handleSwitch}>
            Switch to sessions
          </button>
          <div id="users" class="subject" show>
            <UsersTitle node={node} title={node.getConstants('users')} />
            <div class="upload-export-users">
              <ldod-upload
                data-key="uploadUsers"
                title={node.getConstants('uploadUsers')}
                data-url={`/admin/user/upload-users`}></ldod-upload>
              <ldod-export
                file-prefix="users"
                data-key="exportUsers"
                title={node.getConstants('exportUsers')}
                data-url={`/admin/user/export-users`}></ldod-export>
            </div>
            <UsersTable node={node} />
          </div>
          <div id="sessions" class="subject">
            <h1 class="text-center" data-key="sessions">
              {node.getConstants('sessions')}
            </h1>
            <AdminModeButton
              node={node}
              buttonLabel={node.getConstants(`${node.getMode()}Mode`)}
              tooltipContent={node.getConstants('changeLdodMode')}
            />
            <DeleteSessionsButton
              node={node}
              content={node.getConstants('deleteUserSessions')}
            />
            <SessionsListTable node={node} />
          </div>
        </>
      )}
    </div>
  );
};
