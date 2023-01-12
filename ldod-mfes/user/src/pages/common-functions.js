import { userRequest } from '../api-requests';
import { errorPublisher, loginPublisher, logoutPublisher, messagePublisher, tokenPublisher } from '../events-modules';
import { getState, setState } from '../store';
import { navigateTo } from '@shared/router.js';
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

export function onAuthFail(message) {
	errorPublisher(message);
	setState({ token: '' });
}

export function onSignup(message) {
	messagePublisher(message);
	navigateTo(userReferences.signin());
}

export function logout() {
	setState({ token: '', user: '' });
	logoutPublisher('token absent');
	navigateTo('/');
}

export function login(token) {
	userRequest(token)
		.then(user => {
			setState({ user });
			loginPublisher(user);
			location.pathname.endsWith('/user/signin') && navigateTo('/');
		})
		.catch(error => error.message === 'unauthorized' && logout());
}

export function resetForm(form) {
	form.classList.remove('was-validated');
	form.reset();
}
