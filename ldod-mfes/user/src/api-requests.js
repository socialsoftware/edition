/** @format */
// Include the typedefs file
/// <reference path="typedef.js" />

import { fetcher, navigateTo } from '@core';
import { getState } from './store';
import { errorPublisher } from './events-modules';
import { userReferences } from './user-references.js';

export const signinRequest = async data => await fetcher.post(`/auth/sign-in`, data);

export async function userRequest(token) {
	return await fetcher.get(`/user`, null, token).then(res => {
		if (!res || res.ok === false) return Promise.reject(res);
		return Promise.resolve(res);
	});
}

export const signupRequest = async data => await fetcher.post(`/auth/sign-up`, data);

export const socialAuthRequest = async (path, data, loginCB) =>
	fetcher
		.post(`/auth/${path}`, data)
		.then(response => {
			if (isFormState(response)) {
				navigateTo(userReferences.signup(), response);
				return Promise.resolve({ message: 'googleAssociation' });
			}
			isAccessToken(response) && loginCB(response.accessToken);
		})
		.catch(e => console.error(e));

export const tokenConfirmRequest = async path => await fetcher.get(`/auth${path}`, null);

export const tokenAuthRequest = async path => {
	if (import.meta.env.PROD && !getState().token) {
		errorPublisher('Not authorized to access resource');
		return navigateTo('/');
	}
	return await fetcher.get(`/admin/user${path}`, null, getState().token);
};

export const changePasswordRequest = async data =>
	fetcher.post(`/user/change-password`, data, getState().token);

/**
 *
 * @returns {Promise<?Array<User>>}
 */
export const getUsersList = async () => {
	if (!getState().token) {
		errorPublisher('Not authorized to access resource');
		return navigateTo('/');
	}
	return await fetcher.get(`/admin/user/list`, null, getState().token);
};

export const switchModeRequest = async () =>
	await fetcher.post(`/admin/user/switch`, null, getState().token);

export const deleteSessionsRequest = async () =>
	await fetcher.post(`/admin/user/sessions-delete`, null, getState().token);

export const changeActiveRequest = async externalId =>
	await fetcher.post(`/admin/user/active/${externalId}`);

export const removeUserRequest = async externalId =>
	await fetcher.post(`/admin/user/delete/${externalId}`);

export const updateUserRequest = async data =>
	await fetcher
		.post(`/admin/user/edit`, data)
		.then(requestInterceptorMiddleware)
		.catch(console.error);

function isAccessToken(response) {
	return Object.keys(response).some(key => key === 'accessToken' || key === 'tokenType');
}

function isFormState(object) {
	return object && 'socialId' in object;
}

function requestInterceptorMiddleware(response) {
	return new Promise((resolve, reject) => {
		if (response.ok === 'false') {
			const message = response.message;
			errorPublisher(message);
			return reject(message);
		}
		return resolve(response);
	});
}
