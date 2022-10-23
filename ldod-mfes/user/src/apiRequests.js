import { fetcher } from 'shared/fetcher.js';
import { getState, setState } from './store';
import { emitMessageEvent, eventEmiter } from './utils';
import { navigateTo } from 'shared/router.js';

const HOST = import.meta.env.VITE_HOST;

/*export const authRequest = async (data) => {
  await fetcher.post(`${HOST}/auth/sign-in`, data).then((res) => {
    if (res.message)
      eventEmiter('ldod-error', { detail: { message: res.message } });
    login(res.accessToken);
  });
};*/

export const newAuthRequest = async (data) =>
  await fetcher.post(`${HOST}/auth/sign-in`, data);

export const userRequest = async (token) =>
  await fetcher.get(`${HOST}/user`, null, token);

export const signupRequest = async (data) =>
  await fetcher.post(`${HOST}/auth/sign-up`, data);

export const socialAuthRequest = async (path, data, loginCB) =>
  fetcher.post(`${HOST}/auth/${path}`, data).then((response) => {
    if (isFormState(response)) {
      navigateTo('/user/signup', this, response);
      return Promise.resolve({ message: 'googleAssociation' });
    }
    isAccessToken(response) && loginCB(response.accessToken);
  });

export const tokenConfirmRequest = async (path) =>
  await fetcher.get(`${HOST}/auth${path}`, null);

export const tokenAuthRequest = async (path) => {
  if (import.meta.env.PROD && !getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  return await fetcher.get(`${HOST}/admin/user${path}`, null, getState().token);
};

export const changePasswordRequest = async (data) =>
  fetcher.post(`${HOST}/user/change-password`, data, getState().token);

export const getUsersList = async () => {
  if (!getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  return await fetcher.get(`${HOST}/admin/user/list`, null, getState().token);
};

export const switchModeRequest = async () =>
  await fetcher.post(`${HOST}/admin/user/switch`, null, getState().token);

export const deleteSessionsRequest = async () =>
  await fetcher.post(
    `${HOST}/admin/user/sessions-delete`,
    null,
    getState().token
  );

export const changeActiveRequest = async (externalId) =>
  await fetcher.post(`${HOST}/admin/user/active/${externalId}`);

export const removeUserRequest = async (externalId) =>
  await fetcher.post(`${HOST}/admin/user/delete/${externalId}`);

export const updateUserRequest = async (data) =>
  await fetcher.post(`${HOST}/admin/user/edit`, data);

function isAccessToken(response) {
  return Object.keys(response).some(
    (key) => key === 'accessToken' || key === 'tokenType'
  );
}

function isFormState(response) {
  return Object?.keys(response).some((key) => key === 'socialId');
}
