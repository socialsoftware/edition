/** @format */

export const annotationCats = root => {
	return (
		<p id="annotation-categories" class="categories-chipped">
			{root.annotation.data.tagList.map(ann => (
				<span key={ann.id}>{ann}</span>
			))}
		</p>
	);
};

export const multipleSelectHTML = root => {
	return (
		<div style={{ marginTop: '10px' }}>
			{
				<select-pure
					id="annotations-categories"
					name="annotations-categories"
					multiple
					default-label="Categories">
					{root.openVocab && (
						<input
							id="select-pure-autocomplete"
							type="text"
							onKeyUp={e => onAutocomplete(e, root)}
							placeholder={'Search or enter new tag'}
						/>
					)}
					{root.categories.map(cat => {
						return (
							<option-pure
								key={window.hash(cat)}
								selected={root.annotation.data.tagList.includes(cat)}
								value={cat}>
								{cat}
							</option-pure>
						);
					})}
				</select-pure>
			}
		</div>
	);
};

const anyTagIsEqual = (tags, tag) => tags.some(t => t === tag);

function onAutocomplete(e, root) {
	const assignedCats = root.annotation.data.tagList;
	const nonAssignedCats = root.categories.filter(cat => !assignedCats.includes(cat));
	const target = e.target;

	const matches = nonAssignedCats.filter(cat =>
		cat.toLowerCase().startsWith(target.value.toLowerCase())
	);

	Array.from(root.tagMultipleSelect.querySelectorAll('option-pure')).forEach(option => {
		if (option.hasAttribute('selected') || matches.includes(option.getAttribute('value'))) {
			option.hidden = false;
			return;
		}
		option.hidden = true;
	});

	if (e.key === 'Enter' && !anyTagIsEqual(matches, target.value) && root.openVocab) {
		root.tagMultipleSelect.shadowRoot
			.querySelectorAll('div.multi-selected-wrapper span.multi-selected')
			.forEach(span => span.remove());

		root.tagMultipleSelect
			.querySelectorAll('option-pure[selected]')
			.forEach(option => option.remove());

		!assignedCats.includes(target.value) && assignedCats.push(target.value);
		!root.categories.includes(target.value) && root.categories.push(target.value);

		root.tagMultipleSelect.append(
			...assignedCats.map(cat => (
				<option-pure key={window.hash(cat)} selected value={cat}>
					{cat}
				</option-pure>
			))
		);
	}
}
