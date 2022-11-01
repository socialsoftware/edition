import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/restricted`;

export const createVirtualEdition = async (body) =>
  await fetcher.post(`${HOST}/create`, body, window.token).then((data) => {
    if (data?.ok === false) return Promise.reject(data);
    return Promise.resolve(data);
  });

export const toggleSelectedVE = async ({ externalId, selected }) =>
  await fetcher
    .get(
      `${HOST}/update-selected-ve/${selected}/${externalId}`,
      null,
      window.token
    )
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const submitParticipation = async (externalId) =>
  await fetcher
    .get(`${HOST}/participants/submit/${externalId}`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const cancelParticipation = async (externalId) =>
  await fetcher
    .get(`${HOST}/participants/cancel/${externalId}`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const veAdminDelete = async (id) =>
  await fetcher.get(`${HOST}/delete/${id}`, null, window.token).then((data) => {
    if (data?.ok === false) return Promise.reject(data);
    return Promise.resolve(data);
  });

export const getVirtualEdition = async (id) =>
  await fetcher
    .get(`${HOST}/virtual-edition/${id}`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getEditionGames = async (id) =>
  await fetcher.get(`${HOST}/${id}/games`, null, window.token);

export const approveParticipant = async (id, username) =>
  await fetcher
    .get(`${HOST}/${id}/participants/${username}/approve`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const addParticipant = async (id, username) =>
  await fetcher
    .get(`${HOST}/${id}/participants/${username}/add`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const removeParticipant = async (id, username) =>
  await fetcher
    .get(`${HOST}/${id}/participants/${username}/remove`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const switchMemberRole = async (id, username) =>
  await fetcher
    .get(`${HOST}/${id}/participants/${username}/role`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const removeClassGame = async (id, gameId) =>
  await fetcher
    .get(`${HOST}/${id}/games/${gameId}/remove`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const createClassGame = async (id, gameBody) =>
  await fetcher
    .post(`${HOST}/${id}/games/create`, gameBody, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const editVE = async (id, veBody) =>
  await fetcher
    .post(`${HOST}/edit/${id}`, veBody, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVeIntersWithRecommendation = async (id) =>
  await fetcher
    .get(`${HOST}/assisted/${id}`, null, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const setLinearVE = async (id, body) =>
  await fetcher
    .post(`${HOST}/assisted/${id}/linear`, body, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const saveLinerVE = async (id, body) =>
  await fetcher
    .post(`${HOST}/assisted/${id}/linear/save`, body, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVeIntersForManual = async (id) =>
  await fetcher.get(`${HOST}/manual/${id}`, null, window.token).then((data) => {
    if (data?.ok === false) return Promise.reject(data);
    return Promise.resolve(data);
  });

export const saveReorderedVEInters = async (id, inters) =>
  await fetcher
    .post(`${HOST}/manual/${id}/mutate`, inters, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });
