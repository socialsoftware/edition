/** @format */

import { fetcher } from '@core';

const PATH = '/social';
const ADMIN_PATH = '/admin/social';

export const getCitationsList = async () => await fetcher.get(`${PATH}/twitter-citations`, null);

export const getTweetsToManage = async () => await fetcher.get(`${ADMIN_PATH}/tweets`, null);

export const generateCitations = async () => {
	dataProxy.reset = true;
	return await fetcher.post(`${ADMIN_PATH}/tweets/generateCitations`, {});
};

export const removeTweets = async () => {
	dataProxy.reset = true;
	return await fetcher.post(`${ADMIN_PATH}/remove-tweets`, {});
};

const API = {
	citations: async () => await fetcher.get(`${PATH}/twitter-citations`, null),
	manageCitations: async () => await fetcher.get(`${ADMIN_PATH}/tweets`, null),
};

const data = {};

const handler = {
	set(target, prop, value) {
		if (prop === 'reset' && value) Reflect.ownKeys(target).forEach(key => delete target[key]);
		else target[prop] = value;
		return true;
	},
	async get(target, prop) {
		if (prop === 'reset') {
			return Reflect.ownKeys(target).forEach(key => delete target[key]);
		}
		if (!target[prop]) target[prop] = await API[prop]();
		return target[prop];
	},
};

export const dataProxy = new Proxy(data, handler);
