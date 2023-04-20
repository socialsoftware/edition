/** @format */

import navBarHtml from './nav-bar-html';
import { parse } from 'node-html-parser';

export default async (dom, language = 'en') => {
	const rawNavbar = /*html*/ `
        <nav-bar language="${language}">
            <template shadowrootmode="open">
                ${await navBarHtml(language)}
            </template>
        </nav-bar> 
    `;
	const body = dom.querySelector('body');
	const newNavbar = parse(rawNavbar);
	body.querySelector('nav-bar')?.remove();
	body.appendChild(newNavbar);
};
