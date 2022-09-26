import '../src/LdodRouter.js';

let router;
let uut;
let mocks;

const routes = {
  '/path1': () => ({
    path: '/path1',
    mount: mocks.mountPath1,
    unMount: mocks.unMountPath1,
  }),
  '/path2': () => ({
    path: '/path2',
    mount: mocks.mountPath2,
    unMount: mocks.unMountPath2,
  }),

  '/sub': () => ({
    path: '/sub',
    mount: mocks.mountSubPath,
    unMount: mocks.unMountSubPath,
  }),
  '/no-function': 'bad',
  '/bad-keys': () => ({
    path: '/bad-keys',
    add: 'somethingToAdd',
    remove: 'somethingToRemove',
  }),
  '/no-key-function': () => ({
    path: '/bad-keys',
    mount: 'somethingToAdd',
    unMount: 'somethingToRemove',
  }),
  '/variable-path': () => ({
    path: '/variable-path/:id',
    mount: mocks.mountVariablePath,
    unMount: mocks.unMountVariablePath,
  }),
};

const index = () => ({
  path: '/',
  mount: mocks.mountPath,
  unMount: mocks.unMountPath,
});

const fallback = () => ({
  path: '/not-found',
  mount: mocks.mountNotFound,
  unMount: mocks.unMountNotFound,
});

const subRoutes = {
  '/path1': () => ({
    path: '/path1',
    mount: mocks.mountSubPath1,
    unMount: mocks.unMountSubPath1,
  }),
  '/path2': () => ({
    path: '/path2',
    mount: mocks.mountSubPath2,
    unMount: mocks.unMountSubPath2,
  }),
};

beforeEach(() => {
  document.body.innerHTML = '';
  router = document.createElement('ldod-router');
  router.id = 'test-router';
  router.setAttribute('language', 'pt');
  router.index = index;
  uut = [];

  history.pushState = (data, free, url) => {
    location.pathname = url;
  };

  history.replaceState = (data, unused) => {
    history.state = data;
  };

  window.onpopstate = () => {
    history.back();
  };

  mocks = {
    mountPath1: jest.fn(() => uut.push('path1')),
    unMountPath1: jest.fn(() => uut.push('unMountPath1')),
    mountPath2: jest.fn(() => uut.push('path2')),
    unMountPath2: jest.fn(() => uut.push('unMountPath2')),
    mountPath3: jest.fn(() => uut.push('path3')),
    unMountPath3: jest.fn(() => uut.push('unMountPath3')),
    mountPath: jest.fn(() => uut.push('/')),
    unMountPath: jest.fn(() => uut.push('unMountPath')),
    mountSubPath1: jest.fn(() => uut.push('sub/path1')),
    unMountSubPath1: jest.fn(() => uut.push('unMountSubPath1')),
    mountSubPath2: jest.fn(() => uut.push('sub/path2')),
    unMountSubPath2: jest.fn(() => uut.push('unMountSubPath2')),
    mountSubPath: jest.fn(() => uut.push('sub')),
    unMountSubPath: jest.fn(() => uut.push('unMountSubPath')),
    mountNotFound: jest.fn(() => uut.push('not-found')),
    unMountNotFound: jest.fn(() => uut.push('unMountNotFound')),
    mountVariablePath: jest.fn(() => uut.push('variablePath')),
    unMountVariablePath: jest.fn(() => uut.push('unMountVariablePath')),
  };
});

test('when a router with no routes is appended to dom no route is invoked', () => {
  location.pathname = '/';
  document.body.appendChild(router);
  expect(uut.length).toBe(0);
});

test('when a router with routes is appended to dom the location path route is invoked', async () => {
  location.pathname = '/';
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['/']);
  expect(mocks.mountPath).toBeCalledTimes(1);
});

test('when a router with route = /path and routes is appended to dom the on location "/" no route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('route', '/path');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(0);
});

