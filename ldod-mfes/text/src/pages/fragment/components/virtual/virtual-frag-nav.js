/** @format */

import { isVirtualInter } from '../../utils';

export default ({ language, xmlId, urlId }) => {
	return /*html*/ `
		<virtual-frag-nav
			language="${language}"
			fragment="${xmlId}"
			${isVirtualInter(urlId) ? `urlid="${urlId}"` : ''}				
		></virtual-frag-nav>`;
};
