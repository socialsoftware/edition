/** @format */

import headerSSR, { cleanUp } from '../node_modules/shared/dist/dropdown/navbar-header-ssr';
import headerData from './header-data';

export default (dom, lang) => {
	headerSSR(dom, headerData[0], lang);
	headerSSR(dom, headerData[1], lang);
};
