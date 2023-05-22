/** @format */

import references from '../../references';

try {
	await import('@ui/table.js');
} catch (error) {
	await import('@ui/table-dev.js');
}

const getTableData = (inter, root) => {
	return inter.categories.map(cat => ({
		tag: (
			<div style={{ display: 'flex', gap: '5px' }}>
				<a is="nav-to" to={references.category(cat.veAcronym, cat.name)}>
					{cat.name}
				</a>
				{cat.canBeDissociated && (
					<span
						style={{ marginLeft: '0' }}
						data-inter-id={inter.externalId}
						data-cat-id={cat.externalId}
						title={`Dissociate category from '${inter.title}' interpretation`}
						onClick={root.dissociateTag}
						class="icon icon-erase"></span>
				)}
			</div>
		),
		user: cat.users.map((user, i, arr) => (
			<>
				<a
					is="nav-to"
					to={references.user(
						user.username
					)}>{`${user.firstname} ${user.lastname} (${user.username})`}</a>
				{i !== arr.length - 1 ? ', ' : ''}
			</>
		)),
	}));
};

export default ({ root, inter }) => {
	return (
		<ldod-table
			id="virtual-interTaxonomyTable"
			classes="table  table-hover"
			headers={root.constants.taxonomyHeaders}
			data={getTableData(inter, root)}
			constants={root.constants.taxonomyHeadersIcons}></ldod-table>
	);
};
