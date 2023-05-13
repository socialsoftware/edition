import '../src/LdodRouter.js';

let ldodRouter;

beforeEach(() => {
  ldodRouter = document.createElement('ldod-router');
  ldodRouter.id = 'test-router';
  document.body.appendChild(ldodRouter);
});

afterEach(() => {
  document.body.innerHTML = '';
});

test('when a router without base and route is created and appended to DOM', () => {
  location.pathname = '/';
  expect(ldodRouter.location).toBe('/');
  expect(ldodRouter.route).toBe('');
  expect(ldodRouter.base).toBe('');
  expect(ldodRouter.routerPathname).toBe('/');
  expect(ldodRouter.getFullPath('/test')).toBe('/test');
});

test('when a router with base and route is created and appended to DOM', () => {
  location.pathname = '/';
  ldodRouter.setAttribute('base', '/base');
  ldodRouter.setAttribute('route', '/path');

  expect(ldodRouter.location).toBe('/');
  expect(ldodRouter.route).toBe('/path');
  expect(ldodRouter.base).toBe('/base');
  expect(ldodRouter.routerPathname).toBe('/base/path');
  expect(ldodRouter.getFullPath('/path/test')).toBe('/base/path/test');
});

test('when a router with base and route is created and mounted on certain path', () => {
  location.pathname = '/base';
  ldodRouter.setAttribute('base', '/base');
  ldodRouter.setAttribute('route', '/subpath');

  expect(ldodRouter.location).toBe('/base');
  expect(ldodRouter.route).toBe('/subpath');
  expect(ldodRouter.base).toBe('/base');
  expect(ldodRouter.routerPathname).toBe('/base/subpath');
  expect(ldodRouter.getFullPath('/subpath/test')).toBe('/base/subpath/test');
});

test('no base, no route, location = /base  path = /test => fullpath = /test ', () => {
  location.pathname = '/base';

  expect(ldodRouter.location).toBe('/base');
  expect(ldodRouter.route).toBe('');
  expect(ldodRouter.base).toBe('');
  expect(ldodRouter.routerPathname).toBe('/');
  expect(ldodRouter.getFullPath('/test')).toBe('/test');
});

test('no base, route = /path, location = /base  path = /path/test => fullpath = /path/test ', () => {
  location.pathname = '/base';
  ldodRouter.setAttribute('route', '/path');

  expect(ldodRouter.location).toBe('/base');
  expect(ldodRouter.route).toBe('/path');
  expect(ldodRouter.base).toBe('');
  expect(ldodRouter.routerPathname).toBe('/path');
  expect(ldodRouter.getFullPath('/path/test')).toBe('/path/test');
});

test('no base, route = /path, location = /base  path = /path/test => fullpath = /path/test ', () => {
  location.pathname = '/base';
  ldodRouter.setAttribute('base', '/base');
  ldodRouter.setAttribute('route', '/path');

  expect(ldodRouter.location).toBe('/base');
  expect(ldodRouter.route).toBe('/path');
  expect(ldodRouter.base).toBe('/base');
  expect(ldodRouter.routerPathname).toBe('/base/path');
  expect(ldodRouter.getFullPath('/path/test')).toBe('/base/path/test');
});

test('when a router is appended on other router on /path', () => {
  location.pathname = '/';
  const otherRouter = document.createElement('ldod-router');
  otherRouter.setAttribute('route', 'path');
  ldodRouter.outlet.appendChild(otherRouter);

  expect(ldodRouter.location).toBe('/');
  expect(ldodRouter.route).toBe('');
  expect(ldodRouter.base).toBe('');
  expect(ldodRouter.routerPathname).toBe('/');
  expect(ldodRouter.getFullPath('/path')).toBe('/path');

  expect(otherRouter.location).toBe('/');
  expect(otherRouter.route).toBe('/path');
  expect(otherRouter.base).toBe('');
  expect(otherRouter.routerPathname).toBe('/path');
  expect(otherRouter.getFullPath('/path/test')).toBe('/path/test');
});

test('when a router with base with is appended on other router on /path', () => {
  location.pathname = '/base';
  ldodRouter.setAttribute('base', '/base');
  const otherRouter = document.createElement('ldod-router');
  otherRouter.setAttribute('route', '/path');
  otherRouter.setAttribute('base', '/base');
  ldodRouter.outlet.appendChild(otherRouter);

  expect(ldodRouter.location).toBe('/base');
  expect(ldodRouter.route).toBe('');
  expect(ldodRouter.base).toBe('/base');
  expect(ldodRouter.routerPathname).toBe('/base');
  expect(ldodRouter.getFullPath('/path')).toBe('/base/path');

  expect(otherRouter.location).toBe('/base');
  expect(otherRouter.route).toBe('/path');
  expect(otherRouter.base).toBe('/base');
  expect(otherRouter.routerPathname).toBe('/base/path');
  expect(otherRouter.getFullPath('/path/test')).toBe('/base/path/test');
});

test('when a router with route /path is appended on other router on /subpath', () => {
  location.pathname = '/path';
  ldodRouter.setAttribute('route', '/path');
  const otherRouter = document.createElement('ldod-router');
  otherRouter.setAttribute('route', '/subpath');
  otherRouter.setAttribute('base', '/path');
  ldodRouter.outlet.appendChild(otherRouter);

  expect(ldodRouter.location).toBe('/path');
  expect(ldodRouter.route).toBe('/path');
  expect(ldodRouter.base).toBe('');
  expect(ldodRouter.routerPathname).toBe('/path');
  expect(ldodRouter.getFullPath('/path/subpath')).toBe('/path/subpath');

  expect(otherRouter.location).toBe('/path');
  expect(otherRouter.route).toBe('/subpath');
  expect(otherRouter.base).toBe('/path');
  expect(otherRouter.routerPathname).toBe('/path/subpath');
  expect(otherRouter.getFullPath('/subpath/test')).toBe('/path/subpath/test');
});
