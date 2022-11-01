import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/fragment`;

export const getVirtualFragmentNavInters = async (xmlId, body) =>
  await fetcher.post(`${HOST}/${xmlId}`, body);

export const getVirtualFragmentInter = async (xmlId, urlId) =>
  await fetcher.get(`${HOST}/${xmlId}/inter/${urlId}`, null);

export const getVirtualFragmentInters = async (xmlId, body) =>
  await fetcher.post(`${HOST}/${xmlId}/inters`, body);
