/** @format */

import { ldodEventPublisher } from '../../ldod-event-bus/src/helpers';
import { BASE_PATH } from './ldod-router';
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

	static get observedAttributes() {
		return ['to'];
	}

	attributeChangedCallback(name, oldV, newV) {
		this.attributeChanged[name](oldV, newV);
	}

	attributeChanged = {
		to: () => !this.target && this.hasTo && this.interceptBehavior(),
	};

	connectedCallback() {
		if (this.href) return;
		if (!this.target && this.hasTo) this.interceptBehavior();
		if (this.target) {
			this.handleMfePublishState();
			if (this.hasTo) this.href = `${BASE_PATH}${this.to}`;
		}
	}

	interceptBehavior = () => {
		this.handleMfePublishState();
		this.addEventListener('click', this.onclick);
	};

	handleMfePublishState = () => {
		if (this.isMfePublished() || this.hasAttribute('force')) return;
		this.removeToAttr();
		!this.hasContent && this.hide();
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
customElements.define('nav-to', NavTo, { extends: 'a' });
export const navigateTo = (path, state) => {
	ldodEventPublisher('url-changed', { path, state });
};
