import style from './manageStyle.css?inline';

export default (node) => {
  return () => (
    <>
      <style>{style}</style>
      <ul class="manage-menu" id="virtual-managePopover">
        <li>
          <a class="manage-item" onClick={node.onGamesModal}>
            <span class="icon-popover icon-popover-play"></span>
            <span data-virtualkey="game">{node.getConstants('game')}</span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={node.onTaxonomy}>
            <span class="icon-popover icon-popover-taxonomy"></span>
            <span data-virtualkey="taxonomy">
              {node.getConstants('taxonomy')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={node.onManualModal}>
            <span class="icon-popover icon-popover-edit"></span>
            <span data-virtualkey="manualSort">
              {node.getConstants('manualSort')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={node.onAssistModal}>
            <span class="icon-popover icon-popover-assist"></span>
            <span data-virtualkey="assisted">
              {node.getConstants('assisted')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={node.onEditorsModal}>
            <span class="icon-popover icon-popover-users"></span>
            <span data-virtualkey="editors">
              {node.getConstants('editors')}
            </span>
          </a>
        </li>
        {node.edition?.member?.admin && (
          <>
            <li>
              <a class="manage-item" onClick={node.onEditVe}>
                <span class="icon-popover icon-popover-pen"></span>
                <span data-virtualkey="edit">{node.getConstants('edit')}</span>
              </a>
            </li>
            <li>
              <a class="manage-item" onClick={node.onRemoveVE}>
                <span class="icon-popover icon-popover-trash"></span>
                <span data-virtualkey="remove">
                  {node.getConstants('remove')}
                </span>
              </a>
            </li>
          </>
        )}
      </ul>
    </>
  );
};
