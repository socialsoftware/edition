/** @format */

class ScrollBtn extends HTMLElement {
	constructor() {
		super();
		if (!this.shadowRoot) this.attachShadow({ mode: 'open' });
	}

	async connectedCallback() {
		if (!this.shadowRoot.innerHTML) await this.render();
		this.button = this.shadowRoot.querySelector('button');
		this.addEventListeners();
	}

	async render() {
		this.shadowRoot.innerHTML = (await import('./scroll-btn-html.js')).default();
	}

	addEventListeners() {
		window.addEventListener('scroll', () =>
			this.toggleAttribute('scrolled', window.scrollY > 750)
		);
		this.button?.addEventListener('click', onClickScroll);
	}
}

function onClickScroll() {
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

!customElements.get('scroll-btn') && customElements.define('scroll-btn', ScrollBtn);
