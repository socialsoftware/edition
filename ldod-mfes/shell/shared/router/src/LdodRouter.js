export default class LdodRouter extends HTMLElement {
  constructor() {
    super();
  }

  // location = "/some-location"
  get location() {
    return removeEndSlash(location.pathname.toLowerCase());
  }

  // base = "/some-path"
  get base() {
    const baseAttr = this.getAttribute('base');
    return baseAttr ? addStartSlash(removeEndSlash(baseAttr)) : '/';
  }

  // routerPath ="/some-path"
  get routerPath() {
    if (this.location === this.base) return '/';
    const path = this.location.split(this.base)[1];
    return path && addStartSlash(removeEndSlash(path));
  }

  get outlet() {
    return (
      this.querySelector('ldod-outlet') || document.createElement('ldod-outlet')
    );
  }

  addBaseToPath = (path) => {
    if (isSlash(this.base)) return path;
    return `${this.base}${path}`;
  };

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
    if (this.location !== path) history.pushState({}, undefined, path);
    this.render();
  };

  handleURLChanged = ({ detail: { path } }) => {
    let newPath = addStartSlash(removeEndSlash(path));
    this.isFromThisRouter(newPath) && this.navigate(newPath);
  };

  handlePopstate = (e) => {
    this.isFromThisRouter(this.location) && this.navigate(this.location);
  };
  async render() {
    const route = this.routes?.[this.routerPath];
    if (!route)
      return (
        this.routes['/not-found'] &&
        this.navigate(this.addBaseToPath('/not-found'))
      );

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
    const route = this.routes?.[removeEndSlash(this.active)];
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

const isSlash = (path) => path === '/';

const removeEndSlash = (path) =>
  isSlash(path) || !path.endsWith('/')
    ? path
    : path.substring(0, path.length - 1);

const removeStartSlash = (path) =>
  !isSlash(path) && path.startsWith('/') ? path.substring(1) : path;

const removeSlashes = (path) => removeStartSlash(removeEndSlash(path));
