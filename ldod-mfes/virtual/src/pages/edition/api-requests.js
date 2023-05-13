/** @format */

import { fetcher } from '@core';

const PATH = `/virtual/edition`;

export const getVirtualEditionByAcronym = async acrn =>
	await fetcher.get(`${PATH}/acronym/${acrn}`, null);

export const getVeUser = async username => await fetcher.get(`${PATH}/user/${username}`, null);

export const getVeTaxonomy = async acronym =>
	await fetcher.get(`${PATH}/acronym/${acronym}/taxonomy`, null);

export const getCategoryData = async (acronym, category) =>
	await fetcher.get(`${PATH}/acronym/${acronym}/category/${category}`, null);
