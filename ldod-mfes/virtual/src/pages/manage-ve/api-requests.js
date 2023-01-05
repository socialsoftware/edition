import { fetcher } from '@shared/fetcher.js';

const PATH = `/virtual/admin`;

export const getVirtualEditions4Manage = async () => await fetcher.get(`${PATH}/virtual-editions`, null);

export const deleteVE = async id => await fetcher.get(`${PATH}/virtual-edition-delete/${id}`, null);
