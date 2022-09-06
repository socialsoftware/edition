import { fetcher } from 'shared/fetcher.js';

const HOST = import.meta.env.VITE_HOST;

export const getSources = async () =>
  await fetcher.get(`${HOST}/text/sources`, null);

export const getFragments = async () =>
  await fetcher.get(`${HOST}/text/fragments`, null);
