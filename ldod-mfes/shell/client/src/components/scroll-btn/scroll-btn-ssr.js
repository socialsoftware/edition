/** @format */
import scrollBtnHtml from './scroll-btn-html';

export default () => {
	return /*html*/ `
            <template shadowrootmode="open">
                ${scrollBtnHtml()}
            </template>
    `;
};
