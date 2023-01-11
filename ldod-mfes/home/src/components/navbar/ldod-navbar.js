import { dom, parseHTML } from '@shared/utils.js';
import { navbarHtml } from './navbar.js';
import headers from '../../../resources/navbar/headers-menus.js';
import style from './navbar.css';
import { ldodEventSubscriber } from '@shared/ldod-events.js';
import './li-dropdown-menu.js';
import './li-lang-menu.js';

const DEFAULT_SELECTED_VE = ['LdoD-Twitter', 'LdoD-Mallet'];

(await import('user').catch(e => console.error(e)))?.default.bootstrap();

window.html = String.raw;
const styleSheet = new CSSStyleSheet();
styleSheet.replaceSync(style);

const loadConstants = async lang => (await import(`../../../resources/navbar/constants-${lang}.js`)).default;

export default class LdodNavbar extends HTMLElement {
	constructor() {
		super();
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.adoptedStyleSheets = [styleSheet];
		this.toggleEvent = this.toggleEvent.bind(this);
		this.selectedVE = DEFAULT_SELECTED_VE;
	}

	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	set language(language) {
		this.setAttribute('language', language);
	}

	get token() {
		return this.getAttribute('token');
	}

	get editions() {
		return this.shadowRoot.querySelector('li#editions');
	}

	isAdmin = () => this.user && this.user.roles.includes('ROLE_ADMIN');

	connectedCallback() {
		this.setConstants().then(() => {
			this.render();
			this.addEventListeners();
			this.addDropdownEventListeners();
			this.addExpandedCollapseEvent();
		});
	}

	disconnectedCallback() {
		this.unsubSelectedVe?.();
		this.unsubLogout?.();
		this.unsubLogin?.();
	}

	subscriber = (evt, handler) => ldodEventSubscriber(evt, handler);

	addEventListeners = () => {
		this.unsubSelectedVe = this.subscriber('selected-ve', this.addSelectedVE);
		this.unsubLogout = this.subscriber('logout', this.onUserLogout);
		this.unsubLogin = this.subscriber('login', this.onUserLogin);
	};

	attributeChangedCallback(name, oldValue, newValue) {
		if (oldValue === newValue || !newValue || !oldValue) return;
		if (name === 'language') {
			this.setConstants().then(() => {
				this.componentsTextContextUpdate();
				this.componentsNameUpdate();
				this.componentsItemsUpdate();
				this.componentsLanguageUpdate();
			});
		}
	}

	componentsTextContextUpdate() {
		this.shadowRoot.querySelectorAll('.update-language').forEach(ele => (ele.textContent = this.constants[ele.id]));
	}

	componentsNameUpdate() {
		this.shadowRoot
			.querySelectorAll('[menu-name]')
			.forEach(ele => (ele.menuName = this.constants[this.getHeaders(ele.id).name]));
	}

	componentsLanguageUpdate() {
		this.shadowRoot.querySelectorAll('[language]').forEach(ele => ele.setAttribute('language', this.language));
	}

	componentsItemsUpdate() {
		this.shadowRoot
			.querySelectorAll('[is=dropdown-menu]')
			.forEach(ele => (ele.items = this.getItems(this.getHeaders(ele.id).pages)));
	}

	render() {
		const nav = dom(navbarHtml(this.constants, this.language));
		this.appendDropdownMenus(nav.querySelector('ul.navbar-nav-flex'), nav.querySelector('li.visible-xs'));
		this.shadowRoot.appendChild(nav);
		this.addEditions();
	}

	toggleEvent() {
		const element = this.shadowRoot.querySelector('.navbar-collapse');
		element.ariaExpanded = element.ariaExpanded === 'true' ? 'false' : 'true';
	}

	onUserLogin = ({ payload }) => {
		this.addExpandedCollapseEvent();
		if (!this.user) {
			this.selectedVE = payload.selectedVE;
			this.updateVE();
		}
		this.user = payload;
		this.setAdminVisibility();
	};

