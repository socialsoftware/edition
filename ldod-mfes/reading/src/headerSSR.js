/** @format */

import headerSSR from '../node_modules/shared/dist/ui/nav-dropdown-ssr';
import { headerData } from './header-data';

export default (dom, lang) => {
	return headerSSR(dom, headerData, lang);
};
