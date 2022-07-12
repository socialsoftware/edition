export default class LdodRouter extends HTMLElement {
  constructor() {
    super();
  }

  get location() {
    return addSlashes(location.pathname.toLowerCase());
  }

  get base() {
    return addSlashes(this.getAttribute('base') ?? '/');
  }

  get routerPath() {
    if (this.location === this.base) return '/';
    const path = this.location.split(this.base)[1];
    return path && removeSlashes(path);
  }

  get outlet() {
    return (
      this.querySelector('ldod-outlet') || document.createElement('ldod-outlet')
    );
  }

  addBaseToPath = (path) => `${this.base}${removeSlashes(path)}`;

  isPathActive = (path) =>
    this.active && removeSlashes(this.active) === removeSlashes(path);

  isFromThisRouter = (pathname) => {
    if (!pathname) return;
    let path = addSlashes(pathname);
    return path === this.base || (path.split(this.base)[1] && true);
  };

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
    if (!isNotFound(path) && this.isPathActive(path)) return;

    this.location !== addEndSlash(path) &&
      history.pushState({}, undefined, this.addBaseToPath(path));

    this.render();
  };

  handleURLChanged = ({ detail: { path } }) => {
    this.isFromThisRouter(this.addBaseToPath(path)) && this.navigate(path);
  };

  handlePopstate = (e) => {
    this.isFromThisRouter(this.location) && this.navigate(this.location);
  };

  async render() {
    const route = this.routes?.[this.routerPath];
    console.log(this.routes);
    if (!route) {
      this.routes['not-found'] && this.navigate('not-found');
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
    const route = this.routes?.[removeSlashes(this.active)];
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

const addSlashes = (path) => addStartSlash(addEndSlash(path));

const addStartSlash = (path) => (path.startsWith('/') ? path : `/${path}`);

const addEndSlash = (path) => (path.endsWith('/') ? path : `${path}/`);

const isNotFound = (path) => removeEndSlash(path).endsWith('/not-found');

const removeEndSlash = (path) =>
  path.endsWith('/') ? path.substring(0, path.length - 1) : path;

const removeStartSlash = (path) =>
  path.startsWith('/') ? path.substring(1) : path;

const removeSlashes = (path) => removeStartSlash(removeEndSlash(path));
