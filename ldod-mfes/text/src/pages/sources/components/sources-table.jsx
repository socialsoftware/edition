import { getTableData } from './table-data.jsx';
import.meta.env.DEV ? await import('@shared/table-dev.js') : await import('@shared/table.js');

export default ({ node, constants }) => {
	return (
		<>
			<ldod-table
				id="interSourcesTable"
				classes="table table-bordered table-hover"
				headers={constants.headers}
				data={getTableData(node)}
				constants={constants}
				data-searchkey="externalId"></ldod-table>
		</>
	);
};
