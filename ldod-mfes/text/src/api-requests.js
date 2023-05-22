/** @format */

import { fetcher, fetchProxy } from '@core';
const BASE_URL = `${import.meta.env.VITE_HOST}`;
const PATH = '/text';
const ADMIN_PATH = PATH + '/admin';

export const API = {
	fragments: async () =>
		(await fetchProxy(`${BASE_URL + PATH}/fragments`, { method: 'GET' }))
			.json()
			.catch(console.error),
	sources: async () =>
		(await fetchProxy(`${BASE_URL + PATH}/sources`, { method: 'GET' }))
			.json()
			.catch(console.error),
	adminFragments: async () =>
		(await fetchProxy(`${BASE_URL + ADMIN_PATH}/fragments`, { method: 'GET' }))
			.json()
			.catch(console.error),
};
const data = {};

const handler = {
	async get(target, prop) {
		if (!target[prop]) target[prop] = await API[prop]();
		return target[prop];
	},
	set(target, prop, value) {
		if (prop === 'reset' && value) Reflect.ownKeys(target).forEach(key => delete target[key]);
		else target[prop] = value;
		return true;
	},
};
export const dataProxy = new Proxy(data, handler);

export const removeFragmentById = async id => {
	dataProxy.reset = true;
	return await fetcher.post(`${ADMIN_PATH}/fragment-delete/${id}`, null);
};

export const removeAllFragments = async () => {
	dataProxy.reset = true;
	return await fetcher.post(`${ADMIN_PATH}/fragments-delete-all`, null);
};
export const getExpertEditionByAcrn = async acrn =>
	await fetcher.get(`${PATH}/acronym/${acrn}`, null);

export const getFragment = async xmlId => await fetcher.get(`${PATH}/fragment/${xmlId}`, null);

export const getFragmentInter = async (xmlId, urlId, body) =>
	await fetcher.post(`${PATH}/fragment/${xmlId}/inter/${urlId}`, body);

export const getFragmentInters = async (xmlId, body) =>
	await fetcher.post(`${PATH}/fragment/${xmlId}/inters`, body);

export const updateFragmentInter = async (xmlId, urlId, body) => {
	return body.inters.length > 1 || !urlId
		? await getFragmentInters(xmlId, body)
		: await getFragmentInter(xmlId, urlId, body);
};
