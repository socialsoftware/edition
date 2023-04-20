/** @format */

import { editCategory } from './taxonomy-api-requests';

export default ({ node, category }) => {
	const onEdit = e => {
		e.preventDefault();
		const body = Object.fromEntries(new FormData(e.target));
		editCategory(category?.externalId, body)
			.then(data => {
				node.updateData(data);
				node.editCategoryModal.toggleAttribute('show', false);
			})
			.catch(error => console.error(error));

		// TODO handle Error
	};

	return (
		<ldod-bs-modal id="virtual-edit-category-modal" static>
			<span slot="header-slot">
				<h4>{node.getConstants('updateCategoryName')}</h4>
			</span>
			<div slot="body-slot">
				<div style={{ padding: '20px' }}>
					<form onSubmit={onEdit}>
						<div
							id="editCategoryName"
							style={{
								display: 'flex',
								gap: '20px',
							}}>
							<div class="form-floating" style={{ width: '80%' }}>
								<input
									name="name"
									type="text"
									class="form-control"
									id="updateCatName"
									required
									placeholder="Topics"
									value={category?.name}
								/>
								<label data-virtual-key="topics">{node.getConstants('name')}</label>
							</div>
							<button type="submit" class="btn btn-primary">
								<span>{node.getConstants('submit')}</span>
							</button>
						</div>
					</form>
				</div>
			</div>
		</ldod-bs-modal>
	);
};
