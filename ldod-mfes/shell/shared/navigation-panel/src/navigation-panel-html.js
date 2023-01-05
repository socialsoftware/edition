const getTooltip = tooltip => /*html*/ `
<span id="title-info" is="ldod-span-icon" icon="circle-info" size="16pt" class="icon-flex"></span>
            <ldod-tooltip 
                data-ref="span#title-info" 
                data-tooltip-key="tooltipContent" 
                placement="bottom" 
                light 
                width="200px"
                content="${tooltip}"></ldod-tooltip>
        </div>
`;

export default (title, tooltip) => {
	return /*html*/ `
        <div class="title-container">
            <h5 data-key="title">${title}</h5>
            ${tooltip && getTooltip(tooltip)} 
        `;
};
