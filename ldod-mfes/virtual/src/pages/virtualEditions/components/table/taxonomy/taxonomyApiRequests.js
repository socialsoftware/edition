import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/taxonomy`;

export const getVeTaxonomy = async (id) =>
  await fetcher.get(`${HOST}/restricted/${id}`, null, window.token);

export const addCategory = async (id, body) =>
  await fetcher.post(
    `${HOST}/restricted/${id}/category/create`,
    body,
    window.token
  );

export const deleteCategories = async (taxId, body) =>
  await fetcher.post(
    `${HOST}/restricted/${taxId}/categories/delete`,
    body,
    window.token
  );

export const mergeCategories = async (taxId, body) =>
  await fetcher.post(
    `${HOST}/restricted/${taxId}/categories/merge`,
    body,
    window.token
  );

export const generateTopics = async (veId, body) =>
  await fetcher.post(`${HOST}/restricted/${veId}/generate`, body, window.token);

export const addTopics = async (veId, body) =>
  await fetcher.post(
    `${HOST}/restricted/${veId}/createTopics`,
    body,
    window.token
  );
export const editCategory = async (categoryId, body) =>
  await fetcher.post(
    `${HOST}/restricted/category/${categoryId}/update/`,
    body,
    window.token
  );

export const extractCategoryFragments = async (categoryId, body) =>
  await fetcher.post(
    `${HOST}/restricted/category/${categoryId}/extract`,
    body,
    window.token
  );
