const isAnEventHandler = (name) =>
  name.startsWith('on') && name.toLowerCase() in window;

export const createElement = (tag, props, ...children) => {
  if (typeof tag === 'string') {
    const elem = document.createElement(tag);

    Object.entries(props || {}).forEach(([key, value]) => {
      if (isAnEventHandler(key))
        return elem.addEventListener(key.toLowerCase().substring(2), value);

      if (key === 'style') {
        return Object.entries(value).forEach(
          ([styleKey, styleValue]) => (elem.style[styleKey] = styleValue)
        );
      }

      if (typeof value === 'function' || typeof value === 'object') {
        return (elem[key] = value);
      }

      elem.setAttribute(key, value.toString());
    });

    children.forEach((child) => appendChildren(elem, child));

    return elem;
  }
  if (typeof tag === 'function') return tag(props, children);

  throw new TypeError(`Element ${tag} not recognized`);
};

const appendChildren = (parent, child) => {
  if (Array.isArray(child))
    return child.forEach((nested) => appendChildren(parent, nested));
  parent.appendChild(child.nodeType ? child : document.createTextNode(child));
};

export const createFragment = (...children) => {
  const ele = document.createDocumentFragment();
  children.forEach((child) => child && appendChildren(ele, child));
  return ele;
};
