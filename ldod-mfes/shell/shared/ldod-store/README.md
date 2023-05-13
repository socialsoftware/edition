# LdoD Store

## Developed with vanilla javascript

### Usage

#### Empty store

```js
import { Store } from 'path/to/shared/dist/store.js';

const store = new Store();
```

#### With some state

```js
const store = new Store({ state: 'mystate' });
```

#### Get store

```js
const state = store.getState();
```

#### Set store (partial)

```js
store.setState({ state: 'newState' });
```

#### Set store (replace)

```js
store.setState({ newState: 'replaced-state' }, true);
```

#### Set store (callback)

```js
store.setState((state) => {
  state.someState = 'newState';
});

store.setState((state) => ({ ...state, someState: 'newState' });
```

#### Add store listeners

```js
const callback = (newState, oldState) => {
  if(newState.someState !== oldState.someState) {
    ...
  }
}
const unsub = store.subscribe(callback)

unsub() // remove subscriber
```

#### Add store listeners with selector

```js
const callback = (newSomeState, oldSomeState) => {
  if(newSomeState !== olsSomeState) {
    ...
  }
}
const unsub = store.subscribe(callback, "someState")

store.destroy() // remove all subscribers (store and selector)
```

#### Add browser localstorage (partial or full) persistance

```js
const state = { one: 1, two: 2, three: 3 };
const persistedStore = new Store(state, {
  storageName: 'name',
  keys: ['one', 'two'],
});
```

Note: if a key "name" does not exist in localstorage, it is created with the keys declared ("one","two"). Otherwise, the values on localstorage are updated.
