import { ldodEventBus } from '@shared/ldod-events.js';
import style from './loading.css';
const sheet = new CSSStyleSheet();
sheet.replaceSync(style);

export class LdodLoading extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.adoptedStyleSheets = [sheet];
		this.shadowRoot.innerHTML = /*html*/ `
			<div id="shell-loadingOverlay">
				<div class="lds-dual-ring"></div>
			</div>`;
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
		this.addEventListeners();
	}

	disconnectedCallback() {
		this.unsubLoading?.();
	}

	addEventListeners = () => {
		this.unsubLoading = ldodEventBus.subscribe('ldod:loading', this.handleLoadingEvent).unsubscribe;
	};

	handleLoadingEvent = ({ payload: isLoading }) => {
		if (isLoading) ++this.pendingLoading;
		else if (this.pendingLoading > 0) --this.pendingLoading;
		else this.pendingLoading = 0;
		this.handleLoading(isLoading);
	};
}

customElements.define('ldod-loading', LdodLoading);
