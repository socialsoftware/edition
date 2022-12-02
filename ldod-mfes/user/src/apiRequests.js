import { fetcher } from 'shared/fetcher.js';
import { getState, setState, storage } from './store';
import { emitMessageEvent, tokenEvent } from './utils';
import { navigateTo } from 'shared/router.js';

if (storage?.token) {
  userRequest(getState().token)
    .then((user) => setState({ user }))
    .catch((error) => {
      console.error(error);
      window.dispatchEvent(tokenEvent());
    });
}

export const newAuthRequest = async (data) =>
  await fetcher.post(`/auth/sign-in`, data);

export async function userRequest(token) {
  return await fetcher.get(`/user`, null, token).then((res) => {
    if (res.ok === false) return Promise.reject(res);
    return Promise.resolve(res);
  });
}

export const signupRequest = async (data) =>
  await fetcher.post(`/auth/sign-up`, data);

export const socialAuthRequest = async (path, data, loginCB) =>
  fetcher.post(`/auth/${path}`, data).then((response) => {
    if (isFormState(response)) {
      navigateTo(userReferences.signup, this, response);
      return Promise.resolve({ message: 'googleAssociation' });
    }
    isAccessToken(response) && loginCB(response.accessToken);
  });

export const tokenConfirmRequest = async (path) =>
  await fetcher.get(`/auth${path}`, null);

export const tokenAuthRequest = async (path) => {
  if (import.meta.env.PROD && !getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  return await fetcher.get(`/admin/user${path}`, null, getState().token);
};

export const changePasswordRequest = async (data) =>
  fetcher.post(`/user/change-password`, data, getState().token);

export const getUsersList = async () => {
  if (!getState().token) {
    emitMessageEvent('Not authorized to access resource', 'error');
    return navigateTo('/');
  }
  return await fetcher.get(`/admin/user/list`, null, getState().token);
};

export const switchModeRequest = async () =>
  await fetcher.post(`/admin/user/switch`, null, getState().token);

export const deleteSessionsRequest = async () =>
  await fetcher.post(`/admin/user/sessions-delete`, null, getState().token);

export const changeActiveRequest = async (externalId) =>
  await fetcher.post(`/admin/user/active/${externalId}`);

export const removeUserRequest = async (externalId) =>
  await fetcher.post(`/admin/user/delete/${externalId}`);

export const updateUserRequest = async (data) =>
  await fetcher.post(`/admin/user/edit`, data);

function isAccessToken(response) {
  return Object.keys(response).some(
    (key) => key === 'accessToken' || key === 'tokenType'
  );
}

function isFormState(response) {
  return Object?.keys(response).some((key) => key === 'socialId');
}
