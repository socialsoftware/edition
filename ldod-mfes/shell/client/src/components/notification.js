/** @format */

export default (content, theme) => {
	const template = document.createElement('template');
	template.innerHTML = /*html*/ `
    <ldod-notification theme="${theme}" show><div slot="toast-body">${content}</div></ldod-notification>`;
	return template.content;
};
