/** @format */

import { fetcher } from '@core';

const PATH = '/search';

export const simpleSearchRequest = async searchBody =>
	await fetcher.post(`${PATH}/simple-search`, searchBody);

export const getAdvSearchDto = async () => await fetcher.get(`${PATH}/advanced-search`, null);

export const advancedSearch = async body => await fetcher.post(`${PATH}/advanced-search`, body);
