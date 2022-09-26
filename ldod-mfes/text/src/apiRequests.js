import { fetcher } from 'shared/fetcher.js';

const HOST = import.meta.env.VITE_HOST;

export const getInterSources = async () =>
  await fetcher.get(`${HOST}/text/sources`, null);

export const getFragments = async () =>
  await fetcher.get(`${HOST}/text/fragments`, null);

export const getAdminFragments = async () =>
  await fetcher.get(`${HOST}/text/admin/fragments`, null);

export const removeFragmentById = async (id) =>
  await fetcher.post(`${HOST}/text/admin/fragment-delete/${id}`, null);

export const removeAllFragments = async () =>
  await fetcher.post(`${HOST}/text/admin/fragments-delete-all`, null);

export const getExpertEditionByAcrn = async (acrn) =>
  await fetcher.get(`${HOST}/text/acronym/${acrn}`, null);

export const getFragment = async (xmlId) =>
  await fetcher.get(`${HOST}/text/fragment/${xmlId}`, null);

export const getFragmentInter = async (xmlId, urlId, body) =>
  await fetcher.post(`${HOST}/text/fragment/${xmlId}/inter/${urlId}`, body);

export const getFragmentInters = async (xmlId, body) =>
  await fetcher.post(`${HOST}/text/fragment/${xmlId}/inters`, body);

export const updateFragmentInter = async (xmlId, urlId, body) => {
  return body.inters.length > 1 || !urlId
    ? await getFragmentInters(xmlId, body)
    : await getFragmentInter(xmlId, urlId, body);
};
