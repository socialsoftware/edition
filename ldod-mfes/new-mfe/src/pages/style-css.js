/** @format */

const style = /*css*/ `
	:host {
			display: block;
			padding: 25px;
			color: var(--my-element-text-color, #000);
		}
`;
const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

export default [sheet];
