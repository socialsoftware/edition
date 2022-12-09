import { switchModeRequest } from '@src/api-requests.js';

export default ({ node, buttonLabel, tooltipContent }) => {
  const updateClass = () =>
    node.getMode() === 'admin'
      ? 'btn btn-danger ellipsis'
      : 'btn btn-success ellipsis';

  const onSwitchMode = () => {
    switchModeRequest().then((res) => {
      res && node.setMode(res.ok);
      const button = node.querySelector('button#user-switchModeButton');
      button.querySelector('span[label]').textContent = node.getConstants(
        `${node.getMode()}Mode`
      );
      button.setAttribute('class', updateClass());
    });
  };

  return (
    <div id="adminMode" class="row btn-row">
      <button
        id="user-switchModeButton"
        tooltip-ref="switch-button"
        type="button"
        class={updateClass()}
        onClick={onSwitchMode}>
        <span class="icon icon-edit"></span>
        <span label dynamic dynamicKey={() => `${node.getMode()}Mode`}>
          {buttonLabel}
        </span>
      </button>
      <ldod-tooltip
        placement="top"
        data-ref="[tooltip-ref='switch-button']"
        data-tooltipkey="changeLdodMode"
        content={tooltipContent}></ldod-tooltip>
    </div>
  );
};
