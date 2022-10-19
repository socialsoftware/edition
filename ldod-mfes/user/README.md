# LdoD User Microfrontend (MFE)

## Developed with [Vite](https://vitejs.dev/) and vanilla javascript

### Purpose

- Allow to perform user authentication on the LdoD edition server.
- Logout
- Password management.

### Usage

#### import MFE:

```js
import 'user'; //through Shell MFE importmaps
import '<HOST_PATH>/ldod-mfes/user/user.js';
```

#### MFE exposed API:

```js
{
  path: '/user', // mount path
  mount: async (language: String, selector: String) => {...}, // append user MFE on the NODE given by `selector`
  unMount: async () => {...}, // remove user MFE from the DOM
}
```

#### Authentication Component (automatically loaded during import):

Instantion:

```js
const userAuth = new UserComponent(language: String | optional);
```

or

`<li is="user-component" language="pt" id="user-component-1"></li>`

- Attributes:

  - language
    - Type: String
    - Optional
    - Default value: "en"
    - Alternatives: "pt","es"

- Events:

  - on authentication failed:

    - event name: `ldod-error`
    - payload:

    ```js
    {
      detail: {
        message: String;
      }
    }
    ```

    - emiter: Bubbled on the DOM tree;

  - on authentication success:

    - event name: `ldod-token`
    - payload:

    ```js
    {
      detail: {
        token: String;
      }
    }
    ```

    - emiter: Bubbled on the DOM tree;

  - on login:

    - event name: `ldod-login`
    - payload:

    ```js
    {
      detail: {
        user: Object;
      }
    }
    ```

    - emiter: Bubbled on the DOM tree;

  - on logout:

    - event name: `ldod-logout`
    - emiter: Bubbled on the DOM tree;

  - Note: It is possible to instantiate more than one user component. The syncronization between them is assured out of the box by component functionality (implemented through custom events);
