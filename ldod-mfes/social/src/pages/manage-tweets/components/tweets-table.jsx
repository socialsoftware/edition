import { getTableData } from './table-data.jsx';

export default ({ node, tableInfo, constants }) => {
  return (
    <>
      <p
        data-key="tableInfo"
        data-args={JSON.stringify(node.getTableInfoArgs())}>
        {tableInfo}
      </p>
      <ldod-table
        id="tweetsTable"
        classes="table table-responsive table-bordered"
        headers={constants.headers}
        data={getTableData(node.tweets.twitterCitations)}
        language={node.language}
        constants={constants}
        data-searchkey="tweetId"></ldod-table>
    </>
  );
};
