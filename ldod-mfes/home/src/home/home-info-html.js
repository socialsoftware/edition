/** @format */

import constants from './constants/constants';
import containerCss from '../nav-bar/style/container.css?inline';
import style from './style/info.css?inline';
import common from './style/common.css?inline';

export default language => {
	return /*html*/ `   
        <style>
            ${containerCss + style + common}
        </style>
        <div class="container-md">
            <div id="info" class="bottom-info font-monospace" >
            <div class="skeleton hidden-xs"></div>
                <img loading="lazy" id="webp/logotipos.webp" class="hidden-xs" width="100%" alt="logos"/>
            <div class="skeleton visible-xs-inline"></div>
                <img loading="lazy" id="webp/logotiposm.webp" class="visible-xs-inline" width="100%" alt="logos"/>
                <br />
                <br />
                <br />
                <div data-info-key="info">
                    ${constants[language].info}
                </div>
            </div>
        </div>
        <div class="bottom-bar"></div>
    `;
};
