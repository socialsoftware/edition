import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/edition`;

export const getVirtualEditionByAcronym = async (acrn) =>
  await fetcher.get(`${HOST}/acronym/${acrn}`, null);

export const getVeUser = async (username) =>
  await fetcher.get(`${HOST}/user/${username}`, null);

export const getVeTaxonomy = async (acronym) =>
  await fetcher.get(`${HOST}/acronym/${acronym}/taxonomy`, null);

export const getCategoryData = async (acronym, category) =>
  await fetcher.get(`${HOST}/acronym/${acronym}/category/${category}`, null);
