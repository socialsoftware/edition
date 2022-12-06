export const parseHTML = (html) => {
  const doc = new DOMParser().parseFromString(html, 'text/html');
  return doc.body.firstChild ?? doc.head.firstChild;
};


export const dom = (html) =>
  document.createRange().createContextualFragment(html);

export const ldodRender = (component, target) => target.appendChild(component);

export const useState = (initial) => {
  let state = initial;
  const getState = () => state;
  const setState = (newState) => (state = newState);
  return [getState, setState];
};
export const sleep = async (ms) => new Promise((r) => setTimeout(r, ms));
