/** @format */

import '@src/select-pure.js';
import '@ui/modal-bs.js';
import style from './style.css?inline';

const selectPure = root => root.associateTagModal.querySelector('select-pure#virtual-associateTag');

const anyTagIsEqual = (tags, tag) => tags.some(t => t === tag);

const onAutocomplete = (e, root, inter) => {
	const nonAssignedCats = inter.notAssignedCategories.map(cat => cat.name);
	const assignedCats = inter.categories.map(cat => cat.name);

	const target = e.target;

	const matches = nonAssignedCats.filter(cat =>
		cat.toLowerCase().startsWith(target.value.toLowerCase())
	);

	Array.from(selectPure(root).querySelectorAll('option-pure')).forEach(option => {
		if (option.hasAttribute('selected') || matches.includes(option.getAttribute('value')))
			return (option.hidden = false);
		option.hidden = true;
	});

	if (e.key === 'Enter' && !anyTagIsEqual(matches, target.value) && root.taxonomy.openVocab) {
		selectPure(root)
			.shadowRoot.querySelectorAll('div.multi-selected-wrapper span.multi-selected')
			.forEach(span => span.remove());

		selectPure(root)
			.querySelectorAll('option-pure[selected]')
			.forEach(option => option.remove());

		!assignedCats.includes(target.value) && assignedCats.push(e.target.value);
		!inter.categories.map(c => c.name).includes(target.value) &&
			inter.categories.push({ name: target.value });

		selectPure(root).append(
			...assignedCats.map(cat => (
				<option-pure selected value={cat}>
					{cat}
				</option-pure>
			))
		);
	}
};

export default ({ root, inter }) => {
	return (
		<ldod-bs-modal id="virtual--associate-tag-modal" dialog-class="modal-xl">
			<style>{style}</style>
			<span slot="header-slot">
				<span>{inter.title}</span>
			</span>
			<div slot="body-slot">
				<div id="multipleSelectContainer" style={{ padding: '20px' }}>
					<select-pure id="virtual-associateTag" name="categories" multiple>
						<input
							id="select-pure-autocomplete"
							type="text"
							placeholder={root.getConstants('newCategory')}
							onKeyUp={e => onAutocomplete(e, root, inter)}
						/>
						{inter.notAssignedCategories.map(cat => (
							<option-pure value={cat.name}>{cat.name}</option-pure>
						))}
						{inter.categories.map(cat => (
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
		</ldod-bs-modal>
	);
};
