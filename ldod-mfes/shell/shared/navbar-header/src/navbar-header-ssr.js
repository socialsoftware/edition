/** @format */

import { headerDataSchemaValidator } from './data-schema-validator-ssr';
import { getDropDownHTML } from './li-dropdown-html';

export default (data, lang = 'en') => {
	if (headerDataSchemaValidator(data))
		return /*html*/ `
        <li key="${data.name}" is="drop-down" language="${lang}" data-headers='${JSON.stringify(
			data
		)}'>
            ${getDropDownHTML(data, lang)}
        </li>
        `;
};
