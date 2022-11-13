const onCreateVEModal = () =>
  document
    .querySelector('ldod-virtual-editions ldod-modal#virtual-veCreateModal')
    .toggleAttribute('show');

export default ({ node }) => {
  return (
    <>
      <div id="virtual-createButton" class="flex-center mb-4">
        <button class="btn btn-success" onClick={onCreateVEModal}>
          <span class="icon icon-plus"></span>
          <span data-virtualkey="createVirtualEdition">
            {node.getConstants('createVirtualEdition')}
          </span>
        </button>
        <ldod-tooltip
          data-ref="#virtual-createButton button"
          data-virtualtooltipkey="createVeInfo"
          placement="top"
          width="300px"
          content={node.getConstants('createVeInfo')}></ldod-tooltip>
      </div>
    </>
  );
};