test('when a router with route = path and routes with not-found included is appended to dom the on location "/", the not-found route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('route', '/path');
  router.routes = routes;
  router.fallback = fallback;

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(1);
  expect(uut).toEqual(['not-found']);
  expect(mocks.mountNotFound).toBeCalledTimes(1);
});

test('when a router with route "/path" and routes is appended to dom on location "/path" the index is invoked', async () => {
  location.pathname = '/path';
  router.setAttribute('route', '/path');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['/']);
  expect(mocks.mountPath).toBeCalledTimes(1);
});

test('when a router with base "/base" and routes is appended to dom the on location "/base" the index is invoked', async () => {
  location.pathname = '/base';
  router.setAttribute('base', '/base');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['/']);
  expect(mocks.mountPath).toBeCalledTimes(1);
});

test('when a router with base "/base" and routes is appended to dom the on location "/" no route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('base', '/base');
  router.routes = routes;
  document.body.appendChild(router);

  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(0);
});

test('when a router with base "/base" and routes including not-found is appended to dom the on location "/" no route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('base', '/base');
  router.fallback = fallback;
  router.routes = routes;
  document.body.appendChild(router);

  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['not-found']);
  expect(mocks.mountNotFound).toBeCalledTimes(1);
});

test('when a router with base "/base", route "/path" and routes is appended to dom the on location "/" no route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('base', '/base');
  router.setAttribute('route', '/path');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(0);
});

test('when a router with base "/base", route "/path" and routes with not-found included is appended to dom the on location "/" no route is invoked', async () => {
  location.pathname = '/';
  router.setAttribute('base', '/base');
  router.setAttribute('route', '/path');
  router.fallback = fallback;
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['not-found']);
  expect(mocks.mountNotFound).toBeCalledTimes(1);
});

test('when a router with base "/base", route "/path" and routes is appended to dom the on location "/base" no route is invoked', async () => {
  location.pathname = '/base';
  router.setAttribute('base', '/base');
  router.setAttribute('route', '/path');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(0);
});

test('when a router with base "/base", route "/path" and routes with not-found is appended to dom the on location "/base" no route is invoked', async () => {
  location.pathname = '/base';
  router.setAttribute('base', '/base');
  router.setAttribute('route', '/path');
  router.fallback = fallback;
  router.routes = routes;
  document.body.appendChild(router);

  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['not-found']);
  expect(mocks.mountNotFound).toBeCalledTimes(1);
});

test('when a router with base "/base", route "/path" and routes is appended to dom the on location "/base/path" the route "/" is invoked', async () => {
  location.pathname = '/base/path';
  router.setAttribute('base', '/base');
  router.setAttribute('route', '/path');
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(1);
  expect(uut).toEqual(['/']);
  expect(mocks.mountPath).toBeCalledTimes(1);
});

jest.setTimeout(5000000);
test('when a router with routes is appended to dom on root location the "/"  route is invoked and router reacts to url changes', async () => {
  location.pathname = '/';
  router.routes = routes;

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path2');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/sub');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/');
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(9);
  expect(uut).toEqual([
    '/',
    'unMountPath',
    'path1',
    'unMountPath1',
    'path2',
    'unMountPath2',
    'sub',
    'unMountSubPath',
    '/',
  ]);
});

test('when a router with base "/base" and routes is appended to dom on root location the "/base"  route is invoked and router reacts to url changes', async () => {
  location.pathname = '/base';
  router.setAttribute('base', '/base');
  router.routes = routes;

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path2');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/sub');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/');
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(9);
  expect(uut).toEqual([
    '/',
    'unMountPath',
    'path1',
    'unMountPath1',
    'path2',
    'unMountPath2',
    'sub',
    'unMountSubPath',
    '/',
  ]);
});

test('when a router with route "/path" and routes is appended to dom on root location the "/" route is invoked and router reacts to url changes', async () => {
  location.pathname = '/';
  router.setAttribute('route', '/path');
  router.routes = routes;

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path/path2');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path/sub');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path');
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(7);
  expect(uut).toEqual([
    'path1',
    'unMountPath1',
    'path2',
    'unMountPath2',
    'sub',
    'unMountSubPath',
    '/',
  ]);
});

