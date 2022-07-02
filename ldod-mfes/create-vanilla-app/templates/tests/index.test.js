/**
 * @jest-environment jsdom
 */

import App from '../transpiled-src/app.js';

test('Components are being rendered', () => {
  const div = document.createElement('div');
  div.id = 'root';
  const uut = div.appendChild(App());
  expect(uut.childNodes.length).toBe(5);
  expect(uut.id).toBe('foo');
});
