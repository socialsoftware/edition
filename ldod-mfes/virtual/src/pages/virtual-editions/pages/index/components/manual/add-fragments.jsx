/** @format */

const getAddFragsModal = node =>
	node.shadowRoot.querySelector('ldod-bs-modal#virtual-add-fragments-modal');

const getLdodSearchSimple = node => getAddFragsModal(node)?.querySelector('ldod-search-simple');

const getSelectedInters = node => getLdodSearchSimple(node)?.getSelectedInters?.();

const onAdd = node => {
	const selectedInters = getSelectedInters(node);
	if (!selectedInters?.length) return;
	node.onAddFragments(selectedInters);
	getAddFragsModal(node).toggleAttribute('show');
	getLdodSearchSimple(node).replaceWith(renderLdodSearchSimple(node));
};

const renderLdodSearchSimple = node => (
	<ldod-search-simple language={node.language} fragment></ldod-search-simple>
);

export default ({ node }) => {
	return (
		<ldod-bs-modal id="virtual-add-fragments-modal" dialog-class="modal-xl">
			<h5 slot="header-slot" style={{ margin: '0' }}>
				{node.getConstants('addFrags')}
			</h5>
			<div slot="body-slot" style={{ margin: 'auto 30px' }}>
				{renderLdodSearchSimple(node)}
			</div>
			<div slot="footer-slot">
				<button type="button" class="btn btn-primary" onClick={() => onAdd(node)}>
					{node.getConstants('add')}
				</button>
			</div>
		</ldod-bs-modal>
	);
};
