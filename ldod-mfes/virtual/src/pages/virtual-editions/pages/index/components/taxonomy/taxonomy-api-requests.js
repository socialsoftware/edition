/** @format */

import { fetcher } from '@core';

const PATH = `/virtual/taxonomy`;

export const getVeTaxonomy = async id =>
	await fetcher.get(`${PATH}/restricted/${id}`, null, window.token);

export const addCategory = async (id, body) =>
	await fetcher.post(`${PATH}/restricted/${id}/category/create`, body, window.token);

export const deleteCategories = async (taxId, body) =>
	await fetcher.post(`${PATH}/restricted/${taxId}/categories/delete`, body, window.token);

export const mergeCategories = async (taxId, body) =>
	await fetcher.post(`${PATH}/restricted/${taxId}/categories/merge`, body, window.token);

export const generateTopics = async (veId, body) =>
	await fetcher.post(`${PATH}/restricted/${veId}/generate`, body, window.token);

export const addTopics = async (veId, body) =>
	await fetcher.post(`${PATH}/restricted/${veId}/createTopics`, body, window.token);
export const editCategory = async (categoryId, body) =>
	await fetcher.post(`${PATH}/restricted/category/${categoryId}/update/`, body, window.token);

export const extractCategoryFragments = async (categoryId, body) =>
	await fetcher.post(`${PATH}/restricted/category/${categoryId}/extract`, body, window.token);
