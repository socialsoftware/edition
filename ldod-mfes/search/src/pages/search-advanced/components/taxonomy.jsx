/** @format */

export default ({ root }) => {
	return (
		<div class="form-floating">
			<input
				name="tags"
				type="search"
				class="form-control"
				placeholder="taxonomies"
				required
			/>
			<label data-search-key="taxonomy">{root.getConstants('taxonomy')}</label>
		</div>
	);
};
