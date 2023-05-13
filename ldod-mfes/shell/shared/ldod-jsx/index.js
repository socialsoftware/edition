/** @format */

const isAnEventHandler = name => name.startsWith('on') && name.toLowerCase() in window;

export const createElement = (tag, props, ...children) => {
	if (typeof tag === 'string') {
		const elem = document.createElement(tag, props?.is ? { is: props.is } : '');
		Object.entries(props || {}).forEach(entry => setProps(entry, elem));
		children.forEach(child => appendChildren(elem, child));
		return elem;
	}
	if (typeof tag === 'function') return tag(props, children);
	throw new TypeError(`Element ${tag} not recognized`);
};

const appendChildren = (parent, child) => {
	if (Array.isArray(child)) return child.forEach(nested => appendChildren(parent, nested));
	if (!child) return;
	let element = child.nodeType ? child : document.createTextNode(child);
	if (parent instanceof HTMLTemplateElement) return parent.content.appendChild(element);
	return parent.appendChild(element);
};

export const createFragment = (...children) => {
	const ele = document.createDocumentFragment();
	children.forEach(child => child && appendChildren(ele, child));
	return ele;
};

function setProps([key, value], elem) {
	if (isAnEventHandler(key)) return elem.addEventListener(key.toLowerCase().substring(2), value);

	if (key === 'style')
		return Object.entries(value).forEach(
			([styleKey, styleValue]) => (elem.style[styleKey] = styleValue)
		);

	let typeValue = typeof value;

	if (typeValue === 'function' || typeValue === 'object') {
		elem[key] = value;
		return;
	}

	if (typeValue === 'boolean') return elem.toggleAttribute(key, value);

	elem.setAttribute(key, value?.toString());
}
