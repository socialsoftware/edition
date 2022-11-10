import { fetcher } from 'shared/fetcher.js';
const HOST = '/annotations/inter';

export const fetchAnnotations = async (id) =>
  await fetcher.get(`${HOST}/${id}`, null, window.token);

export const createAnnotation = async (interId, annotation) =>
  await fetcher.post(
    `${HOST}/${interId}/annotation/create`,
    annotation,
    window.token
  );

export const deleteAnnotation = async (interId, id) =>
  await fetcher.post(
    `${HOST}/${interId}/annotation/${id}/delete`,
    null,
    window.token
  );

export const updateAnnotation = async (id, annotation) =>
  await fetcher.post(`${HOST}/annotation/${id}`, annotation, window.token);
