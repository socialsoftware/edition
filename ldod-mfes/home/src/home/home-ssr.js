/** @format */
import ldodHomeHtml from './ldod-home-html';
import ldodInfoHtml from './home-info-html';

export default (element, language) => {
	console.log(element);
	element.innerHTML = /*html*/ `
        <ldod-home language="${language}">
            <template shadowrootmode="open">
                ${ldodHomeHtml(language)}
            </template>
        </ldod-home>
        <home-info language="${language}">
            <template shadowrootmode="open">
                ${ldodInfoHtml(language)}
            </template>
        </home-info>   
    `;
};
