/** @format */

const getFragmentRemove = (node, id) => {
	return (
		<div
			id={`remove-container-${id}`}
			class="text-center"
			tooltip-ref={`remove-container-${id}`}>
			<span
				id={`xmark-icon-${id}`}
				data-id={id}
				class="icon icon-xmark"
				onClick={node.handleRemoveFragment}
			/>
			<ldod-tooltip
				data-ref={`div#remove-container-${id}`}
				data-tooltipkey="removeFragment"
				placement="top"
				content={node.getConstants('removeFragment')}></ldod-tooltip>
		</div>
	);
};

const getExport = (node, id) => {
	const handleSelection = ({ target }) => {
		const trId = target.dataset.id;
		node.querySelector(`table#manageFragmentsTable>tbody>tr[id="${trId}"]`).toggleAttribute(
			'selected'
		);
		const element = node.exportSelectedElement;
		element.body = element.body.some(id => id === trId)
			? element.body.filter(id => id !== trId)
			: [...element.body, trId];
	};

	return (
		<div
			id={`export-container-${id}`}
			class="text-center"
			tooltip-ref={`export-container-${id}`}
			onClick={handleSelection}>
			<span id={`dark-export-icon-${id}`} data-id={id} class="icon icon-dark-export" />
			<span hidden id={`checked-icon-${id}`} data-id={id} class="icon icon-checked" />
			<ldod-tooltip
				data-ref={`div#export-container-${id}`}
				data-tooltipkey="select"
				placement="top"
				content={node.getConstants('select')}></ldod-tooltip>
		</div>
	);
};

export const getTableData = node => {
	const data = node.fragments?.map(({ externalId, title, xmlId }) => {
		return {
			externalId,
			title,
			xmlId,
			remove: getFragmentRemove(node, externalId),
			export: getExport(node, externalId),
		};
	});

	return data?.map(entry => {
		return {
			...entry,
			search: Object.values(entry).reduce((prev, curr) => {
				return prev.concat(curr.toString(), ',');
			}, ''),
		};
	});
};
