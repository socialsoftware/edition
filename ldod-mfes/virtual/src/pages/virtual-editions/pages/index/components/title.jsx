
export default function ({ node }) {
  return (
    <div class="flex-center">
      <h3 data-virtualkey="virtualEditions" class="text-center">
        {node.getConstants('virtualEditions')}
      </h3>
      <span id="virtual-titleTooltip" class="icon-flex icon-info"></span>
      <ldod-tooltip
        data-ref="span#virtual-titleTooltip"
        data-virtualtooltipkey="virtualEditionsTitleInfo"
        placement="top"
        light
        width="250px"
        content={node.getConstants('virtualEditionsTitleInfo')}></ldod-tooltip>
    </div>
  )
}