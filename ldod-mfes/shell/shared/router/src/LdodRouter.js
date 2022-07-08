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

  isNotFound = (path) => path !== '/not-found';

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
    if (this.isNotFound(path) && this.active && this.isPathActive(path)) return;
    this.location !== path && history.pushState({}, undefined, path);
    this.render();
  };

  handleURLChanged = ({ detail: { path } }) => {
    this.isFromThisRouter(path) && this.navigate(path);
  };

  handlePopstate = (e) => {
    this.isFromThisRouter(this.location) && this.navigate(this.location);
  };

  async render() {
    const route = this.routes?.[this.routerPath];
    if (!route) {
      this.routes['/not-found'] && this.navigate('/not-found');
      return;
    }
    if (await isApiContractNotCompliant(route)) return;
    this.active && (await this.remove());
    const api = await route();
    api.mount(
      this.language || this.getAttribute('language'),
      `ldod-router#${this.id}>ldod-outlet`
    );
    this.active = this.routerPath;
  }

  async remove() {
    const route = this.routes?.[this.active];
    route && (await route()).unMount();
  }
}

const isApiContractNotCompliant = async (route) => {
  if (typeof route !== 'function') return true;
  const api = await route();
  if (!(api.mount && api.unMount)) return true;
  if (typeof api.mount !== 'function' || typeof api.unMount !== 'function')
    return true;
  return false;
};

customElements.define('ldod-router', LdodRouter);
