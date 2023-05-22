/** @format */

import headerSSR from 'shared/dist/ui/nav-dropdown-ssr.js';
import { headerData } from './header-data.js';

export default (dom, lang) => {
	headerSSR(dom, headerData, lang);
};
