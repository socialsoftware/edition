/** @format */

import headerSSR from '../node_modules/shared/dist/ui/nav-dropdown-ssr';
import headerData from './header-data';

export default (dom, lang) => headerSSR(dom, headerData, lang);
