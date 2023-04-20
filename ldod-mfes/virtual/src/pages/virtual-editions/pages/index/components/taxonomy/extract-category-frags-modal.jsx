/** @format */

import '@src/select-pure.js';
import { extractCategoryFragments } from './taxonomy-api-requests';

export default ({ node, category }) => {
	const onExtract = () => {
		let body = Array.from(
			node.shadowRoot.querySelector('select-pure#virtual-extractCatModal').selectedOptions
		).map(option => option.value);
		extractCategoryFragments(category?.externalId, body)
			.then(data => {
				node.updateData(data);
				node.handleCloseModal({
					detail: { id: 'virtual-extract-category-frags-modal' },
				});
			})
			.catch(error => console.error(error));

		// TODO handle Error
	};

	return (
		<ldod-bs-modal id="virtual-extract-category-frags-modal" dialog-class="modal-xl" static>
			<span slot="header-slot">
				<h4>{node.getConstants('extractFrags')}</h4>
			</span>
			<div slot="body-slot">
				<div id="multipleSelectContainer" style={{ padding: '20px' }}>
					<select-pure
						id="virtual-extractCatModal"
						name="fragments"
						multiple
						default-label={node.getConstants('multipleFrags')}>
						{category.veInters.map(inter => (
							<option-pure value={inter.externalId}>{inter.title}</option-pure>
						))}
					</select-pure>
				</div>
			</div>
			<div slot="footer-slot">
				<button type="button" class="btn btn-primary" onClick={onExtract}>
					<span>{node.getConstants('extract')}</span>
				</button>
			</div>
		</ldod-bs-modal>
	);
};
