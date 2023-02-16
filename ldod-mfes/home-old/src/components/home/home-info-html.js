const getURL = path =>
	new URL(import.meta.url.includes('resources') ? `../${path}` : `resources/home/${path}`, import.meta.url).href;

export default root => {
	return /*html*/ `   
<div class="container">
    <div id="info" class="bottom-info font-monospace update-language">
        <img  src="${getURL('webp/logotipos.webp')}" class="hidden-xs" width="100%" alt="logos"/>
        <img src="${getURL('webp/logotiposm.webp')}" class="visible-xs-inline" width="100%" alt="logos"/>
        <br />
        <br />
        <br />
        ${root.constants.info}
    </div>
</div>
<div class="bottom-bar"></div>
    `;
};
