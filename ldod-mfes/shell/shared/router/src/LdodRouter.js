export default class LdodRouter extends HTMLElement {
  constructor() {
    super();
  }

  get location() {
    const path = location.pathname;
    return path !== '/' && path.endsWith('/')
      ? path.substring(0, path.length - 1).toLowerCase()
      : path.toLowerCase();
  }

  get base() {
    return this.getAttribute('base') ?? '/';
  }

  get routerPath() {
    if (this.location === this.base) return this.location;
    const path = this.location.split(this.base)[1];
    return path && (path.startsWith('/') ? path : `/${path}`);
  }

  get outlet() {
    return (
      this.querySelector('ldod-outlet') || document.createElement('ldod-outlet')
    );
  }

  isPathActive = (path) => this.active === path;

  isFromThisRouter = (path) =>
    path && (path === this.base || (path.split(this.base)[1] && true));

  async connectedCallback() {
    this.append(this.outlet);
    this.addEventListeners();
    this.navigate(this.location);
  }

  disconnectedCallback() {
    window.removeEventListener('ldod-url-changed', this.handleURLChanged);
    window.removeEventListener('popstate', this.handlePopstate);
  }

  addEventListeners() {
    window.addEventListener('ldod-url-changed', this.handleURLChanged);
    window.addEventListener('popstate', this.handlePopstate);
  }

  navigate = (path) => {
    if (this.active && this.isPathActive(path)) return;
    this.location !== path && history.pushState({}, undefined, path);
    this.render();
  };

  handleURLChanged = ({ detail: { path } }) =>
    this.isFromThisRouter(path) && this.navigate(path);

  handlePopstate = (e) => {
    this.isFromThisRouter(this.location) && this.navigate(this.location);
  };

  async render() {
    if (this.isPathActive(this.routerPath)) return;
    this.active && this.remove();

    const route = this.routes?.[this.routerPath];
    route &&
      (await route())?.mount(
        this.language || this.getAttribute('language'),
        `ldod-router#${this.id}>ldod-outlet`
      );
    this.active = this.routerPath;
  }

  async remove() {
    const route = this.routes?.[this.active];
    route && (await route())?.unMount();
  }
}

customElements.define('ldod-router', LdodRouter);
