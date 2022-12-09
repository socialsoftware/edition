import { deleteSessionsRequest } from '@src/api-requests.js';
import SessionsListTable from './sessions-list-table';

export default ({ node, content }) => {
  const onDeleteSessions = () => {
    deleteSessionsRequest().then((data) => {
      node.usersData.sessionList = data.sessionList;
      node
        .querySelector('div#sessions-list')
        .replaceChildren(<SessionsListTable node={node} />);
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
        data-tooltipkey="deleteUserSessions"
        content={content}></ldod-tooltip>
    </div>
  );
};
