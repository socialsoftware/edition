export const parseHTML = (html) => {
  const doc = new DOMParser().parseFromString(html, 'text/html');
  return doc.body.firstChild ?? doc.head.firstChild;
};
