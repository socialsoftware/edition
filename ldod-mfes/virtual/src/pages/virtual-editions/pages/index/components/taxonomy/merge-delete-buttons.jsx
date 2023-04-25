/** @format */

export const MergeButton = ({ node }) => {
	return (
		<div id="mergeCategory">
			<button
				style={{ height: '100%', width: '200px' }}
				onClick={node.onMergeCategories}
				type="button"
				inactive={node.selectedRows < 2}
				class="btn btn-sm btn-primary">
				<span is="ldod-span-icon" icon="object-group" size="16px" fill="#fff"></span>
				<span data-virtual-key="mergeCategory">{node.getConstants('merge')}</span>
			</button>
			<ldod-tooltip
				data-ref="div#mergeCategory>button"
				data-virtual-tooltip-key="mergeCategoryInfo"
				placement="top"
				width="300px"
				content={node.getConstants('mergeCategoryInfo')}></ldod-tooltip>
		</div>
	);
};

export const DeleteButton = ({ node }) => {
	return (
		<div id="deleteCategory">
			<button
				style={{ height: '100%', width: '200px' }}
				onClick={() => node.onDeleteCategories()}
				type="button"
				class="btn btn-sm btn-danger"
				inactive={!node.selectedRows}>
				<span is="ldod-span-icon" icon="trash" size="16px" fill="#fff"></span>
				<span data-virtual-key="deleteCategory">{node.getConstants('delete')}</span>
			</button>

			<ldod-tooltip
				data-ref="div#deleteCategory>button"
				data-virtual-tooltip-key="deleteCategoryInfo"
				placement="top"
				width="300px"
				content={node.getConstants('deleteCategoryInfo')}></ldod-tooltip>
		</div>
	);
};
