import { getCitationsList } from '@src/apiRequests.js';
import './LdodCitations.jsx';

const mount = (lang, ref) => {
  getCitationsList().then((data) => {
    const ldodSocial = document.querySelector(`${ref}>ldod-citations`);
    ldodSocial.citations = data;
    ldodSocial.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(<ldod-citations language={lang}></ldod-citations>);
};
const unMount = () => document.querySelector('ldod-citations')?.remove();

const path = '/citations';

export { mount, unMount, path };
