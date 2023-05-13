/** @format */
import loadingSpinnerHTML from './loading-spinner-html';

export default () => {
	return /*html*/ `
            <template shadowrootmode="open">
                ${loadingSpinnerHTML()}
            </template>
    `;
};
