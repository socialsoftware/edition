import { textReferences } from './textReferences';
export const isDev = () => import.meta.env.DEV;

export function dispatchCustomEvent(
  node,
  payload,
  { type, bubbles, composed }
) {
  node.dispatchEvent(
    new CustomEvent(`ldod-${type}`, {
      detail: payload,
      bubbles,
      composed,
    })
  );
}

export function processFragmentAnchor(docFrag) {
  docFrag.querySelectorAll('a[href]').forEach((anchor) => {
    if (anchor.href.includes('/fragments/fragment/Fr')) {
      const xmlId = anchor.href.slice(anchor.href.indexOf('Fr'));
      const newAnchor = document.createElement('a', { is: 'nav-to' });
      newAnchor.setAttribute('to', textReferences.fragment(xmlId));
      newAnchor.innerHTML = anchor.innerHTML;
      anchor.replaceWith(newAnchor);
    }
  });
  return docFrag;
}
