/** @jsx createElement */
/** @jsxFrag createFragment */

import { createElement, createFragment } from 'shared/public/vanilla-jsx.js';

const arr = ['Hey', 'There', 'Jack'];

const Test = () => (
  <>
    <div>
      <div>
        <h1>João</h1>
      </div>
    </div>
  </>
);

const foo = (
  <div>
    <Test />
    <div class="foo" style={{ fontSize: '50px' }}>
      Hi there
    </div>
    {arr.map((name) => (
      <div>{name}</div>
    ))}
  </div>
);

document.getElementById('test').appendChild(foo);
