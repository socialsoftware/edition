/** @format */

import headerSSR from '../node_modules/shared/dist/ui/nav-dropdown-ssr.js';
import headerData from './header-data';

export default (dom, lang) => {
	headerSSR(dom, headerData[0], lang);
	headerSSR(dom, headerData[1], lang);
};
