import { iconSVGLoader } from './helpers';

const style = (className, icon, iconHover) => `
span[is="ldod-span-icon"].${className} {
    --icon-background: url(data:image/svg+xml,${encodeURIComponent(icon)});
}

span[is="ldod-span-icon"].${className}:hover {
    --icon-background: url(data:image/svg+xml,${encodeURIComponent(iconHover)});
}

span[is="ldod-span-icon"].${className} {
	background-image: var(--icon-background);

}
`;

const PRIMARY = '#0d6efd';
const SECONDARY = '#6c757d';

export class LdodSpanIcon extends HTMLSpanElement {
	constructor() {
		super();
		this.style.display = 'inline-block';
		this.style.backgroundRepeat = 'no-repeat';
		this.style.backgroundPosition = 'center';
		this.style.verticalAlign = 'middle';
		this.classList.add(...super.classList);
		this.styleElement = document.createElement('style');
	}

	get icon() {
		return this.getAttribute('icon');
	}
	get fill() {
		return this.getAttribute('fill') || SECONDARY;
	}
	get hoverIcon() {
		return this.getAttribute('hover-icon') || this.icon;
	}

	get hoverFill() {
		return this.getAttribute('hover-fill') || PRIMARY;
	}
	get size() {
		return this.getAttribute('size') || '24px';
	}

	setFillColor(iconSVG, color) {
		iconSVG.querySelector('path').setAttribute('fill', color);
		return iconSVG;
	}

	async connectedCallback() {
		this.style.width = this.size;
		this.style.height = this.size;
		this.style.backgroundSize = this.size;
		const className = `icon-${this.icon}-${crypto.randomUUID()}`;
		this.classList.add(className);
		const iconSVG = this.setFillColor(await iconSVGLoader(this.icon), this.fill);
		const iconHoverSVG = this.setFillColor(await iconSVGLoader(this.hoverIcon), this.hoverFill);
		this.styleElement.textContent = style(className, iconSVG.outerHTML, iconHoverSVG.outerHTML);
		if (this.getRootNode() === document) document.head.appendChild(this.styleElement);
		else this.getRootNode().appendChild(this.styleElement);
	}

	disconnectedCallback() {
		this.styleElement.remove();
	}
}

!customElements.get('ldod-span-icon') && customElements.define('ldod-span-icon', LdodSpanIcon, { extends: 'span' });
