/** @format */

import headerSSR from '../node_modules/shared/dist/dropdown/navbar-header-ssr';
import { headerData } from './header-data';

export default () => {
	return headerSSR(headerData[0], 'en') + headerSSR(headerData[1], 'en');
};
