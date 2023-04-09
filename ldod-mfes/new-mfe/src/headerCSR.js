/** @format */

import { headerData } from './header-data';

const template = document.createElement('template');
template.innerHTML = /*html*/ `<li is="drop-down" key="new-mfe" data-headers='${JSON.stringify(
	headerData
)}' language="en"></li>`;
customElements
	.whenDefined('nav-bar')
	.then(({ instance }) => instance.addHeader(template.content.firstElementChild.cloneNode(true)));
