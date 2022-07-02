# string-to-template-literal

[![MEAN Module](https://img.shields.io/badge/MEAN%20Module-TypeScript-blue.svg?style=flat-square)](https://github.com/mgenware/MEAN-Module)
[![Build Status](https://img.shields.io/travis/mgenware/string-to-template-literal.svg?style=flat-square&label=Build+Status)](https://travis-ci.org/mgenware/string-to-template-literal)
[![npm version](https://img.shields.io/npm/v/string-to-template-literal.svg?style=flat-square)](https://npmjs.com/package/string-to-template-literal)
[![Node.js Version](http://img.shields.io/node/v/string-to-template-literal.svg?style=flat-square)](https://nodejs.org/en/)

Converts a string to a template literal

## Installation

```sh
yarn add string-to-template-literal
```

## Usage

```js
import convert from 'string-to-template-literal';

convert('AB`C` ${');
// `AB\`C\` \${`
```
