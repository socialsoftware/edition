import { fetcher } from 'shared/fetcher.js';

const PATH = '/text';
const ADMIN_PATH = '/text/admin';

export const getInterSources = async () =>
  await fetcher.get(`${PATH}/sources`, null);

export const getFragments = async () =>
  await fetcher.get(`${PATH}/fragments`, null);

export const getAdminFragments = async () =>
  await fetcher.get(`${ADMIN_PATH}/fragments`, null);

export const removeFragmentById = async (id) =>
  await fetcher.post(`${ADMIN_PATH}/fragment-delete/${id}`, null);

export const removeAllFragments = async () =>
  await fetcher.post(`${ADMIN_PATH}/fragments-delete-all`, null);

export const getExpertEditionByAcrn = async (acrn) =>
  await fetcher.get(`${PATH}/acronym/${acrn}`, null);

export const getFragment = async (xmlId) =>
  await fetcher.get(`${PATH}/fragment/${xmlId}`, null);

export const getFragmentInter = async (xmlId, urlId, body) =>
  await fetcher.post(`${PATH}/fragment/${xmlId}/inter/${urlId}`, body);

export const getFragmentInters = async (xmlId, body) =>
  await fetcher.post(`${PATH}/fragment/${xmlId}/inters`, body);

export const updateFragmentInter = async (xmlId, urlId, body) => {
  return body.inters.length > 1 || !urlId
    ? await getFragmentInters(xmlId, body)
    : await getFragmentInter(xmlId, urlId, body);
};
