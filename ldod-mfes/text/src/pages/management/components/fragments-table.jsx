/** @format */

import { getTableData } from './table-data';
import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export default ({ node, constants }) => {
	return (
		<>
			<ldod-table
				id="manageFragmentsTable"
				tooltip-ref
				classes="table table-bordered table-hover"
				headers={constants.headers}
				data={getTableData(node)}
				language={node.language}
				constants={constants}
				data-searchkey="externalId"></ldod-table>
			<ldod-tooltip
				data-ref="thead>tr>th[data-key='export']"
				data-tooltipkey="unselectAll"
				placement="top"
				content={node.getConstants('unselectAll')}></ldod-tooltip>
		</>
	);
};
