/** @format */

import '@src/select-pure.js';
import '@shared/modal.js';
import style from './style.css?inline';

const selectPure = root => root.associateTagModal.querySelector('select-pure#virtual-associateTag');

const anyTagIsEqual = (tags, tag) => tags.some(t => t === tag);

const onAutocomplete = (e, root) => {
	const nonAssignedCats = root.inter.notAssignedCategories.map(cat => cat.name);
	const assignedCats = root.inter.categories.map(cat => cat.name);

	const target = e.target;

	root.computeSelectHeight();

	const matches = nonAssignedCats.filter(cat =>
		cat.toLowerCase().startsWith(target.value.toLowerCase())
	);

	Array.from(selectPure(root).querySelectorAll('option-pure')).forEach(option => {
		if (option.hasAttribute('selected') || matches.includes(option.getAttribute('value')))
			return (option.hidden = false);
		option.hidden = true;
	});

	root.computeSelectHeight();

	if (e.key === 'Enter' && !anyTagIsEqual(matches, target.value) && root.taxonomy.openVocab) {
		selectPure(root)
			.shadowRoot.querySelectorAll('div.multi-selected-wrapper span.multi-selected')
			.forEach(span => span.remove());

		selectPure(root)
			.querySelectorAll('option-pure[selected]')
			.forEach(option => option.remove());

		!assignedCats.includes(target.value) && assignedCats.push(e.target.value);
		!root.inter.categories.map(c => c.name).includes(target.value) &&
			root.inter.categories.push({ name: target.value });

		selectPure(root).append(
			...assignedCats.map(cat => (
				<option-pure selected value={cat}>
					{cat}
				</option-pure>
			))
		);
		return;
	}
};

export default ({ root }) => {
	return (
		<ldod-modal id="virtual-associateTagModal" dialog-class="modal-xl">
			<style>{style}</style>
			<span slot="header-slot">
				<span>{root.inter.title}</span>
			</span>
			<div slot="body-slot">
				<div id="multipleSelectContainer" style={{ padding: '20px' }}>
					<select-pure id="virtual-associateTag" name="categories" multiple>
						<input
							id="select-pure-autocomplete"
							type="text"
							onKeyUp={e => onAutocomplete(e, root)}
						/>
						{root.inter.notAssignedCategories.map(cat => (
							<option-pure value={cat.name}>{cat.name}</option-pure>
						))}
						{root.inter.categories.map(cat => (
							<option-pure selected value={cat.name}>
								{cat.name}
							</option-pure>
						))}
					</select-pure>
				</div>
			</div>
			<div slot="footer-slot">
				<button type="button" class="btn btn-primary" onClick={root.onAssociateTags}>
					<span>{root.getConstants('associate')}</span>
				</button>
			</div>
		</ldod-modal>
	);
};
