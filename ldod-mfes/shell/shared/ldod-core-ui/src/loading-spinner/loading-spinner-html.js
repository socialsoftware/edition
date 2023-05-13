/** @format */
import style from './style.css';

export default () => {
	return /*html*/ `
    <style>${style}</style>
    <div id="shell-loadingOverlay">
        <div class="lds-dual-ring"></div>
    </div>`;
};
