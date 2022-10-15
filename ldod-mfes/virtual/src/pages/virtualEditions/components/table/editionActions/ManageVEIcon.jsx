const onManageVirtualEdition = (node, edition, target) => {
  node.edition = node.virtualEditions.find(
    ({ externalId }) => externalId === edition.externalId
  );
  node.querySelector('ldod-popover').target = target;
  node.querySelector('ldod-popover').toggleAttribute('show');
};

export default ({ node, edition }) => {
  return (
    <>
      <span
        open-popover
        id="action_2"
        class="icon icon-edit"
        onClick={({ target }) =>
          onManageVirtualEdition(node, edition, target)
        }></span>
      <ldod-tooltip
        data-ref={`tr[id="${edition.externalId}"] #action_2`}
        data-virtualtooltipkey={`action_2`}
        placement="right"
        content={node.getConstants(`action_2`)}></ldod-tooltip>
    </>
  );
};
