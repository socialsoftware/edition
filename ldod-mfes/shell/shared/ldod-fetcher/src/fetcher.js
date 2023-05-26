/** @format */

import { ldodEventPublisher } from '../../ldod-event-bus/src/helpers';
import { getPartialStorage } from '../../ldod-store/index';
import { handleRequest } from './handlers';

const HOST = window.process?.apiHost || 'http://localhost:8000/api';

const handleLoading = isLoading => ldodEventPublisher('loading', isLoading);

const getStorageToken = () => getPartialStorage('ldod-store', ['token'])?.token;

const request = async (method, path, data, token, signal) => {
	handleLoading(true);
	const options = {};
	const accessToken = token ? token : getStorageToken();

	if (data && typeof data !== 'object') throw new Error('Data must be an Object');
	options.headers = new Headers();
	options.headers.append('Authorization', `Bearer ${accessToken || ''}`);
	options.headers.append('Content-Type', 'application/json');
	options.headers.append('Accept-Encoding', 'gzip');

	if (path.includes('restricted') || path.includes('admin'))
		options.headers.append('Cache-Control', 'private');

	options.method = method;
	if (signal) options.signal = signal;
	if (data) options.body = JSON.stringify(data);
	return await handleRequest(HOST.concat(path), options);
};

export const fetcher = ['get', 'post', 'put', 'delete'].reduce((fetcher, method) => {
	fetcher[method] = (url, data = {}, token = undefined, signal = undefined) =>
		request(method.toUpperCase(), url, data, token, signal);
	return fetcher;
}, {});

export const xmlFileFetcher = async ({
	url,
	body,
	method = 'POST',
	token,
	headers = [],
	signal,
}) => {
	handleLoading(true);
	const options = {};
	const accessToken = token ? token : getStorageToken();
	options.headers = new Headers();
	accessToken && options.headers.set('Authorization', accessToken);
	headers.forEach(header =>
		Object.entries(header).forEach(([key, value]) => {
			options.headers.set(key, value);
		})
	);
	options.method = method;
	if (signal) options.signal = signal;
	if (body) options.body = body;
	return await handleRequest(HOST.concat(url), options);
};

export default fetcher;
