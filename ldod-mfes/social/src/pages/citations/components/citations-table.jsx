import { getTableData } from './data';

export default ({ node, constants }) => {
  return (
    <ldod-table
      id="twitterCitationsTable"
      classes="table table-responsive-sm table-hover table-bordered"
      headers={constants.headers}
      data={getTableData(node.citations)}
      language={node.language}
      constants={constants}
      data-searchkey="tweetId"></ldod-table>
  );
};
