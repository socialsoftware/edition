import { fetcher } from 'shared/fetcher.js';
import { setState } from './store';
import { eventEmiter } from './utils';
import { navigateTo } from 'shared/router.js';

const HOST = import.meta.env.VITE_HOST;

export const authRequest = async (data) => {
  await fetcher.post(`${HOST}/auth/signin`, data).then((res) => {
    if (res.message)
      eventEmiter('ldod-error', { detail: { message: res.message } });
    login(res.accessToken);
  });
};

export const userRequest = async (token) => {
  await fetcher
    .get(`${HOST}/user`, null, token)
    .then((user) => setUser(user))
    .catch((error) => console.error(error));
};

export const signupRequest = async (data) =>
  await fetcher.post(`${HOST}/auth/signup`, data);

export const socialAuthRequest = async (path, data) =>
  fetcher.post(`${HOST}/auth/${path}`, data).then((response) => {
    if (isFormState(response)) {
      navigateTo('/user/signup', this, response);
      return Promise.resolve({ message: 'googleAssociation' });
    }
    isAccessToken(response) && login(response.accessToken);
  });

export const tokenAuthRequest = async (path) =>
  await fetcher.get(`${HOST}/auth/signup${path}`);

export const changePasswordRequest = async (data) =>
  fetcher.post(`${HOST}/auth/change-password`, data).then((res) => {
    navigateTo('/');
    return Promise.resolve(res);
  });

export const getUsersList = async () =>
  await fetcher.get(`${HOST}/user/admin/list`);

export const switchMode = async () =>
  await fetcher.post(`${HOST}/user/admin/switch`);

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
