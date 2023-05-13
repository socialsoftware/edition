/** @format */

import { Window } from 'happy-dom';
import NavTo from '../src/nav-to.js';

let window;
let uut;
let result;
const callback = jest.fn(e => (result = e.detail));

beforeEach(() => {
	result = undefined;
	window = new Window();
	window.addEventListener('ldod-url-changed', callback);
	window.customElements.define('nav-to', NavTo);
	uut = window.document.createElement('a', { is: 'nav-to' });

	Object.defineProperty(uut, 'publishedMfes', {
		get() {
			return ['path', '/'];
		},
	});
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

test('when the nav-to is appended with no attribute to it is not hidden', () => {
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('');
});

test('when the nav-to is appended with a "to" attribute corresponding to a ublished MFE the element is not hidden', () => {
	uut.setAttribute('to', '/path');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('');
});

test('when the nav-to is appended with a "to" attribute corresponding to a non published MFE the element is hidden', () => {
	uut.setAttribute('to', '/test');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a "to" attribute with no content the element is hidden', () => {
	uut.setAttribute('to', '');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a "to" attribute with undefined value the element is hidden', () => {
	uut.setAttribute('to', undefined);
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a "to" attribute with false value the element is hidden', () => {
	uut.setAttribute('to', false);
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a "to" attribute with null value the element is hidden', () => {
	uut.setAttribute('to', null);
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a  invalid "to" attribute value the element is hidden', () => {
	uut.setAttribute('to', '    ');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('none');
});

test('when the nav-to is appended with a "to" attribute value corresnponding to the root', () => {
	uut.setAttribute('to', '/');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('');
});

test('when the nav-to is appended with a target attribute the element is not hidden', () => {
	uut.setAttribute('target');
	window.document.appendChild(uut);
	const display = uut.style.display;
	expect(display).toBe('');
});
