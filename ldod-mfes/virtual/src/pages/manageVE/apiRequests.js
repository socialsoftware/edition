import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/admin`;

export const getVirtualEditions4Manage = async () =>
  await fetcher.get(`${HOST}/virtual-editions`, null);

export const deleteVE = async (id) =>
  await fetcher.get(`${HOST}/virtual-edition-delete/${id}`, null);
