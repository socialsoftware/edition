/** @format */

import './popover';
import tooltipStyle from './tooltip.css?inline';
import tooltipHtml from './tooltip-html';
import { createPopper } from './popper.js';

export class LdodTooltip extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		const sheet = new CSSStyleSheet();
		sheet.replaceSync(tooltipStyle);
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.shadowRoot.innerHTML = tooltipHtml;
	}

	get element() {
		return this.getRootNode().querySelector(this.dataset.ref);
	}

	get tooltip() {
		return this.shadowRoot.querySelector('#tooltip');
	}

	get placement() {
		return this.getAttribute('placement') ?? 'bottom';
	}

	isLightTheme() {
		return this.hasAttribute('light');
	}

	setLightTheme() {
		this.tooltip.toggleAttribute('dark', false);
		this.tooltip.toggleAttribute('light', true);
	}

	get options() {
		return {
			strategy: 'fixed',
			placement: this.placement,
			modifiers: [
				{
					name: 'offset',
					options: {
						offset: [0, 8],
					},
				},
			],
		};
	}

	get content() {
		return this.getAttribute('content') || this.dataset.content;
	}

	get instance() {
		return createPopper(this.element, this.tooltip, this.options);
	}
	static get observedAttributes() {
		return ['content'];
	}

	connectedCallback() {
		this.addEventListeners();
		this.render();
	}

	render() {
		this.isLightTheme() && this.setLightTheme();
		this.tooltip.style.maxWidth = this.getAttribute('width');
		this.tooltip.querySelector('#content').textContent = this.content;
	}

	attributeChangedCallback(name, oldV, newV) {
		if (name === 'content' && oldV && oldV !== newV) this.render();
	}

	disconnectedCallback() {
		this.removeEventListeners();
	}

	addEventListeners() {
		['mouseenter', 'focus'].forEach(event => this.element?.addEventListener(event, this.show));
		['mouseleave', 'blur'].forEach(event => this.element?.addEventListener(event, this.hide));
	}

	removeEventListeners() {
		['mouseenter', 'focus'].forEach(event => {
			this.element?.removeEventListener(event, this.show);
		});
		['mouseleave', 'blur'].forEach(event =>
			this.element?.removeEventListener(event, this.hide)
		);
	}

	show = () => {
		this.tooltip.toggleAttribute('data-show', true);
		this.instance.setOptions(options => ({
			...options,
			modifiers: [...options.modifiers, { name: 'eventListeners', enabled: true }],
		}));
		this.instance.update();
	};

	hide = () => {
		this.tooltip.toggleAttribute('data-show', false);
		this.instance.setOptions(options => ({
			...options,
			modifiers: [...options.modifiers, { name: 'eventListeners', enabled: false }],
		}));
	};
}

!customElements.get('ldod-tooltip') && customElements.define('ldod-tooltip', LdodTooltip);
