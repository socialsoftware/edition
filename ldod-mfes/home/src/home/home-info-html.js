/** @format */

import constants from './constants/constants';
import containerCss from '../nav-bar/style/container.css?inline';
import style from './style/info.css?inline';

export default language => {
	return /*html*/ `   
        <style>
            ${containerCss + style}
        </style>
        <div class="container-md">
            <div id="info" class="bottom-info font-monospace" >
                <img id="logotipos" class="hidden-xs" width="100%" alt="logos"/>
                <img id="logotiposm" class="visible-xs-inline" width="100%" alt="logos"/>
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
