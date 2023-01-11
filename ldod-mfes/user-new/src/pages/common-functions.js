import { userRequest } from '../api-requests';
import { loginPublisher, logoutPublisher } from '../events-modules';
import { getState, setState } from '../store';
import { navigateTo } from '@shared/router.js';

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
	login();
}

export function onAuthFail(message) {
	errorPublisher(message);
	setState({ token: '' });
}

export function logout() {
	setState({ token: '', user: '' });
	logoutPublisher();
	navigateTo('/');
}

export function login() {
	userRequest(getState().token)
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
