/** @format */

import { ldodEventBus, loginSubscriber, logoutSubscriber } from './events-modules';
import { isAuth, userFullName } from './store';
import { userReferences } from './user-references';

const authMenuPayload = {
	true: {
		links: [
			{ key: 'changePassword', path: userReferences.password() },
			{ key: 'logout', path: userReferences.logout() },
		],
		constants: {
			pt: {
				logout: 'Sair',
				changePassword: 'Alterar Senha',
			},
			en: {
				logout: 'Logout',
				changePassword: 'Change Password',
			},
			es: {
				logout: 'Cerrar',
				changePassword: 'Cambiar la Contraseña',
			},
		},
	},
	false: {
		links: [{ key: 'login', path: userReferences.signin() }],
		constants: {
			pt: {
				login: 'Iniciar Sessão',
			},
			en: {
				login: 'Login',
			},
			es: {
				login: 'Iniciar Sesión',
			},
		},
	},
};

function updateMenuComponent(isAuth, data) {
	ldodEventBus.publish('navbar:auth-status', { isAuth, data, name: userFullName() });
}

loginSubscriber(() => updateMenuComponent(true, authMenuPayload[true]));
logoutSubscriber(() => updateMenuComponent(false, authMenuPayload[false]));

customElements.whenDefined('nav-bar').then(() => {
	updateMenuComponent(isAuth(), authMenuPayload[isAuth() ? true : false]);
});
