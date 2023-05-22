/** @format */

import { ldodEventBus } from '../ldod-event-bus';
import { getPartialStorage } from '../ldod-store';

export const mergeHeaders = request => {
	const headers = new Request(request).headers;

	return new Headers({
		Authorization:
			headers.get('Authorization') ||
			`Bearer ${getPartialStorage('ldod-store', ['token'])?.token || ''}`,
		'Accept-Encoding': headers.get('Accept-Encoding') || 'gzip',
		'Content-Type': headers.get('Content-Type') || 'application/json',
	});
};

export const fetchProxy = new Proxy(fetch, {
	apply: fetchInterceptor,
});

const handleLoading = isLoading => ldodEventBus.publish('ldod:loading', isLoading);
const handleError = message =>
	ldodEventBus.publish('ldod:error', message || 'Something went wrong');

function fetchInterceptor(target, thisArg, args) {
	handleLoading(true);
	const [resource, options] = args;
	const request = new Request(resource, {
		headers: mergeHeaders(new Request(resource, options)),
	});
	return new Promise((resolve, reject) => {
		target(request)
			.then(res => {
				handleLoading(false);
				return resolve(res);
			})
			.catch(error => {
				handleError(error?.message);
				handleLoading(false);
				return reject(error);
			});
	});
}
