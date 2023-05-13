/** @format */

import { ldodEventPublisher } from '../../ldod-event-bus/src/helpers';
import { getPartialStorage } from '../../ldod-store/index';
import { navigateTo } from '../../ldod-router/index';

const HOST = window.process?.apiHost || 'http://localhost:8000/api';

const handleLoading = isLoading => ldodEventPublisher('loading', isLoading);
const handleLogout = () => ldodEventPublisher('logout');
const handleError = message => ldodEventPublisher('error', message || 'Something went wrong');

const getStorageToken = () => getPartialStorage('ldod-store', ['token'])?.token;

const fetchRequest = async (url, options) => {
	try {
		const res = await fetch(url, options);
		if (res.status === 401) {
			handleLogout();
			return Promise.reject(res);
		}
		const resData = await res.json();
		if (!res.ok) {
			handleError(resData?.message);
			return Promise.reject(resData || res);
		}
		return resData;
	} catch (error) {
		console.error('FETCH ERROR: ', error?.stack ?? error);
		handleError();
		navigateTo('/');
	} finally {
		handleLoading(false);
	}
};

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
	return await fetchRequest(HOST.concat(path), options);
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
	return await fetchRequest(HOST.concat(url), options);
};

export default fetcher;
