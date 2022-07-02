export const parseHTML = (html) => {
  const doc = new DOMParser().parseFromString(html, 'text/html');
  return doc.body.firstChild ?? doc.head.firstChild;
};

export const dom = (html) =>
  document.createRange().createContextualFragment(html);

export const ldodRender = (component, target) => target.appendChild(component);
document.appendChild;
