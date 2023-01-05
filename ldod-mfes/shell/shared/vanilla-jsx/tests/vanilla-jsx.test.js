import { createElement, createFragment } from '../vanilla-jsx';

test('create div element with no props and no children', () => {
	const uut = createElement('div');

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual([]);
	expect(uut.childNodes.length).toBe(0);
});

test('create div element with no props and no children with text inside', () => {
	const uut = createElement('div', {}, 'Test');

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual([]);
	expect(uut.childNodes.length).toBe(1);
	expect(uut.childNodes[0]).toBeInstanceOf(Text);
});

test('create p element with string and style props and no children', () => {
	const uut = createElement('p', {
		id: 'test',
		class: 'class1 class2',
		style: { fontSize: '10px', color: 'red' },
	});

	expect(uut).toBeInstanceOf(HTMLParagraphElement);
	expect(uut.getAttributeNames()).toEqual(['id', 'class', 'style']);
	expect(uut.getAttribute('id')).toBe('test');
	expect(uut.getAttribute('class')).toBe('class1 class2');
	expect(uut.style.fontSize).toBe('10px');
	expect(uut.style.color).toBe('red');
});

test('create div element with 2 children', () => {
	const h1 = createElement('h1');
	const p = createElement('p', {});
	const uut = createElement('div', {}, p, h1);

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual([]);
	expect(uut.childNodes.length).toBe(2);
	const fisrtChild = uut.childNodes[0];
	const secondChild = uut.childNodes[1];
	expect(fisrtChild).toBeInstanceOf(HTMLParagraphElement);
	expect(fisrtChild.childNodes.length).toBe(0);
	expect(secondChild).toBeInstanceOf(HTMLHeadingElement);
	expect(secondChild.childNodes.length).toBe(0);
});

test('create div element with nested children', () => {
	const h1 = createElement('h1');
	const p = createElement('p', {}, h1);
	const uut = createElement('div', {}, p);

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual([]);
	expect(uut.childNodes.length).toBe(1);
	const child = uut.childNodes[0];
	const nestedChild = child.childNodes[0];
	expect(child).toBeInstanceOf(HTMLParagraphElement);
	expect(child.childNodes.length).toBe(1);
	expect(nestedChild).toBeInstanceOf(HTMLHeadingElement);
	expect(nestedChild.childNodes.length).toBe(0);
});

test('create div element with child and multiple nested childs', () => {
	const h1 = createElement('h1');
	const h2 = createElement('h2');
	const p = createElement('p', {}, h1, h2);
	const uut = createElement('div', {}, p);

	const child = uut.childNodes[0];
	const nestedChilds = child.childNodes;
	expect(child).toBeInstanceOf(HTMLParagraphElement);
	expect(nestedChilds.length).toBe(2);
});

test('create div element with an event as prop', () => {
	const events = {};

	const clickHandler = jest.fn(() => (events.click = 'test'));

	const uut = createElement('div', {
		id: 'test',
		onclick: clickHandler,
	});

	uut.dispatchEvent(new Event('click'));

	jest.spyOn(uut, 'click');

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual(['id']);
	expect(clickHandler).toHaveBeenCalled();
	expect(events.click).toEqual('test');
});

test('create div element with props as function and object', () => {
	const uut = createElement('div', {
		component: console.log,
		object: { one: 'one', two: 'two' },
	});

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual([]);
	expect(uut.component).toBe(console.log);
	expect(uut.object).toEqual({ one: 'one', two: 'two' });
});

test('create element from component and the function is executed', () => {
	const componentUnderTest = (props, children) => createElement('div', props, children);
	const uut = createElement(
		componentUnderTest,
		{
			id: 'component',
			class: 'comp',
		},
		createElement('div')
	);

	expect(uut).toBeInstanceOf(HTMLDivElement);
	expect(uut.getAttributeNames()).toEqual(['id', 'class']);
	expect(uut.childNodes.length).toBe(1);
	expect(uut.childNodes[0]).toBeInstanceOf(HTMLDivElement);
});

test('create element with invalid tag and an error is thrown', () => {
	expect(() => createElement(1, { id: 'int' })).toThrow(TypeError);
});

test('create a document fragment', () => {
	const uut = createFragment(createElement('div'), createElement('div'));
	expect(uut.childNodes.length).toBe(2);
	uut.childNodes.forEach(node => expect(node).toBeInstanceOf(HTMLDivElement));
});

test('create a div element that is an extension of a custom HTMLDivElement', () => {
	class CustomDiv extends HTMLDivElement {
		constructor() {
			super();
		}
	}
	customElements.define('custom-div', CustomDiv, { extends: 'div' });
	const customDiv = createElement('div', {
		is: 'custom-div',
		id: 'myDiv',
		class: 'test test2',
	});
	expect(customDiv).toBeInstanceOf(CustomDiv);
	expect(customDiv.id).toBe('myDiv');
	expect(customDiv.classList).toContain('test');
	expect(customDiv.classList).toContain('test2');
});
test('create a template element', () => {
	const div = createElement('div', { id: 'div' });
	const template = createElement(
		'template',
		{
			id: 'template',
		},
		div
	);
	expect(template).toBeInstanceOf(HTMLTemplateElement);
	expect(template.id).toBe('template');
	expect(template.content).toBeInstanceOf(DocumentFragment);
	console.log(template.content.firstElementChild);
	expect(template.content.firstElementChild).toBeInstanceOf(HTMLDivElement);
	expect(template.content.firstElementChild.id).toBe('div');
});
