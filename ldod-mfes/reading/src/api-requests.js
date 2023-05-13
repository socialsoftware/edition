/** @format */

import { fetcher } from '@core';
import { getState, resetReadingStore } from './store';

const PATH = '/reading';

export async function getStartReading() {
	return await fetcher.get(PATH, null);
}

export async function getExpertEditionInter(xmlId, urlId) {
	return await fetcher.post(`${PATH}/fragment/${xmlId}/inter/${urlId}`, getState()).then(res => {
		if (!res || res.ok === false) {
			resetReadingStore();
			return Promise.reject({ message: res?.message });
		}
		return Promise.resolve(res);
	});
}
