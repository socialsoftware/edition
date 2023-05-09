/** @format */

customElements.define(
	'virtual-associate-tag',
	class extends HTMLButtonElement {
		constructor() {
			super();
			this.innerHTML = String.raw`<span is="ldod-span-icon" style="pointer-events: none;" icon="plus" size="14px" fill="#fff"></span>`;
			this.type = 'button';
			this.classList.add('btn', 'btn-sm', 'btn-primary');
			this.id = 'virtual--associate-tag-btn';
			this.onclick = () =>
				this.dispatchEvent(new CustomEvent('virtual:associate-tag', { bubbles: true }));
		}
	},
	{
		extends: 'button',
	}
);
