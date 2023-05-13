# LdoD Router

## Developed with vanilla javascript (Custom Element)

### Usage

#### Declare routes (example with lazy loading)

```js
const routes = {
  '/one-route': async () => await import('./path/to/oneRoute.js'),
  '/other-route': async () => await import('./path/to/otherRoute.js'),
  ...
};
```

Note: Each route must expose an object according:

```js
{
  path: "/path",
  mount: () => { ... },
  unMount: () => { ... }
}
```

#### Declare LdodRouter Custom Element and NavTo Anchor Element

```js
import 'path/to/shared/dist/router.js';

<ldod-router
  id="routerId"
  base="/base"
  route="/route"
  routes="{routes}"
  language="en"></ldod-router>;

<a is="nav-to" to="/route">
  Go to route
</a>;
```

### Attributes

- id (mandatory)

  - Type: String
  - eg

  ```js
  id = 'my-router';
  ```

- base (optional)

  - Description: url path where the main application is mounted
  - Type: String
  - default: "/"
  - eg

  ```js
  base = '/base';
  ```

- route (optional)

  - Description: url path where the router is mounted
  - Type: String
  - default: "/"
  - eg

  ```js
  route = '/route';
  ```

- routes

  - Type: Object { string : function }
  - eg

  ```js
  {
    "route-path": () => {
      path: "/route-path",
      mount: () => { ... },
      unMount: () => { ... }
    },
    "other-path": () => { ... }
  },
  ```

- index

  - Description: route to be served on Router route path
  - Type: function
  - eg

    ```js
     () => {
      path: "/",
      mount: () => { ... },
      unMount: () => { ... }
    }
    ```

- fallback

  - Description: route to be served on not found path
  - Type: function
  - eg

    ```js
     () => {
      path: "/not-found",
      mount: () => { ... },
      unMount: () => { ... }
    }
    ```

- shadow

  - Description: Possibility to attach shadow root to custom element
  - Type: boolean

- language (optional, LdoD application specific)
  - Description: country code alpha-2 lower case (pt, en, es)
  - Type: String
  - on change: On change language attribute the router will propagate the change to all children with language attribute: [language]
  - eg
  ```js
  language = 'pt';
  ```
