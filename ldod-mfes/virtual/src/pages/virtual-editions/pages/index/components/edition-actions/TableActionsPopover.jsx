import style from './manageStyle.css?inline';

export default (root) => {
  return () => (
    <>
      <style>{style}</style>
      <ul class="manage-menu" id="virtual-managePopover">
        <li>
          <a class="manage-item" onClick={root.onGamesModal}>
            <span class="icon-popover icon-popover-play"></span>
            <span data-virtualkey="game">{root.getConstants('game')}</span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={root.onTaxonomy}>
            <span class="icon-popover icon-popover-taxonomy"></span>
            <span data-virtualkey="taxonomy">
              {root.getConstants('taxonomy')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={root.onManualModal}>
            <span class="icon-popover icon-popover-edit"></span>
            <span data-virtualkey="manualSort">
              {root.getConstants('manualSort')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={root.onAssistModal}>
            <span class="icon-popover icon-popover-assist"></span>
            <span data-virtualkey="assisted">
              {root.getConstants('assisted')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={root.onEditorsModal}>
            <span class="icon-popover icon-popover-users"></span>
            <span data-virtualkey="editors">
              {root.getConstants('editors')}
            </span>
          </a>
        </li>
        {root.edition?.member?.admin && (
          <>
            <li>
              <a class="manage-item" onClick={root.onEditVe}>
                <span class="icon-popover icon-popover-pen"></span>
                <span data-virtualkey="edit">{root.getConstants('edit')}</span>
              </a>
            </li>
            <li>
              <a class="manage-item" onClick={root.onRemoveVE}>
                <span class="icon-popover icon-popover-trash"></span>
                <span data-virtualkey="remove">
                  {root.getConstants('remove')}
                </span>
              </a>
            </li>
          </>
        )}
      </ul>
    </>
  );
};
