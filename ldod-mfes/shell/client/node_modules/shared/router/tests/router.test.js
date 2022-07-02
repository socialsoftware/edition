import Router from '../src/LdodRouter.js';

describe('testing simple router ', () => {
  let uut;
  let router;
  let nestedRouter;
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
  });

  beforeAll(async () => {
    routes = {
      '/': () => ({ mount: mocks.mountPath, unMount: mocks.unMountPath }),
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

    expect(mocks.mountPath).toBeCalledTimes(1);
    expect(uut).toEqual(['/']);

    document.body.removeChild(router);
  });

  it('when appending a router with base and the location is equal to the base it should mount the view', async () => {
    router.setAttribute('base', '/sub');
    window.location.pathname = '/sub';
    await document.body.appendChild(router);

    expect(mocks.mountSubPath).toBeCalledTimes(1);
    expect(uut).toEqual(['sub']);

    document.body.removeChild(router);
  });

  it('when appending a router with base and the location does not contains the base it should do nothing', async () => {
    router.setAttribute('base', 'sub');
    await document.body.appendChild(router);

    expect(mocks.mountSubPath).toBeCalledTimes(0);
    expect(uut).toEqual([]);

    document.body.removeChild(router);
  });

  it('when appending a router with base and the location contains the base it should mount the view', async () => {
    window.location.pathname = '/sub';
    await document.body.appendChild(router);

    expect(mocks.mountSubPath).toBeCalledTimes(1);
    expect(uut).toEqual(['sub']);

    document.body.removeChild(router);
  });

  it('when view is already active the router should do nothing', async () => {
    await document.body.appendChild(router);

    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/' } })
    );
    await new Promise((r) => setTimeout(r, 1));

    expect(mocks.mountPath).toBeCalledTimes(1);
    expect(uut).toEqual(['/']);

    document.body.removeChild(router);
  });

  it('when the location path is written in caps the router resolves it', async () => {
    router.setAttribute('base', '/sub');
    window.location.pathname = '/SUB';
    await document.body.appendChild(router);

    expect(mocks.mountSubPath).toBeCalledTimes(1);
    expect(uut).toEqual(['sub']);

    document.body.removeChild(router);
  });

  it('when the location path is unknown the router should do nothing', async () => {
    window.location.pathname = '/path4';
    await document.body.appendChild(router);

    expect(uut).toEqual([]);

    document.body.removeChild(router);
  });

  it('when navigating through routes the views are mounted and unmounted accordingly', async () => {
    await document.body.appendChild(router);

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

    await router.appendChild(nestedRouter);

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

    console.log(uut);

    document.body.removeChild(router);
  });
});
