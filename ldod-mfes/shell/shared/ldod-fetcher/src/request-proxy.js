/** @format */

import { ldodEventPublisher } from '../../ldod-event-bus/src/helpers';
import { getPartialStorage } from '../../ldod-store/index';
import { navigateTo } from '../../ldod-router/index';

const HOST = window.process?.apiHost || 'http://localhost:8000/api';
const handleLoading = isLoading => ldodEventPublisher('loading', isLoading);
const handleLogout = () => ldodEventPublisher('logout');
const handleError = message => ldodEventPublisher('error', message || 'Something went wrong');

function getOptions(method, token, contentType) {
	const headers = new Headers({
		Authorization: `Bearer ${token || ''}`,
		'Accept-Encoding': 'gzip',
	});
	if (contentType === mediaType.JSON) headers.set('Content-Type', contentType);
	return { method, headers, token };
}

const mediaType = Object.freeze({
	JSON: 'application/json',
	FORM_DATA: 'multipart/form-data',
});

class RequestProxy {
	constructor() {
		if (RequestProxy.instance) return RequestProxy.instance;
		RequestProxy.instance = this;
	}

	get storageToken() {
		return getPartialStorage('ldod-store', ['token'])?.token;
	}

	async fetchRequest(url, options) {
		try {
			const res = await fetch(url, options);
			const resData = await res.json();
			handleLoading(false);
			if (!res.ok) {
				if (res.status === 401) handleLogout();
				else handleError(resData?.message);
				return Promise.reject(resData || res);
			}
			return resData;
		} catch (error) {
			console.error('FETCH ERROR: ', error?.stack ?? error);
			handleError();
			navigateTo('/');
			handleLoading(false);
		}
	}

	async request(method, { url, path, token, signal }, contentType, data) {
		handleLoading(true);
		const accessToken = token ? token : this.storageToken;
		const options = getOptions(method, accessToken, contentType);
		if (data) options.body = data;
		if (signal) options.signal = signal;
		return await this.fetchRequest(url ?? HOST.concat(path), options);
	}

	async get(request) {
		return await this.request('GET', request, mediaType.JSON);
	}

	async post(request, data) {
		return await this.request('POST', request, mediaType.JSON, JSON.stringify(data));
	}

	async upload(request, data) {
		return await this.request('POST', request, mediaType.FORM_DATA, data);
	}
}

export default Object.freeze(new RequestProxy());
