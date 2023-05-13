/** @format */

export default (payload, lang) => {
	return /*html*/ `
    <li
        is="nav-dropdown"
        key="${payload.name}"
        language="${lang}"
        data-headers='${JSON.stringify(payload)}'
    ></li>`;
};
