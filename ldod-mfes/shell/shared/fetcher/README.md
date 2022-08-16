# LdoD fetcher

## Developed with vanilla javascript

### Usage

```js
import {fetcher} from 'path/to/shared/dist/fetcher.js

fetcher.get(url, {}, token)

fetcher.post(url, {data: "some-data"})

fetcher.put(url, {data: "some-data"}, token)

fetcher.delete(url)
```

### Request methods arguments

#### url

- Type: String

#### data (optional)

- Type: Object
- Details:
  - object passed is automatically stringified

#### token (optional)

- Type: String
- Details:
  - if token not passed, it will look for a token in "ldod-store" localstorage

### Response

- The response is returned in an object format (parsed through `json` function)

- On a response status 401 a `Promise.reject({message: "unauthorized"})` is returned

### Events

#### Loading state:

- On the beginning of the request a custom event `"ldod-loading"` is dispatched

```js
window.dispatchEvent(
  new CustomEvent('ldod-loading', { detail: { isLoading: true } })
);
```

- After request fetched the opposite event is dispatched

```js
window.dispatchEvent(
  new CustomEvent('ldod-loading', { detail: { isLoading: false } })
);
```

#### Error state:

- On a fetch error a `"ldod-error"` custom event is dispatched:

```js
window.dispatchEvent(
  new CustomEvent('ldod-error', {
    detail: { { message: 'Something went wrong' }},
  })
);
```

- On a response status code 403 a `"ldod-error"` custom event is dispatched:

```js
window.dispatchEvent(
  new CustomEvent('ldod-error', {
    detail: { message: 'Not authorized to access this resource' },
  })
);
```
