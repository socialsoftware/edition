/** @format */

import headerSSR from 'shared/dropdown/navbar-header-ssr';
import headerData from './header-data';

export default (dom, lang) => {
	return headerSSR(dom, headerData, lang);
};
