# LdoD utils

## Developed with vanilla javascript

### Create HTML Node from string

```js
import { parseHTML } from 'path/to/shared/utils.js';

const divNode = parseHTML('<div>I am HTMLDivElement</div>');
divNode instanceof HTMLDivElement; // true

const linkNode = parseHTML('<link>I am HTMLLinkElement</link>');
linkNode instanceof HTMLLinkElement; // true
```

### Create DocumentFragment from string

```js
import { dom } from 'path/to/shared/utils.js';

const docFrag = dom('<div>I am HTMLDivElement</div>');

docFrag instanceof DocumentFragment; // true

docFrag.firstChild instanceof HTMLDivElement; // true
```
