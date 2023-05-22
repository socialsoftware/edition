/** @format */

export default ({ root }) => {
	return (
		<ldod-table
			id="virtual-manageVeTable"
			classes="table table-bordered table-hover"
			headers={root.constants.headersManage}
			data={root.virtualEditions.map(row => ({
				externalId: row.externalId,
				data: () => ({
					acronym: row.acronym,
					title: row.title,
					editors: row.activeMembers.join(', '),
					categories: row.categories.join(', '),
					annotations: row.annotations.join(', '),
					remove: (
						<span
							id={row.externalId}
							data-acrn={row.acronym}
							class="icon icon-trash"
							onClick={root.handleRemoveVE}></span>
					),
				}),
				search: JSON.stringify(row),
			}))}
			constants={root.constants}
			language={root.language}
			data-searchkey="externalId"></ldod-table>
	);
};
