/** @format */

import { fetcher } from '@core';
const PATH = '/annotations/inter';

export const fetchAnnotations = async id => await fetcher.get(`${PATH}/${id}`, null, window.token);

export const createAnnotation = async (interId, annotation) =>
	await fetcher.post(`${PATH}/${interId}/annotation/create`, annotation, window.token);

export const deleteAnnotation = async (interId, id) =>
	await fetcher.post(`${PATH}/${interId}/annotation/${id}/delete`, null, window.token);

export const updateAnnotation = async (id, annotation) =>
	await fetcher.post(`${PATH}/annotation/${id}`, annotation, window.token);