	onUserLogout = () => {
		this.user && this.setAdminVisibility(true);
		this.user && this.removeEditions();
		this.user = undefined;
		this.selectedVE = DEFAULT_SELECTED_VE;
		this.updateVE();
	};

	setAdminVisibility = (hide = !this.isAdmin()) => {
		const admin = this.shadowRoot.querySelector('li#admin[is=dropdown-menu]');
		if (admin) admin.setAttribute('aria-hidden', hide);
	};

	addSelectedVE = ({ payload }) => {
		this.selectedVE = payload.selected
			? [...this.selectedVE, payload.name]
			: this.selectedVE.filter(acr => acr !== payload.name);
		this.updateVE();
	};

	updateVE() {
		this.removeEditions();
		this.addEditions();
	}

	addEditions() {
		this.editions?.addSelectedEditions(this.selectedVE);
	}

	removeEditions() {
		this.editions?.removeSelectedEditions();
	}

	getHeaders(selector) {
		return headers[selector];
	}

	async setConstants() {
		this.constants = await loadConstants(this.language);
	}

	appendDropdownMenus(node, ref) {
		Object.entries(headers).forEach(([key, value]) => {
			if (!value) return;
			const { name, pages } = value;
			const dropdownMenu = parseHTML(html`<li
				is="dropdown-menu"
				id=${key}
				class="dropdown"
				language=${this.language}
				menu-name=${this.constants[name]}
				aria-hidden=${key === 'admin' && !this.isAdmin()}></li>`);
			dropdownMenu.items = this.getItems(pages);
			node.insertBefore(dropdownMenu, ref);
		});
	}

	getItems(pages) {
		return pages.filter(Boolean).map(keys => ({
			name: this.constants[keys.id] || keys.id,
			...keys,
		}));
	}

	getDropdownElements() {
		return this.shadowRoot.querySelectorAll('.dropdown');
	}

	addExpandedCollapseEvent() {
		// TODO: user component collapse
		this.shadowRoot
			.querySelectorAll('.navbar-toggle, ul.dropdown-menu a, li.dropdown.visible-xs>a.login')
			.forEach(element => {
				element.removeEventListener('click', this.toggleEvent);
				element.addEventListener('click', this.toggleEvent);
			});
	}

	addDropdownEventListeners() {
		this.getDropdownElements().forEach(drop => {
			this.bindHandlers(drop);
			drop.addEventListener('click', this.handleDropdownClick);
		});
	}

	bindHandlers(drop) {
		drop.handleClickNavbar = this.handleClickNavbar.bind(drop);
		drop.handleDocumentClick = this.handleDocumentClick.bind(drop);
		drop.removeDropdownEventListeners = this.removeDropdownEventListeners.bind(drop);
	}

	handleDropdownClick() {
		if (isOpen(this)) return close(this);

		this.getRootNode()
			.querySelectorAll('.dropdown')
			.forEach(drop => close(drop));

		this.getRootNode().addEventListener('click', this.handleClickNavbar);
		document.addEventListener('click', this.handleDocumentClick);
		open(this);
	}

	handleClickNavbar = e => {
		if (
			[...this.getRootNode().querySelectorAll('a.dropdown-toggle')].some(drop => drop === e.target) ||
			[...this.getRootNode().querySelectorAll('li.nav-lang>a')].some(lang => lang === e.target)
		)
			return;
		this.removeDropdownEventListeners();
		close(this);
	};

	handleDocumentClick = e => {
		if (e.target instanceof customElements.get('ldod-navbar')) return;
		this.removeDropdownEventListeners();
		close(this);
	};

	removeDropdownEventListeners() {
		this.getRootNode().removeEventListener('click', this.handleClickNavbar);
		document.removeEventListener('click', this.handleDocumentClick);
	}
}

customElements.define('ldod-navbar', LdodNavbar);

const isOpen = node => node.classList.contains('open');
const open = node => node.classList.add('open');
const close = node => node.classList.remove('open');
