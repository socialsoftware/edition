/** @format */

import { isAdmin, store } from '../store';

const { instance } = await customElements.whenDefined('nav-bar');
const adminHeader = [...instance.dropdowns].find(drop => drop.getAttribute('key') === 'admin');
const unsub = store.subscribe(() => (adminHeader.hidden = !isAdmin()), 'user', { fire: true });
