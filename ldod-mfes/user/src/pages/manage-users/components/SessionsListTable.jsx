import constants from '../resources/constants.js';

export default ({ node }) => {
  return (
    <div id="sessions-list" class="row">
      <ldod-table
        id="user-sessionsListTable"
        classes="table table-responsive-sm table-striped table-bordered"
        headers={constants.sessionListHeaders}
        data={node.usersData.sessionList.map((row) => ({
          ...row,
          search: Object.values(row).reduce((prev, curr) => {
            return prev.concat(String(curr), ',');
          }, ''),
        }))}
        language={node.language}
        constants={constants}
        data-searchkey="sessionId"></ldod-table>
    </div>
  );
};
