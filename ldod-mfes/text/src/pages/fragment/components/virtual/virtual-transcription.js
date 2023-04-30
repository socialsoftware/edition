/** @format */

export default ({ language, xmlId, urlId }) => {
	return /*html*/ `
        <virtual-transcription
            xmlid="${xmlId}"
            urlid="${urlId || ''}"
            language="${language}"
        ></virtual-transcription>`;
};
