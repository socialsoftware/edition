/** @format */

import { getTableData } from './table-data';
import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export default ({ node, constants }) => {
	return (
		<>
			<ldod-table
				id="encodedFragsTable"
				classes="table table-bordered table-hover"
				headers={constants.headers}
				data={getTableData(node)}
				data-rows="5"
				constants={constants}
				data-searchkey="externalId"></ldod-table>
		</>
	);
};
