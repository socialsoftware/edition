import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/fragment`;

export const getVirtualFragmentNavInters = async (xmlId, body) =>
  await fetcher.post(`${HOST}/${xmlId}`, body, window.token);

export const getVirtualFragmentInter = async (xmlId, urlId) =>
  await fetcher.get(`${HOST}/${xmlId}/inter/${urlId}`, null, window.token);

export const getVirtualFragmentInters = async (xmlId, body) =>
  await fetcher.post(`${HOST}/${xmlId}/inters`, body, window.token);

export const addInterRequest = async (xmlId, veId, interId, body) =>
  await fetcher.post(
    `${HOST}/${xmlId}/restricted/add-inter/${veId}/${interId}`,
    body,
    window.token
  );

export const associateTagsRequest = async (fragInterId, body) =>
  await fetcher.post(
    `${HOST}/restricted/frag-inter/${fragInterId}/tag/associate`,
    body,
    window.token
  );

export const dissociateTagRequest = async (fragInterId, categoryId) =>
  await fetcher.get(
    `${HOST}/restricted/frag-inter/${fragInterId}/tag/dissociate/${categoryId}`,
    null,
    window.token
  );
