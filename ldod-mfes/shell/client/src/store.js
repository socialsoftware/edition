import { Store } from 'shared/store.js';

const store = new Store(
  { language: 'en', token: '' },
  { storageName: 'ldod-store', keys: ['language', 'token'] }
);

export { store };
