class Store {
  constructor() {
    this.listeners = new Set();
  }

  subscribe(...listeners) {
    listeners.forEach((listener) => this.listeners.add(listener));
    return () => this.listeners.clear();
  }

  notify(...args) {
    this.listeners.forEach((listener) => listener(...args));
  }
}
export default (data) => {
  let unsub;
  Object.entries(data).forEach(([key, { value, listeners }]) => {
    const store = new Store();
    let internalValue = value;
    Object.defineProperty(data, key, {
      get() {
        return internalValue;
      },
      set(newVal) {
        let oldVal = internalValue;
        internalValue = newVal;
        store.notify(oldVal, newVal);
      },
    });
    unsub = store.subscribe(...listeners);
  });
  return unsub;
};
