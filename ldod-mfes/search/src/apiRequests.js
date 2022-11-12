import { fetcher } from 'shared/fetcher.js';

const HOST = import.meta.env.VITE_HOST;

export const simpleSearchRequest = async (searchBody) =>
  await fetcher.post(`${HOST}/search/simple-search`, searchBody);

export const getAdvSearchDto = async () =>
  await fetcher.get(`${HOST}/search/advanced-search`, null);
