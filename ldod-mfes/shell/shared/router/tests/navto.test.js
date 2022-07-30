import { Window } from 'happy-dom';
import NavTo from '../src/NavTo.js';

let window;
let uut;
let result;
const callback = jest.fn((e) => (result = e.detail));

beforeEach(() => {
  result = undefined;
  window = new Window();
  window.addEventListener('ldod-url-changed', callback);
  window.customElements.define('nav-to', NavTo);
  uut = window.document.createElement('nav-to');
});

test('when the nav-to is appended to DOM with valid "to" attribute the event is  fired', () => {
  uut.setAttribute('to', '/path');
  window.document.appendChild(uut);
  uut.dispatchEvent(new Event('click'));

  expect(result).toEqual({ path: '/path' });
});
test('when the nav-to is not appended to DOM the event is not fired', () => {
  uut.dispatchEvent(new Event('click'));

  expect(result).toBeUndefined();
});

test('when the nav-to is appended to DOM with no "to" attribute the event is not fired', () => {
  window.document.appendChild(uut);
  expect(result).toBeUndefined();
});

test('when the nav-to is appended to DOM with invalid "to" attribute the event is not fired', () => {
  uut.setAttribute('to', '');
  window.document.appendChild(uut);
  uut.dispatchEvent(new Event('click'));
  expect(result).toBeUndefined();
});

test('when the nav-to is appended to DOM with invalid "to" attribute the event is not fired', () => {
  uut.setAttribute('to', '  ');
  window.document.appendChild(uut);
  uut.dispatchEvent(new Event('click'));
  expect(result).toBeUndefined();
});
