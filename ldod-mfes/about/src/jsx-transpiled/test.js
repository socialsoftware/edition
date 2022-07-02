/** @jsx createElement */

/** @jsxFrag createFragment */
import { createElement, createFragment } from 'shared/public/vanilla-jsx.js';
const arr = ['Hey', 'There', 'Jack'];

const Test = () => createElement(createFragment, null, createElement("div", null, createElement("div", null, createElement("h1", null, "Jo\xE3o"))));

const foo = createElement("div", null, createElement(Test, null), createElement("div", {
  class: "foo",
  style: {
    fontSize: '50px'
  }
}, "Hi there"), arr.map(name => createElement("div", null, name)));
document.getElementById('test').appendChild(foo);