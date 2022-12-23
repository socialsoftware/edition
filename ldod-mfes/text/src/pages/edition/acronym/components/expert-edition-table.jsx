import { getTableData } from './table-data';
import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export default ({ node, constants }) => {
  return (
    <>
      <ldod-table
        id="expertEditionTable"
        classes="table table-bordered table-hover"
        headers={constants.headers}
        data={getTableData(node)}
        constants={constants}
        language={node.language}
        data-searchkey="externalId"></ldod-table>
    </>
  );
};
