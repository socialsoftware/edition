/** @format */

const checkbox = (id, checked = false) =>
	/*html*/ `<input id="${id}" ${checked ? 'checked' : ''} type="checkbox">`;

const editionPath = acrn => window.references?.text?.edition?.(acrn);
const fragmentInterPath = (xmlId, urlId) => window.references?.text?.fragmentInter?.(xmlId, urlId);

export default ({ data, type, tooltip }) => {
	console.log(data);
	return /*html*/ `
        <div class="wrapper">
            ${getTitleElement(type, tooltip)}
            ${data.reduce(
				(prev, entry) =>
					prev +
					/*html*/ `
                    <div class="grid-container">
                        ${
							entry.name
								? /*html*/ `<div class="caption">
									<a is="nav-to" to="${editionPath(entry.acronym)}"></a>${entry.name}
								</div>`
								: ''
						}
                        
                        ${entry.inters.reduce((prev, frag) => prev + getRowElement(frag), '')}
                    </div>
                `,
				''
			)}
        </div>`;
};

function getTitleElement(type, content) {
	return /*html*/ `
    <div class="title-container">
            <h5 data-key="type">${type}</h5>
            ${getTooltip(content)}
    </div>
    `;
}
function getRowElement(frag) {
	return /*html*/ `
        <div class="grid">
            <div></div>
            <div>${checkbox(frag.externalId, frag.checked)}</div>
            <div>${getArrow('left', frag.previous)}</div>
            <div class="frag">${frag.current.name}</div>
            <div>${getArrow('right', frag.next)}</div>
    <div></div>
        </div>
        `;
}

function getTooltip(tooltip) {
	return tooltip
		? /*html*/ `
        <span id="title-info" is="ldod-span-icon" icon="circle-info" size="16pt"></span>
        <ldod-tooltip
                data-ref="span#title-info"
                data-tooltip-key="tooltip"
                placement="bottom"
                light
                width="200px"
                content="${tooltip}"
        ></ldod-tooltip>
`
		: '';
}

function getArrow(side, data) {
	return /*html*/ `
        <a is="nav-to" ${data ? `to=${fragmentInterPath(data.xmlId, data.urlId)}` : 'hidden'}>
            <span is="ldod-span-icon" icon="chevron-${side}" size="15px" fill="#0d6efd"></span>
        </a>
    `;
}
