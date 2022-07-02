/**
 * @jest-environment jsdom
 */
import { Store } from '../src/Store.js';

describe('Store class instantiation with no args', () => {
  let store = new Store();
  const state = store.getState();

  it('store is created with empty state and no storage', () => {
    expect(state).toEqual({});
  });

  it('setstate with no args nothing change', () => {
    store.setState();
    expect(state).toEqual({});
  });
});

describe('Store class instantiation unpersisted with object as state', () => {
  let store;

  beforeEach(() => {
    store = new Store({ someState: 'state' });
  });

  it('a store is created and the state is stored', () => {
    expect(store.getState()).toStrictEqual({ someState: 'state' });
  });

  it('a non persistance store is created and the local Storage does not contain it', () => {
    expect(localStorage.length).toBe(0);
  });

  it('the state is changed passing an object', () => {
    store.setState({ someState: 'newState' });
    expect(store.getState()).toStrictEqual({ someState: 'newState' });
  });

  it('the state is changed passing an object', () => {
    const state = { someState: 'someState' };
    store.setState(state);
    expect(store.getState()).toStrictEqual(state);
  });

  it('the state is changed passing a function changing prop', () => {
    store.setState((state) => {
      state.someState = 'newState';
    });
    expect(store.getState()).toStrictEqual({ someState: 'newState' });
  });

  it('the state is changed passing a function passing new object', () => {
    store.setState((state) => ({ ...state, someState: 'newState' }));
    expect(store.getState()).toStrictEqual({ someState: 'newState' });
  });

  it('set state by adding new values to the object', () => {
    store.setState({ newState: 'newState' });
    const ut = Object.entries(store.getState());
    expect(ut.length).toBe(2);
    expect(ut).toStrictEqual([
      ['someState', 'state'],
      ['newState', 'newState'],
    ]);
  });

  it('set state by adding new value through anonymous function creating prop', () => {
    store.setState((state) => {
      state.newState = 'newState';
    });
    const ut = Object.entries(store.getState());
    expect(ut.length).toBe(2);
    expect(ut).toStrictEqual([
      ['someState', 'state'],
      ['newState', 'newState'],
    ]);
  });

  it('set state by adding new value through anonymous function returning new object', () => {
    store.setState((state) => ({ ...state, newState: 'newState' }));
    const ut = Object.entries(store.getState());
    expect(ut.length).toBe(2);
    expect(ut).toStrictEqual([
      ['someState', 'state'],
      ['newState', 'newState'],
    ]);
  });

  it('set state by replacing it', () => {
    const state = { key: 'value', other: 'otherValue' };
    store.setState(state, true);
    expect(store.getState()).toStrictEqual(state);
  });

  it('set state by adding new key values', () => {
    const state = { key: 'value', other: 'otherValue' };
    store.setState(state);
    expect(store.getState()).toStrictEqual({ someState: 'state', ...state });
  });

  it('set state that differs from the previous and the listeners are invoked', () => {
    const state = { someState: 'otherValue' };
    const mockFn = jest.fn();
    store.subscribe(mockFn);
    store.setState(state);
    expect(mockFn).toBeCalledTimes(1);
  });

  it('set state that differs from the previous and the listeners is unsubscribed', () => {
    const state = { someState: 'otherValue' };
    const mockFn = jest.fn();
    const unsub = store.subscribe(mockFn);
    unsub();
    store.setState(state);
    expect(mockFn).toBeCalledTimes(0);
  });

  it('set state that differs from the previous and the listeners are destroyed', () => {
    const state = { someState: 'otherValue' };
    const mockFn = jest.fn();
    const unsub = store.subscribe(mockFn);
    store.destroy();
    store.setState(state);
    expect(mockFn).toBeCalledTimes(0);
  });

  it('set state that do not differs from the previous', () => {
    const state = { someState: 'state' };
    const mockFn = jest.fn();
    const unsub = store.subscribe(mockFn);
    store.setState(state);
    expect(mockFn).toBeCalledTimes(0);
  });
});

describe('Store class instantiation unpersisted with variable as state', () => {
  let store;
  beforeEach(() => {
    store = new Store(2);
  });

  it('a store is created and the state is stored', () => {
    expect(store.getState()).toStrictEqual({ state: 2 });
  });

  it('a non persistance store is created and the local Storage does not contain it', () => {
    expect(localStorage.length).toBe(0);
  });

  it('the state is changed passing an object', () => {
    store.setState(3);
    expect(store.getState()).toStrictEqual({ state: 3 });
  });

  it('incrementing previous state', () => {
    store.setState(({ state }) => ({ state: (state += 1) }));
    expect(store.getState('state')).toBe(3);
  });
});

describe('Store class instantiation persisted', () => {
  let persistedStore;
  let emptyPersistedStore;
  beforeEach(() => {
    localStorage.clear();
    persistedStore = new Store(
      { someState: 'state' },
      { storageName: 'persisted', keys: ['someState'] }
    );
    emptyPersistedStore = new Store(
      {},
      { storageName: 'persisted', keys: ['someState'] }
    );
  });

  it('a persisted store is created and the state is stored', () => {
    expect(persistedStore.getState()).toStrictEqual({ someState: 'state' });
  });

  it('a store is created with local storage persistance', () => {
    expect(localStorage.length).toBe(1);
    expect(localStorage.getItem('persisted')).toBeDefined();
    expect(JSON.parse(localStorage.getItem('persisted'))).toStrictEqual({
      someState: 'state',
    });
  });

  it('a empty persisted store is created and retrieves the state of the localstorage', () => {
    expect(emptyPersistedStore.getState()).toStrictEqual({
      someState: 'state',
    });
  });
});

describe('Store class instantiation persisted with key object values', () => {
  let persistedStore;
  const state = { token: true, user: { name: 'test', admin: true } };
  beforeEach(() => {
    localStorage.clear();

    persistedStore = new Store(state, {
      storageName: 'persisted',
      keys: ['token', 'user'],
    });
  });

  it('a persisted store is created and the state is stored', () => {
    expect(persistedStore.getState()).toStrictEqual(state);
  });

  it('a store is created with local storage persistance', () => {
    expect(localStorage.length).toBe(1);
    expect(localStorage.getItem('persisted')).toBeDefined();
    expect(JSON.parse(localStorage.getItem('persisted'))).toStrictEqual(state);
  });

  it('setting partial state propagates to localstorage', () => {
    persistedStore.setState({ user: undefined });
    const result = JSON.parse(localStorage.getItem('persisted'));
    expect(result.user).toStrictEqual(undefined);
  });
});
