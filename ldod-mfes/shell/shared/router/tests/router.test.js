import '../src/LdodRouter.js';

describe('testing simple router ', () => {
  let uut;
  let router;
  let nestedRouter;
  let emptyRouter;
  let routes;
  let subRoutes;
  let mocks;

  beforeEach(async () => {
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
    };

    uut = [];
    window.location.pathname = '/';

    window.history.pushState = (data, free, url) => {
      location.pathname = url;
    };

    router = document.createElement('ldod-router');
    router.id = 'router';
    router.routes = routes;
    router.language = 'pt';

    nestedRouter = document.createElement('ldod-router');
    nestedRouter.id = 'sub-router';
    nestedRouter.routes = subRoutes;
    nestedRouter.setAttribute('language', 'pt');
    nestedRouter.setAttribute('base', 'sub');

    emptyRouter = document.createElement('ldod-router');
    emptyRouter.id = 'empry-router';
    emptyRouter.routes = {};
    emptyRouter.language = 'pt';
  });

  beforeAll(async () => {
    routes = {
      '/': () => ({
        path: '/',
        mount: mocks.mountPath,
        unMount: mocks.unMountPath,
      }),
      '/path1': () => ({
        mount: mocks.mountPath1,
        unMount: mocks.unMountPath1,
      }),
      '/path2': () => ({
        mount: mocks.mountPath2,
        unMount: mocks.unMountPath2,
      }),
      '/path3': () => ({
        mount: mocks.mountPath3,
        unMount: mocks.unMountPath3,
      }),
      '/sub': () => ({
        mount: mocks.mountSubPath,
        unMount: mocks.unMountSubPath,
      }),
      '/err': () => ({
        mountError: 'error',
        unMountError: 'error',
      }),
      '/not': () => ({
        mount: 'error',
        unMount: 'error',
      }),
      '/bad': 'bad',
    };

    subRoutes = {
      '/path1': () => ({
        mount: mocks.mountSubPath1,
        unMount: mocks.unMountSubPath1,
      }),
      '/path2': () => ({
        mount: mocks.mountSubPath2,
        unMount: mocks.unMountSubPath2,
      }),
    };
  });

  it('when appending a router without base it should mount the view correspondent to /', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(mocks.mountPath).toBeCalledTimes(1);
    expect(uut).toEqual(['/']);

    document.body.removeChild(router);
  });

  it('when appending a router with base and the location is equal to the base it should mount the "/" view', async () => {
    router.setAttribute('base', '/sub');
    window.location.pathname = '/sub';

    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(mocks.mountPath).toBeCalledTimes(1);
    expect(uut).toEqual(['/']);

    document.body.removeChild(router);
  });

  it('when appending a router with base and the location does not contains the base it should do nothing', async () => {
    router.setAttribute('base', 'sub');
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(uut).toEqual([]);
    document.body.removeChild(router);
  });

  it('when appending a router with base and the location does not contains the base and there is a not-found route it should mount it', async () => {
    router.setAttribute('base', '/sub');

    router.routes['/not-found'] = () => ({
      mount: mocks.mountNotFound,
      unMount: mocks.unMountNotFound,
    });

    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(uut).toEqual(['not-found']);
    document.body.removeChild(router);
    delete router.routes['/not-found'];
  });

  it('when view is already active the router should do nothing', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/' } })
    );
    await new Promise((r) => setTimeout(r, 1));

    expect(mocks.mountPath).toBeCalledTimes(1);
    expect(uut).toEqual(['/']);

    document.body.removeChild(router);
  });

  it('when the location path is written in caps the router resolves it', async () => {
    window.location.pathname = '/SUB';
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(uut).toEqual(['sub']);
    expect(mocks.mountSubPath).toBeCalledTimes(1);

    document.body.removeChild(router);
  });

  it('when the location path is unknown the router should do nothing or mount the not-found view', async () => {
    window.location.pathname = '/path4';
    router.routes['/not-found'] = () => ({
      mount: mocks.mountNotFound,
      unMount: mocks.unMountNotFound,
    });
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    expect(uut).toEqual(['not-found']);

    document.body.removeChild(router);
    delete router.routes['/not-found'];
  });

  it('when navigating through routes the views are mounted and unmounted accordingly', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/path1' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    expect(mocks.mountPath).toBeCalledTimes(2);
    expect(mocks.unMountPath).toBeCalledTimes(1);
    expect(mocks.mountPath1).toBeCalledTimes(1);
    expect(mocks.unMountPath1).toBeCalledTimes(1);
    expect(uut).toEqual(['/', 'unMountPath', 'path1', 'unMountPath1', '/']);

    document.body.removeChild(router);
  });

  it('simulating back or forward navigation on browser', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/path1' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/path2' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    window.location.pathname = '/sub';
    window.dispatchEvent(new CustomEvent('popstate'));

    await new Promise((r) => setTimeout(r, 1));

    expect(uut.length).toBe(7);
    expect(uut).toEqual([
      '/',
      'unMountPath',
      'path1',
      'unMountPath1',
      'path2',
      'unMountPath2',
      'sub',
    ]);
    document.body.removeChild(router);
  });

  it('appending a nested router and when navigating on it the router should not do nothing', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    await router.appendChild(nestedRouter);

    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/sub/path1/' } })
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

    expect(uut).toEqual([
      '/',
      'unMountPath',
      'sub/path1',
      'sub',
      'unMountSubPath',
      'unMountSubPath1',
      'sub',
      'sub/path2',
      'unMountSubPath',
      '/',
    ]);
    expect(uut.length).toBe(10);

    document.body.removeChild(router);
  });

  it('rendering to non compliant view the router should ignore it', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/err' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    expect(uut.length).toBe(1);
    expect(uut).toEqual(['/']);
    document.body.removeChild(router);
  });

  it('rendering to non compliant view the router should ignore it', async () => {
    await document.body.appendChild(router);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/not' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    expect(uut.length).toBe(1);
    expect(uut).toEqual(['/']);
    document.body.removeChild(router);
  });

  it('Router with no routes should be ignored', async () => {
    await document.body.appendChild(emptyRouter);
    await new Promise((r) => setTimeout(r, 1));

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/not' } })
    );

    await new Promise((r) => setTimeout(r, 1));

    expect(uut.length).toBe(0);
    expect(uut).toEqual([]);
    document.body.removeChild(emptyRouter);
  });
});
