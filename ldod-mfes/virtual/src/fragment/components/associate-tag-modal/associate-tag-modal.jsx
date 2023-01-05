import '@src/select-pure.js';
import '@shared/modal.js';
import style from './style.css?inline';

const selectPure = node => node.associateTagModal.querySelector('select-pure#virtual-associateTag');

const anyTagIsEqual = (tags, tag) => tags.some(t => t === tag);

const onAutocomplete = (e, node) => {
	const nonAssignedCats = node.inter.notAssignedCategories.map(cat => cat.name);
	const assignedCats = node.inter.categories.map(cat => cat.name);

	const target = e.target;

	node.computeSelectHeight();

	const matches = nonAssignedCats.filter(cat => cat.toLowerCase().startsWith(target.value.toLowerCase()));

	Array.from(selectPure(node).querySelectorAll('option-pure')).forEach(option => {
		if (option.hasAttribute('selected') || matches.includes(option.getAttribute('value')))
			return (option.hidden = false);
		option.hidden = true;
	});

	node.computeSelectHeight();

	if (e.key === 'Enter' && !anyTagIsEqual(matches, target.value) && node.taxonomy.openVocab) {
		selectPure(node)
			.shadowRoot.querySelectorAll('div.multi-selected-wrapper span.multi-selected')
			.forEach(span => span.remove());

		selectPure(node)
			.querySelectorAll('option-pure[selected]')
			.forEach(option => option.remove());

		!assignedCats.includes(target.value) && assignedCats.push(e.target.value);
		!node.inter.categories.map(c => c.name).includes(target.value) &&
			node.inter.categories.push({ name: target.value });

		selectPure(node).append(
			...assignedCats.map(cat => (
				<option-pure selected value={cat}>
					{cat}
				</option-pure>
			))
		);
		return;
	}
};

export default ({ node }) => {
	return (
		<ldod-modal id="virtual-associateTagModal" dialog-class="modal-xl">
			<style>{style}</style>
			<span slot="header-slot">
				<span>{node.inter.title}</span>
			</span>
			<div slot="body-slot">
				<div id="multipleSelectContainer" style={{ padding: '20px' }}>
					<select-pure id="virtual-associateTag" name="categories" multiple>
						<input id="select-pure-autocomplete" type="text" onKeyUp={e => onAutocomplete(e, node)} />
						{node.inter.notAssignedCategories.map(cat => (
							<option-pure value={cat.name}>{cat.name}</option-pure>
						))}
						{node.inter.categories.map(cat => (
							<option-pure selected value={cat.name}>
								{cat.name}
							</option-pure>
						))}
					</select-pure>
				</div>
			</div>
			<div slot="footer-slot">
				<button type="button" class="btn btn-primary" onClick={node.onAssociateTags}>
					<span>{node.getConstants('associate')}</span>
				</button>
			</div>
		</ldod-modal>
	);
};
