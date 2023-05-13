/** @format */

const ldodEventBus = globalThis.eventBus;
export class LoadingSpinner extends HTMLElement {
	constructor() {
		super();
		if (!this.shadowRoot) this.attachShadow({ mode: 'open' });
		this.pendingLoading = 0;
	}

	get isLoading() {
		return this.hasAttribute('show');
	}

	handleLoading = async isLoading => {
		if (this.pendingLoading > 1 && isLoading) return;
		if (this.pendingLoading !== 0 && !isLoading) return;
		this.toggleVisibility(isLoading);
	};

	toggleVisibility = isLoading => (this.hidden = !isLoading);

	async connectedCallback() {
		if (!this.shadowRoot.innerHTML) await this.render();
		this.addEventListeners();
	}

	async render() {
		this.shadowRoot.innerHTML = (await import('./loading-spinner-html')).default();
	}

	disconnectedCallback() {
		this.unsubLoading?.();
	}

	addEventListeners = () => {
		this.unsubLoading = ldodEventBus?.subscribe(
			'ldod:loading',
			this.handleLoadingEvent
		).unsubscribe;
	};

	handleLoadingEvent = ({ payload: isLoading }) => {
		if (isLoading) ++this.pendingLoading;
		else if (this.pendingLoading > 0) --this.pendingLoading;
		else this.pendingLoading = 0;
		this.handleLoading(isLoading);
	};
}

!customElements.get('loading-spinner') && customElements.define('loading-spinner', LoadingSpinner);
