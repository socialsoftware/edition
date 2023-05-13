/** @format */

import { userRequest } from '../api-requests';
import {
	errorPublisher,
	loginPublisher,
	logoutPublisher,
	messagePublisher,
	tokenPublisher,
} from '../events-modules';
import { getState, setState } from '../store';
import { navigateTo } from '@core';
import { userReferences } from '../user-references';

export function revealPassword({ target }) {
	getAssociatedPasswordInput(target).type = 'text';
}

export function hidePassword({ target }) {
	getAssociatedPasswordInput(target).type = 'password';
}

function getAssociatedPasswordInput(target) {
	return target.getRootNode().querySelector(target.dataset.input);
}

export function onAuthSuccess(token) {
	if (!token) return logout();
	if (token !== getState().token) setState({ token });
	tokenPublisher(token);
	login(token);
}

export function onAuthFail(error) {
	errorPublisher(error?.message);
	setState({ token: '' });
}

export function onSignup(message) {
	message && messagePublisher(message);
	navigateTo(userReferences.signin());
}

export function onChangePassword(res) {
	if (res.ok === false) return onChangePasswordFail(res);
	messagePublisher(res.message);
	redirectToHome();
}

export function onChangePasswordFail(res) {
	console.error(res);
	errorPublisher(res?.message);
}

export function logout() {
	setState({ token: '', user: '' });
	logoutPublisher();
	redirectToHome();
}

export function login(token) {
	userRequest(token)
		.then(user => {
			setState(state => ({ ...state, user }));
			loginPublisher(user);
			location.pathname.endsWith('/user/signin') && navigateTo('/');
		})
		.catch(error => error.message === 'unauthorized' && logout());
}

export function resetForm(form) {
	form.classList.remove('was-validated');
	form.reset();
}

export function redirectToHome() {
	navigateTo('/');
}
