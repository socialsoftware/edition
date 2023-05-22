/** @format */

import preRenderNavbarMenu from 'shared/dist/ui/nav-dropdown-ssr';
import headerData from './header-data';

export default (dom, lang) => {
	return preRenderNavbarMenu(dom, headerData, lang);
};
