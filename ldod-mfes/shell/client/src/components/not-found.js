import { dom } from 'shared/utils.js';

const message = {
  pt: 'LdoD: Esta página não existe...',
  en: 'LdoD: Page not found...',
  es: 'LdoD: Esta página no existe',
};

const getNoPage = () => document.body.querySelector('div#no-page.container');

const NoPage = (language) =>
  dom(
    /*html*/ ` <div id="no-page" class="container">${message[language]}</div>`
  );

const mount = (language, ref) => {
  const container = document.body.querySelector(ref);
  container.appendChild(NoPage(language));
  addLanguageListener();
};
const unMount = () => {
  removeLanguageListener();
  getNoPage().remove();
};

const addLanguageListener = () =>
  window.addEventListener('ldod-language', handleLanguageEvent);

const removeLanguageListener = () =>
  window.removeEventListener('ldod-language', handleLanguageEvent);

const handleLanguageEvent = ({ detail }) =>
  (getNoPage().textContent = message[detail.language]);

export default () => ({
  path: '/not-found',
  mount,
  unMount,
});
