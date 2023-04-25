/** @format */

const checkbox = frag => {
	return /*html*/ `<input name="${frag.current.urlId}" id="${frag.externalId}" ${
		frag.checked ? 'checked' : ''
	} type="checkbox">`;
};

const fragmentInterPath = (xmlId, urlId) => window.references?.text?.fragmentInter?.(xmlId, urlId);

export default ({ data, type, tooltip }) => {
	return /*html*/ `
        <div class="wrapper">
            ${getTitleElement(type, tooltip)} ${data.reduce(
		(prev, entry) =>
			prev +
			/*html*/ `
            <div class="grid-container">
                ${
					entry.name
						? /*html*/ `
                <div class="caption">
                    <a is="nav-to" to="${entry.url}">${entry.name}</a>
                </div>
                `
						: ''
				} ${entry.inters.reduce((prev, frag) => prev + getRowElement(frag), '')}
            </div>
            ${entry.add ? getAddBtn(entry) : ''}`,
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
                <div>${checkbox(frag)}</div>
                <div>${getArrow('left', frag.previous)}</div>
                <div class="frag">
                    <a
                        is="nav-to"
                        to="${fragmentInterPath(frag.current.xmlId, frag.current.urlId)}"
                    >
                        ${frag.current.name}
                    </a>
                </div>
                <div>${getArrow('right', frag.next)}</div>
                <div></div>
            </div>
        `;
}

function getTooltip(tooltip) {
	return tooltip
		? /*html*/ `
        <span id="title-info" is="ldod-span-icon" icon="circle-info" size="12pt"></span>
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

function getAddBtn(entry) {
	return /*html*/ `
        <div style="text-align: center;">
            <button
                id="btn-add"
                type="button"
                data-ve-id="${entry.veId}"
                data-inter-id="${entry.interId}"
                class="btn btn-sm btn-primary"
                title="${`Add fragment interpretation to ${entry.acronym}`}"
            >
                <span is="ldod-span-icon" icon="plus" size="12px" fill="#fff" style="pointer-events: none;"></span>
            </button>
        </div>
    `;
}
