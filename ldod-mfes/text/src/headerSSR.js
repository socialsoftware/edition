/** @format */

import headerSSR from '../node_modules/shared/dist/ui/nav-dropdown-ssr.js';
import { documentsMenuLinks, editionsMenuLinks } from './header-data';

export default (dom, lang) => {
	headerSSR(dom, documentsMenuLinks, lang);
	headerSSR(dom, editionsMenuLinks(), lang);
};
