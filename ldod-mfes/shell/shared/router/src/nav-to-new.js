/** @format */

import { ldodEventPublisher } from './events-module';

export default class NavToNew extends HTMLAnchorElement {
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
		if (this.href) return;
		if (!this.target && this.hasTo) this.interceptBehavior();
		if (this.target) {
			this.handleMfePublishState();
			if (this.hasTo) this.href = `/ldod-mfes${this.to}`;
		}
	}

	interceptBehavior = () => {
		this.handleMfePublishState();
		this.addEventListener('click', this.onclick);
	};

	handleMfePublishState = () => {
		if (this.isMfePublished()) return;
		this.hasContent ? this.removeToAttr() : this.hide();
	};

	onclick(e) {
		e.preventDefault();
		this.emitURLEvent();
	}

	emitURLEvent() {
		if (!this.to) return;
		ldodEventPublisher('url-changed', { path: this.to });
	}

	isMfePublished = () => this.publishedMfes.includes(this.mfePath);
	hide = () => {
		this.style.display = 'none';
	};
	removeToAttr = () => this.removeAttribute('to');
}

customElements.define('nav-to-new', NavToNew, { extends: 'a' });
