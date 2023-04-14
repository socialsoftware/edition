/** @format */

import headerSSR, { cleanUp } from '../node_modules/shared/dist/dropdown/navbar-header-ssr';
import { headerData } from './header-data';

export default (dom, lang) => {
	return headerSSR(dom, headerData, lang);
};

export function cleanUpHeader(dom) {
	cleanUp(dom, 'reading');
}
