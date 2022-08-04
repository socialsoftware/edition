import { fetcher } from 'shared/fetcher.js';
import { getState, setState } from './store';
import { emitMessageEvent, eventEmiter } from './utils';
import { navigateTo } from 'shared/router.js';

const HOST = import.meta.env.VITE_HOST;

export const authRequest = async (data) => {
  await fetcher.post(`${HOST}/auth/sign-in`, data).then((res) => {
    if (res.message)
      eventEmiter('ldod-error', { detail: { message: res.message } });
    login(res.accessToken);
  });
};

export const userRequest = async (token) => {
  await fetcher
    .get(`${HOST}/user`, null, token)
    .then((user) => setUser(user))
    .catch((error) => error.message === 'unauthorized' && logout());
};

export const signupRequest = async (data) =>
  await fetcher.post(`${HOST}/auth/sign-up`, data);

export const socialAuthRequest = async (path, data) =>
  fetcher.post(`${HOST}/auth/${path}`, data).then((response) => {
    if (isFormState(response)) {
      navigateTo('/user/signup', this, response);
      return Promise.resolve({ message: 'googleAssociation' });
    }
    isAccessToken(response) && login(response.accessToken);
  });

export const tokenConfirmRequest = async (path) =>
  await fetcher.get(`${HOST}/auth${path}`);

export const tokenAuthRequest = async (path) => {
  if (import.meta.env.PROD && !getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  await fetcher.get(`${HOST}/admin/user${path}`, null, getState().token);
};

export const changePasswordRequest = async (data) =>
  fetcher.post(`${HOST}/user/change-password`, data).then((res) => {
    navigateTo('/');
    return Promise.resolve(res);
  });

export const getUsersList = async () => {
  if (!getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  return await fetcher.get(`${HOST}/admin/user/list`, null, getState().token);
};

export const switchModeRequest = async () =>
  await fetcher.post(`${HOST}/admin/user/switch`);

export const deleteSessionsRequest = async () =>
  await fetcher.post(`${HOST}/admin/user/sessions-delete`);

export const changeActiveRequest = async (externalId) =>
  await fetcher.post(`${HOST}/admin/user/active/${externalId}`);

export const removeUserRequest = async (externalId) =>
  await fetcher.post(`${HOST}/admin/user/delete/${externalId}`);

function isAccessToken(response) {
  return Object.keys(response).some(
    (key) => key === 'accessToken' || key === 'tokenType'
  );
}

function isFormState(response) {
  return Object?.keys(response).some((key) => key === 'socialId');
}

export function login(token) {
  if (!token) return logout();
  setState({ token });
  eventEmiter('ldod-token', { detail: { token } });
}

export function logout() {
  setState({ token: '', user: '' });
  eventEmiter('ldod-logout');
  navigateTo('/user/signin');
}

export function setUser(user) {
  setState({ user });
  eventEmiter('ldod-login', { detail: { user } });
}
