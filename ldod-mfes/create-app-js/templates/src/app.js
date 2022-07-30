/** @jsx createElement */
/** @jsxFrag createFragment */

import { createElement, createFragment } from 'shared/vanilla-jsx.js';

const arr = ['Hello', 'There', 'World'];

const Bar = () => (
  <>
    <div>
      <div>
        <h1>Hello</h1>
      </div>
    </div>
  </>
);

export default () => (
  <div id="foo">
    <Bar />
    <div class="foo" style={{ fontSize: '50px' }}>
      World!
    </div>
    {arr.map((name) => (
      <div>{name}</div>
    ))}
  </div>
);
