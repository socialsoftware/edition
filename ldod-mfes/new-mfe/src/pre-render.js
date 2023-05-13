/** @format */

import preRender from 'shared/ui/nav-dropdown-ssr.js';
import { headerData } from './references';

export default function (dom, lang) {
	preRender(dom, headerData, lang);
}