test('when the attribute language change the router propagate the change', async () => {
  location.pathname = '/';
  router.routes = routes;
  router.handleLanguageChange = jest.fn(() => uut.push('language'));

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  router.setAttribute('language', 'en');
  await new Promise((r) => setTimeout(r, 1));

  expect(router.getAttribute('language')).toEqual('en');
  expect(uut.length).toBe(4);
  expect(uut).toEqual(['/', 'unMountPath', 'path1', 'language']);
});

test('when the popstate event is triggered the router reacts to it accordingly', async () => {
  location.pathname = '/';
  router.routes = routes;
  router.handleLanguageChange = jest.fn(() => uut.push('language'));

  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path1');
  await new Promise((r) => setTimeout(r, 1));

  dispatchUrlEvent('/path2');
  await new Promise((r) => setTimeout(r, 1));

  dispatchPopState();
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(7);
  expect(uut).toEqual([
    '/',
    'unMountPath',
    'path1',
    'unMountPath1',
    'path2',
    'unMountPath2',
    '/',
  ]);
});

test('when the a route do not expose a function', async () => {
  location.pathname = '/no-function';
  router.routes = routes;
  document.body.appendChild(router);
  expect(uut.length).toBe(0);
  expect(uut).toEqual([]);
});

test('when the a route do not expose the right keys names', async () => {
  location.pathname = '/bad-keys';
  router.routes = routes;
  document.body.appendChild(router);
  expect(uut.length).toBe(0);
  expect(uut).toEqual([]);
});

test('when the a route expose the right keys but not functions', async () => {
  location.pathname = '/no-key-function';
  router.routes = routes;
  document.body.appendChild(router);
  expect(uut.length).toBe(0);
  expect(uut).toEqual([]);
});

test('when appending a nested router and navigating on it the outter router should not do nothing', async () => {
  location.pathname = '/sub/path1';
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  const nestedRouter = document.createElement('ldod-router');
  nestedRouter.setAttribute('route', '/sub');
  nestedRouter.routes = subRoutes;
  nestedRouter.id = 'nested-router';
  router.outlet.appendChild(nestedRouter);
  await new Promise((r) => setTimeout(r, 1));

  window.dispatchEvent(
    new CustomEvent('ldod-url-changed', { detail: { path: '/sub/path1' } })
  );

  await new Promise((r) => setTimeout(r, 1));

  window.dispatchEvent(
    new CustomEvent('ldod-url-changed', { detail: { path: '/sub/path2' } })
  );
  await new Promise((r) => setTimeout(r, 1));

  window.dispatchEvent(
    new CustomEvent('ldod-url-changed', { detail: { path: '/' } })
  );
  await new Promise((r) => setTimeout(r, 1));
  expect(uut.length).toBe(6);

  expect(uut).toEqual([
    'sub',
    'sub/path1',
    'unMountSubPath1',
    'sub/path2',
    'unMountSubPath',
    '/',
  ]);

  document.body.removeChild(router);
});

function dispatchUrlEvent(path) {
  window.dispatchEvent(
    new CustomEvent('ldod-url-changed', { detail: { path } })
  );
}

function dispatchPopState() {
  history.pushState(null, null, '/');
  window.dispatchEvent(new Event('popstate'));
}

test('when a router with a variable path route "/variable-path/:id" the route is appended and the variable is stored on history state as key value pair object ', async () => {
  location.pathname = '/';
  router.routes = routes;
  document.body.appendChild(router);
  await new Promise((r) => setTimeout(r, 1));

  window.dispatchEvent(
    new CustomEvent('ldod-url-changed', {
      detail: { path: '/variable-path/some-id' },
    })
  );
  await new Promise((r) => setTimeout(r, 1));

  expect(uut.length).toBe(3);
  expect(uut).toEqual(['/', 'unMountPath', 'variablePath']);
});
