import 'select-pure';
import 'shared/modal.js';
import style from './style.css?inline';

const selectPure = (node) =>
  node.associateTagModal.querySelector('select-pure');

const onAutocomplete = (e, node) => {
  const nonAssignedCats = node.inter.notAssignedCategories;
  const assignedCats = node.inter.categories;
  const target = e.target;
  node.computeSelectHeight();

  const matches = nonAssignedCats
    .concat(assignedCats)
    .filter((cat) =>
      cat.name.toLowerCase().startsWith(target.value.toLowerCase())
    )
    .map((cat) => cat.externalId);

  Array.from(selectPure(node).querySelectorAll('option-pure')).forEach(
    (option) => {
      if (option.hasAttribute('selected') || matches.includes(option.id))
        return (option.hidden = false);
      option.hidden = true;
    }
  );

  node.computeSelectHeight();

  if (e.key === 'Enter' && !matches.length && node.taxonomy.openVocab) {
    selectPure(node)
      .shadowRoot.querySelectorAll(
        'div.multi-selected-wrapper span.multi-selected'
      )
      .forEach((span) => span.remove());

    selectPure(node)
      .querySelectorAll('option-pure[selected]')
      .forEach((option) => option.remove());

    assignedCats.push({
      externalId: e.target.id,
      name: e.target.value,
    });
    const option = selectPure(node).append(
      ...assignedCats.map((cat) => (
        <option-pure id={cat.externalId} selected value={cat.name}>
          {cat.name}
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
          <select-pure name="categories" multiple>
            <input
              id="select-pure-autocomplete"
              type="text"
              onKeyUp={(e) => onAutocomplete(e, node)}
            />
            {node.inter.notAssignedCategories.map((cat) => (
              <option-pure id={cat.externalId} value={cat.name}>
                {cat.name}
              </option-pure>
            ))}
            {node.inter.categories.map((cat) => (
              <option-pure id={cat.externalId} selected value={cat.name}>
                {cat.name}
              </option-pure>
            ))}
          </select-pure>
        </div>
      </div>
      <div slot="footer-slot">
        <button
          type="button"
          class="btn btn-primary"
          onClick={node.onAssociateTags}>
          <span>{node.getConstants('associate')}</span>
        </button>
      </div>
    </ldod-modal>
  );
};
