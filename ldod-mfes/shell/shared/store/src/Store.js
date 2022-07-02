import {
  objectsAreEqual,
  setPartialStorage,
  getPartialStorage,
  filterObjectByKeys,
} from './utils.js';

export class Store {
  #listeners;
  #persist;
  #state;
  constructor(state, { storageName, keys } = {}) {
    this.#state = typeof state === 'object' ? state : state ? { state } : {};
    this.#listeners = new Set();
    this.#persist = undefined;
    if (storageName && keys) {
      this.#persist = { storageName, keys };
      const storage = getPartialStorage(storageName, keys);
      storage
        ? this.setState(storage, true)
        : setPartialStorage(storageName, filterObjectByKeys(this.#state, keys));
    }
  }

  subscribe(listener) {
    this.#listeners.add(listener); //sub
    return () => this.#listeners.delete(listener); //unsub
  }

  destroy() {
    this.#listeners.clear();
  }

  getState(selector) {
    return selector ? this.#state[selector] : this.#state;
  }

  setState(state, replace) {
    if (!state) return;
    state =
      typeof state === 'object' || typeof state === 'function'
        ? state
        : { state };
    const nextState = typeof state === 'function' ? state(this.#state) : state;
    if (!objectsAreEqual(nextState, this.#state)) {
      const previousState = this.#state;
      this.#state = replace ? nextState : { ...this.#state, ...nextState };
      if (this.#persist?.storageName && this.#persist?.keys)
        setPartialStorage(
          this.#persist.storageName,
          filterObjectByKeys(this.#state, this.#persist.keys)
        );
      this.#listeners.forEach((listener) =>
        listener(this.#state, previousState)
      );
    }
  }
}
