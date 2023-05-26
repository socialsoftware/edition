/** @format */

import { ldodEventPublisher } from '../../ldod-event-bus/src/helpers';
import { navigateTo } from '../../ldod-router/index';

export const HOST = window.process?.apiHost || 'http://localhost:8000/api';
export const handleLoading = isLoading => ldodEventPublisher('loading', isLoading);
export const handleError = message =>
	ldodEventPublisher('error', message || 'Something went wrong');

export function onRequestError(error) {
	console.error('FETCH ERROR: ', error?.stack ?? error);
	handleError();
	navigateTo('/');
	handleLoading(false);
}

export function onUnauthorized() {
	ldodEventPublisher('logout');
	navigateTo('/');
}

export async function handleRequest(url, options) {
	try {
		const res = await fetch(url, options);
		const resData = await res.json();
		if (!res.ok) {
			if (res.status === 401) onUnauthorized();
			else handleError(resData?.message);
			return Promise.reject(resData || res);
		}
		return resData;
	} catch (error) {
		onRequestError(error);
		return Promise.reject(error);
	} finally {
		handleLoading(false);
	}
}
