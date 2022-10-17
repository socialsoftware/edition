const getAddFragsModal = (node) =>
  node.querySelector('ldod-modal#virtual-addFragmentsModal');

const getLdodSearchSimple = (node) =>
  getAddFragsModal(node)?.querySelector('ldod-search-simple');

const getSelectedInters = (node) =>
  getLdodSearchSimple(node)?.getSelectedInters();

const onAdd = (node) => {
  const selectedInters = getSelectedInters(node);
  if (!selectedInters?.length) return;
  node.onAddFragments(selectedInters);
  getAddFragsModal(node).toggleAttribute('show');
  getLdodSearchSimple(node).replaceWith(renderLdodSearchSimple(node));
};

const renderLdodSearchSimple = (node) => (
  <ldod-search-simple language={node.language} fragment></ldod-search-simple>
);

export default ({ node }) => {
  return (
    <ldod-modal
      id="virtual-addFragmentsModal"
      dialog-class="modal-xl"
      document-overflow>
      <span slot="header-slot">{node.getConstants('addFrags')}</span>
      <div slot="body-slot" style={{ margin: 'auto 30px' }}>
        {renderLdodSearchSimple(node)}
      </div>
      <div slot="footer-slot">
        <button
          type="button"
          class="btn btn-primary"
          onClick={() => onAdd(node)}>
          {node.getConstants('add')}
        </button>
      </div>
    </ldod-modal>
  );
};
