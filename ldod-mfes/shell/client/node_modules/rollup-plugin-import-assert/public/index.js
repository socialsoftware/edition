import styles from './style.css' assert { type: 'css' };
import data from './data/info.json' assert { type: 'json' };
import './test-el/test-el.wc.js';

document.adoptedStyleSheets = [styles];

setTimeout(() => {
  document.querySelector('h1 > span').innerText = data.name.first;
}, 1000);

import('./other.css', { assert: { type: 'css' } })
  .then(module => {
    const sheet = module.default;
    document.adoptedStyleSheets = [...document.adoptedStyleSheets, sheet];
  });
