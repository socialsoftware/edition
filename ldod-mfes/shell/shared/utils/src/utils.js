export const parseHTML = (html) => {
  const doc = new DOMParser().parseFromString(html, 'text/html');
  return doc.body.firstChild ?? doc.head.firstChild;
};

export const dom = (html) => {
  const doc = document.createRange().createContextualFragment(html);
  doc.querySelectorAll('a[href]').forEach((anchor) => {
    if (anchor.href.includes('/fragments/fragment/Fr')) {
      const xmlId = anchor.href.slice(anchor.href.indexOf('Fr'));
      const newAnchor = document.createElement('a', { is: 'nav-to' });
      newAnchor.setAttribute('to', `/text/fragment/${xmlId}`);
      newAnchor.innerHTML = anchor.innerHTML;
      anchor.replaceWith(newAnchor);
    }
  });
  return doc;
};

export const ldodRender = (component, target) => target.appendChild(component);

export const useState = (initial) => {
  let state = initial;
  const getState = () => state;
  const setState = (newState) => (state = newState);
  return [getState, setState];
};
