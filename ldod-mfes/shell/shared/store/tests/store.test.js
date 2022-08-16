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

describe('A store with some state is created', () => {
  let store;
  let state;

  beforeEach(() => {
    store = new Store({ first: 'first', second: 'second' });
    state = undefined;
  });

  it('when subscribing store without selector full store is used on callback', () => {
    const unsub = store.subscribe((curr, prev) => {
      state = { prev, curr };
    });
    store.setState({ first: 'new first' });
    expect(state).toStrictEqual({
      prev: { first: 'first', second: 'second' },
      curr: { first: 'new first', second: 'second' },
    });
  });

  it('when unsubscribing store without selector no more reactions will happen upon changing state', () => {
    const unsub = store.subscribe((curr, prev) => {
      state = { prev, curr };
    });
    unsub();
    store.setState({ first: 'new first' });
    expect(state).toBeUndefined();
  });

  it('when subscribing store with selector only the partial state is used on callback', () => {
    const cb = (currFirst, prevFirst) => {
      state = { currFirst, prevFirst };
    };
    const unsub = store.subscribe(cb, 'first');
    store.setState({ first: 'new first' });

    expect(state).toStrictEqual({ currFirst: 'new first', prevFirst: 'first' });
  });

  it('when subscribing to store with selector and another selector is changed no reaction happens', () => {
    const cb = (currFirst, prevFirst) => {
      state = { currFirst, prevFirst };
    };
    const unsub = store.subscribe(cb, 'first');
    store.setState({ second: 'new second' });

    expect(state).toBeUndefined();
  });

  it('when unsubscribing store with selector no reaction happens', () => {
    const cb = (curr, prev) => {
      state = { curr, prev };
    };
    const unsub = store.subscribe(cb, 'second');
    unsub();
    store.setState({ second: 'new second' });

    expect(state).toBeUndefined();
  });

  it('when subscribing store with multuple selectors the right selector is used on store changed', () => {
    const cb = (curr, prev) => {
      state = { ...state, curr };
    };
    const unsubOne = store.subscribe(cb, 'first');

    const unsubTwo = store.subscribe(cb, 'second');
    store.setState({ first: 'one' });
    store.setState({ second: 'two' });

    expect(state).toStrictEqual({ curr: 'two' });
  });

  it('when subscribing store with multuple selectors the right selector is used on store changed', () => {
    const cbOne = (newFirst, oldFirst) => {
      state = { ...state, newFirst };
    };

    const cbTwo = (newSecond, oldSecond) => {
      state = { ...state, newSecond };
    };
    const unsubOne = store.subscribe(cbOne, 'first');
    const unsubTwo = store.subscribe(cbTwo, 'second');
    store.setState({ first: 'one' });
    store.setState({ second: 'two' });

    expect(state).toStrictEqual({ newFirst: 'one', newSecond: 'two' });
  });

  it('when adding two subscribers without selector both are executed', () => {
    const cbOne = (curr, prev) => {
      state = { ...state, prev };
    };

    const cbTwo = (curr, prev) => {
      state = { ...state, curr };
    };
    const unsubOne = store.subscribe(cbOne);
    const unsubTwo = store.subscribe(cbTwo);
    store.setState({ first: 'one' });
    store.setState({ second: 'two' });

    expect(state).toStrictEqual({
      curr: { first: 'one', second: 'two' },
      prev: { first: 'one', second: 'second' },
    });
  });

  it('when adding two subscribers with selector both are executed', () => {
    const cbOne = (newFirst, oldFirst) => {
      state = { ...state, newFirst };
    };

    const cbTwo = (newFirst, oldFirst) => {
      state = { ...state, newFirst };
    };
    const unsubOne = store.subscribe(cbOne, 'first');
    const unsubTwo = store.subscribe(cbTwo, 'first');
    store.setState({ first: 'one' });
    store.setState({ first: 'two' });

    expect(state).toStrictEqual({ newFirst: 'two' });
  });
});
