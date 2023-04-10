/** @format */

import constants from './constants/constants';

export default root => {
	return /*html*/ `   
        <div class="container-md">
            <div id="info" class="bottom-info font-monospace" >
                <img  src="${getURL(
					`${import.meta.env.VITE_BASE}resources/webp/logotipos.webp`
				)}" class="hidden-xs" width="100%" alt="logos"/>
                <img src="${getURL(
					`${import.meta.env.VITE_BASE}resources/webp/logotiposm.webp`
				)}" class="visible-xs-inline" width="100%" alt="logos"/>
                <br />
                <br />
                <br />
                <div data-info-key="info">
                    ${constants[root.language].info}
                </div>
            </div>
        </div>
        <div class="bottom-bar"></div>
    `;
};

function getURL(path) {
	return new URL(path, import.meta.url).href;
}
