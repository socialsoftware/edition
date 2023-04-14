/** @format */

import navBarHtml from './nav-bar-html';
import { parse } from 'node-html-parser';

export default async (dom, language = 'en') => {
	const rawDrops = dom.querySelector('li[is="drop.down"]:not(li[key="admin"])')?.outerHTML || '';
	const rawNavbar = /*html*/ `
        <nav-bar language="${language}">
            <template shadowrootmode="open">
                ${navBarHtml(language, rawDrops)}            
            </template>
        </nav-bar> 
    `;
	const body = dom.querySelector('body');
	const newNavbar = parse(rawNavbar);
	body.querySelector('nav-bar')?.remove();
	body.appendChild(newNavbar);
};

export function cleanUpNavbar(dom) {
	dom.querySelector('nav-bar').remove();
}
