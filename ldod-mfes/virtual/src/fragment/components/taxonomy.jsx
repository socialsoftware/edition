import { virtualReferences } from '../../references';

try {
	await import('@shared/table.js');
} catch (error) {
	await import('@shared/table-dev.js');
}

const getTableData = (inter, node) => {
	return inter.categories.map(cat => ({
		tag: (
			<div style={{ display: 'flex', gap: '5px' }}>
				<a is="nav-to" to={virtualReferences.category(cat.veAcronym, cat.name)}>
					{cat.name}
				</a>
				{cat.canBeDissociated && (
					<span
						style={{ marginLeft: '0' }}
						data-inter-id={inter.externalId}
						data-cat-id={cat.externalId}
						title={`Dissociate category from '${inter.title}' interpretation`}
						onClick={node.dissociateTag}
						class="icon icon-erase"></span>
				)}
			</div>
		),
		user: cat.users.map((user, i, arr) => (
			<>
				<a
					is="nav-to"
					to={virtualReferences.user(
						user.username
					)}>{`${user.firstname} ${user.lastname} (${user.username})`}</a>
				{i !== arr.length - 1 ? ', ' : ''}
			</>
		)),
	}));
};

export default ({ node, inter }) => {
	return (
		<ldod-table
			id="virtual-interTaxonomyTable"
			classes="table  table-hover"
			headers={node.constants.taxonomyHeaders}
			data={getTableData(inter, node)}
			constants={node.constants.taxonomyHeadersIcons}></ldod-table>
	);
};
