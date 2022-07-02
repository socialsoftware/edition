import { Store } from 'shared/store.js';

const store = new Store(
  { language: 'en', token: '' },
  { storageName: 'ldod-store', keys: ['language', 'token'] }
);

const onLanguage = ({ detail }) => store.setState(detail);
window.addEventListener('ldod-language', onLanguage);

export { store };
