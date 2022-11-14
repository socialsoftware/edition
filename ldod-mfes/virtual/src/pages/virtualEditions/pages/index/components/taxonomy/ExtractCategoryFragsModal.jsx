import 'shared/select-pure.js';
import { extractCategoryFragments } from './taxonomyApiRequests';

export default ({ node, category }) => {
  const onExtract = () => {
    let body = Array.from(
      node.querySelector('select-pure').selectedOptions
    ).map((option) => option.value);
    extractCategoryFragments(category?.externalId, body)
      .then((data) => {
        node.updateData(data);
        node.handleCloseModal({
          detail: { id: 'virtual-extractCategoryFragsModal' },
        });
      })
      .catch((error) => console.error(error));

    // TODO handle Error
  };

  return (
    <ldod-modal id="virtual-extractCategoryFragsModal" dialog-class="modal-xl">
      <span slot="header-slot">
        <span>{node.getConstants('extractFrags')}</span>
      </span>
      <div slot="body-slot">
        <div id="multipleSelectContainer" style={{ padding: '20px' }}>
          <select-pure
            id="virtual-extractCatModal"
            name="fragments"
            multiple
            default-label={node.getConstants('multipleFrags')}>
            {category.veInters.map((inter) => (
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
    </ldod-modal>
  );
};
