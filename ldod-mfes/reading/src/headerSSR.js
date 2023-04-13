/** @format */

import headerSSR from '../node_modules/shared/dist/dropdown/navbar-header-ssr';
import { headerData } from './header-data';

export default (dom, lang) => {
	return headerSSR(dom, headerData, lang);
};
