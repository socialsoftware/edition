/** @format */

import navBarHtml from './nav-bar-html';

export default (language = 'en') => {
	return /*html*/ `
        <nav-bar language="${language}">
            <template shadowrootmode="open">
                ${navBarHtml(language)}            
            </template>
        </nav-bar> 
    `;
};
