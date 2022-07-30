import {
  addEndSlash,
  addStartSlash,
  isSlash,
  PATH_REGEX,
  removeEndSlash,
} from './utils';
export default class LdodRouter extends HTMLElement {
  constructor() {
    super();
  }
  static get observedAttributes() {
    return ['language'];
  }

  get location() {
    return removeEndSlash(location.pathname.toLowerCase());
  }

  get route() {
    return addStartSlash(removeEndSlash(this.getAttribute('route'))) ?? '';
  }

  get base() {
    return addStartSlash(removeEndSlash(this.getAttribute('base'))) ?? '';
  }

  get routerPathname() {
    return removeEndSlash(`/${this.base}${this.route}`)?.replace(/\/\/+/g, '/');
  }

  get outlet() {
    return (
      this.querySelector('ldod-outlet') || document.createElement('ldod-outlet')
    );
  }

  get language() {
    return this.getAttribute('language');
  }

  set language(language) {
    this.setAttribute('language', language);
  }

  getFullPath = (path) =>
    removeEndSlash(`/${this.base}/${path ?? ''}`.replace(/\/\/+/g, '/'));

  isPathActive = (path) => {
    if (!this.active) return false;
    const transform = (pathname) =>
      isSlash(this.routerPathname)
        ? pathname
        : addEndSlash(pathname)
            .replace(`${this.routerPathname}/`, '/')
            .replace(/\/\/+/g, '/');
    let target = PATH_REGEX.exec(transform(path)).at(0);
    let current = removeEndSlash(transform(this.active.path));
    return current === target;
  };

  isFromThisRouter = (path) => {
    if (!this.route) return true;
    const target = this.base ? path.replace(this.base, '') : path;
    return PATH_REGEX.exec(target).at(0) === this.route;
  };

  async connectedCallback() {
    if (!this.routes && !this.index) return;
    this.processRoutes();
    this.append(this.outlet);
    this.addEventListeners();
    this.navigate();
  }

  processRoutes() {
    if (!this.routes) return;
    this.routes = Object.entries(this.routes).reduce((prev, [key, api]) => {
      let path = removeEndSlash(
        `/${this.base}/${this.route}/${key}`.replace(/\/\/+/g, '/')
      );
      prev[path] = api;
      return prev;
    }, {});
  }

  attributeChangedCallback(name, oldV, newV) {
    if (name === 'language' && oldV && oldV !== newV)
      this.handleLanguageChange(newV);
  }

  handleLanguageChange(language) {
    this.querySelectorAll('[language]').forEach((ele) =>
      ele.setAttribute('language', language)
    );
  }

  disconnectedCallback() {
    window.removeEventListener('ldod-url-changed', this.handleURLChanged);
    window.removeEventListener('popstate', this.handlePopstate);
  }

  addEventListeners() {
    window.addEventListener('ldod-url-changed', this.handleURLChanged);
    window.addEventListener('popstate', this.handlePopstate);
  }

  navigate = (path = this.location, state = {}) => {
    if (this.isPathActive(path)) return;
    if (this.location !== path) history.pushState(state, undefined, path);
    this.render();
  };

  handleURLChanged = ({ detail: { path, state } }) => {
    if (path && this.isFromThisRouter(path))
      this.navigate(this.getFullPath(path), state);
  };

  handlePopstate = (e) => {
    this.isFromThisRouter(this.location) && this.navigate(this.location);
  };

  async render() {
    let route = this.getRoute();
    if (await isApiContractNotCompliant(route, this.id)) return;
    if (this.active) await this.removeMFE();
    await this.appendMFE(route);
  }

  getRoute() {
    let targetPath =
      this.location === this.routerPathname
        ? this.index
        : Object.entries(this.routes).find(([path, api]) => {
            return this.location.startsWith(path) && api;
          })?.[1];

    if (!targetPath) targetPath = this.fallback;
    return targetPath;
  }

  async appendMFE(route) {
    if (!route) return;
    const api = await route();
    this.active = api;
    await api.mount(this.language, `ldod-router#${this.id}>ldod-outlet`);
  }

  async removeMFE() {
    await this.active.unMount();
    this.outlet.innerHTML = '';
  }
}

const isApiContractNotCompliant = async (route, id) => {
  if (!route) return;
  if (typeof route !== 'function')
    return complianceWaring('Exposed api must of type "function"', id);
  const api = await route();
  if (
    !(api.mount && api.unMount) ||
    !(typeof api.mount === 'function' && typeof api.unMount === 'function')
  )
    return complianceWaring(
      'the api function must expose mount and unMount functions',
      id
    );
  return false;
};

const complianceWaring = (message, id) => {
  console.error(`ldod-router#${id}: ${message}`);
  return true;
};

customElements.define('ldod-router', LdodRouter);
