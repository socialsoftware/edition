/** @format */

import { iconSVGLoader } from './helpers';
import style from './ldod-span-icon-style.css?inline';

const iconStyle = (icon, iconHover, size) => /*css*/ `
:host {
    --icon-background: url(data:image/svg+xml,${encodeURIComponent(icon)});
	background-image: var(--icon-background);
	min-width: ${size};
	min-height: ${size};
	background-size: ${size};
}
:host(:hover) {
    --icon-background: url(data:image/svg+xml,${encodeURIComponent(iconHover)});
}
`;

export class LdodSpanIcon extends HTMLSpanElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.sheet = new CSSStyleSheet();
		this.sheet.replaceSync(style);
		this.shadowRoot.adoptedStyleSheets = [this.sheet];
	}

	get icon() {
		return this.getAttribute('icon');
	}
	get fill() {
		return this.getAttribute('fill') || '#6c757d';
	}
	get hoverIcon() {
		return this.getAttribute('hover-icon') || this.icon;
	}

	get hoverFill() {
		return this.getAttribute('hover-fill') || this.fill;
	}
	get size() {
		return this.getAttribute('size') || '24px';
	}

	setFillColor(iconSVG, color) {
		iconSVG.querySelector('path').setAttribute('fill', color);
		return iconSVG;
	}

	async connectedCallback() {
		const iconSVG = this.setFillColor(await iconSVGLoader(this.icon), this.fill);
		const iconHoverSVG = this.setFillColor(await iconSVGLoader(this.hoverIcon), this.hoverFill);
		this.sheet.replaceSync(
			style + iconStyle(iconSVG.outerHTML, iconHoverSVG.outerHTML, this.size)
		);
	}
}

!customElements.get('ldod-span-icon') &&
	customElements.define('ldod-span-icon', LdodSpanIcon, { extends: 'span' });
