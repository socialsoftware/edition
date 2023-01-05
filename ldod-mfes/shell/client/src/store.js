import { Store } from '@shared/store.js';

export const store = new Store(
	{ language: 'en', token: '' },
	{ storageName: 'ldod-store', keys: ['language', 'token'] }
);
