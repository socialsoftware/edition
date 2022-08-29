import InfoRangesTable from './InfoRangesTable.jsx';
import { getTableData } from './tableData.jsx';

export default ({ node, tableInfo, constants }) => {
  const infoRangesHeaders = [
    'xmlId',
    'text',
    'quote',
    'start',
    'end',
    'startOffset',
    'endOffset',
  ];
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
