/** @format */

import { ldodEventPublisher } from './events-module';

export default class NavTo extends HTMLAnchorElement {
	get to() {
		const toAttr = this.getAttribute('to');
		return typeof toAttr === 'string' ? toAttr.trim() : '';
	}

	get mfePath() {
		return this.to === '/' ? this.to : this.to?.split('/')[1];
	}

	get hasTo() {
		return this.hasAttribute('to');
	}

	get hasContent() {
		return this.hasAttribute('content');
	}

	get publishedMfes() {
		return [...(window.mfes ?? []), '/'];
	}

	connectedCallback() {
		this.checkIfMfesIsPublished();
		if (this.target) {
			this.href = this.to;
			return;
		}
		this.addEventListener('click', this.onclick);
	}

	onclick(e) {
		e.preventDefault();
		this.emitURLEvent();
	}

	emitURLEvent() {
		if (!this.to) return;
		ldodEventPublisher('url-changed', { path: this.to });
	}

	checkIfMfesIsPublished = () => {
		if (this.target || !this.hasTo) return;
		if (!this.publishedMfes.includes(this.mfePath)) {
			if (this.hasContent) return this.setAttribute('to', '');
			this.style.display = 'none';
		}
	};
}

customElements.define('nav-to', NavTo, { extends: 'a' });
