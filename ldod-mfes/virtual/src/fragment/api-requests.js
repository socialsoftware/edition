/** @format */

import { fetcher } from '@core';

const PATH = `/virtual/fragment`;

export const getVirtualFragmentNavInters = async (xmlId, body) =>
	await fetcher.post(`${PATH}/${xmlId}`, body, window.token);

export const getVirtualFragmentInter = async (xmlId, urlId) =>
	await fetcher.get(`${PATH}/${xmlId}/inter/${urlId}`, null, window.token);

export const getVirtualFragmentInters = async (xmlId, body) =>
	await fetcher.post(`${PATH}/${xmlId}/inters`, body, window.token);

export const addInterRequest = async (xmlId, veId, interId, body) =>
	await fetcher.post(
		`${PATH}/${xmlId}/restricted/add-inter/${veId}/${interId}`,
		body,
		window.token
	);

export const associateTagsRequest = async (fragInterId, body) =>
	await fetcher.post(
		`${PATH}/restricted/frag-inter/${fragInterId}/tag/associate`,
		body,
		window.token
	);

export const dissociateTagRequest = async (fragInterId, categoryId) =>
	await fetcher.get(
		`${PATH}/restricted/frag-inter/${fragInterId}/tag/dissociate/${categoryId}`,
		null,
		window.token
	);
