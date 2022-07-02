# rollup-plugin-import-assert

🍣 A Rollup plugin which enables [import assertions](https://github.com/tc39/proposal-import-assertions) for top-level CSS and JSON modules. 

## Installation

This package is available on the npm registry under the name `rollup-plugin-import-assert` and can be installed with npm, yarn or however else you consume dependencies.

### Example commands

npm: 

```zsh
npm i -D rollup-plugin-import-assert
```

yarn:

```zsh
yarn add -D rollup-plugin-import-assert
```

## Usage

Once the plugin is installed, you will also need to make sure you have the `acorn-import-assertions` package installed. You can then add both items to your Rollup configuration as below:

```javascript
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import { importAssertions } from 'acorn-import-assertions';

export default {
  input: 'path/to/file.js'
  output: {
    format: 'esm',
    dir: 'lib' // only necessary to enable dynamic imports
  },
  acornInjectPlugins: [ importAssertions ],
  plugins: [ importAssertionsPlugin() ]
}
```

These two plugins will enable the import assertion syntax and behavior in your Rollup build. In your input file, you can import files using type assertions as follows:

```javascript
import styles from './styles.css' assert { type: 'css' };

class MyCustomElement extends HTMLElement {
  connectedCallback() {
    const root = this.attachShadow({ mode: 'open' });
    root.innerHTML = `<h1>Hello world</h1>`;
    root.adoptedStyleSheets = [ styles ];
  }
}

customElements.define('my-custom-element', MyCustomElement);
```

Assuming valid CSS in `styles.css`, the contents of the the CSS will be transformed to use CSS module scripts for use with `DocumentOrShadowRoot.prototype.adoptedStyleSheets`. Currently this API only exists in Chrome, but a [polyfill exists](https://www.npmjs.com/package/construct-style-sheets-polyfill) to port the behavior back to IE11.

## Limitations

This plugin will ignore dynamic imports with dynamic values, e.g.: 

```js
import(`./foo/${bar}.json`, { assert: { type: 'json' } }); // will be ignored

const foo = './foo.json';
import(foo, { assert: { type: 'json' } }); // will be ignored
```