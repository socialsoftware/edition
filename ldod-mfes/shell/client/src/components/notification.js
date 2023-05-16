/** @format */

export default (content, theme) => {
	const template = document.createElement('template');
	if (content) {
		template.innerHTML = /*html*/ `
		<ldod-notification theme="${theme}" show><div></div></ldod-notification>`;
		template.content.querySelector('div').innerText = content;
	}
	return template.content;
};
