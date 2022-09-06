import { switchModeRequest } from '@src/apiRequests.js';

export default ({ node, buttonLabel, tooltipContent }) => {
  const onSwitchMode = () => {
    switchModeRequest().then((res) => {
      res && node.setMode(res.ok);
      node.querySelector('span[label]').textContent = node.getConstants(
        `${node.getMode()}Mode`
      );
    });
  };

  return (
    <div id="adminMode" class="row btn-row">
      <button
        tooltip-ref="switch-button"
        type="button"
        class="btn btn-danger ellipsis"
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
