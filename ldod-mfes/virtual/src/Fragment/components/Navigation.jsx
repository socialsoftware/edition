export default ({ node }) => {
  return (
    <div
      id="virtual-virtualNavContainer"
      style={{ display: 'flex', justifyContent: 'center' }}>
      <h5 data-virtualkey="virtualEditions" class="text-center">
        {node.getConstants('virtualEditions')}
      </h5>
      <span id="virtual-titleTooltip" class="icon-flex icon-info"></span>
      <ldod-tooltip
        data-ref="div#virtual-virtualNavContainer span#virtual-titleTooltip"
        data-virtualtooltipkey="virtualEditionsInfo"
        placement="bottom"
        light
        width="200px"
        content={node.getConstants('virtualEditionsInfo')}></ldod-tooltip>
    </div>
  );
};
