import { fetcher } from 'shared/fetcher.js';

const HOST = import.meta.env.VITE_HOST;

export const getVirtualEditions = async () =>
  await fetcher.get(`${HOST}/virtual/virtual-editions`, null, window.token);

export const createVirtualEdition = async (body) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/create`, body, window.token)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVirtualEditions4Manage = async () =>
  await fetcher.get(`${HOST}/virtual/admin/virtual-editions`, null);

export const deleteVE = async (id) =>
  await fetcher.get(`${HOST}/virtual/admin/virtual-edition-delete/${id}`, null);

export const toggleSelectedVE = async ({ externalId, selected }) =>
  await fetcher
    .get(
      `${HOST}/virtual/restricted/update-selected-ve/${selected}/${externalId}`,
      null
    )
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const submitParticipation = async (externalId) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/participants/submit/${externalId}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const cancelParticipation = async (externalId) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/participants/cancel/${externalId}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const veAdminDelete = async (id) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/delete/${id}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVirtualEdition = async (id) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/virtual-edition/${id}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getEditionGames = async (id) =>
  await fetcher.get(`${HOST}/virtual/restricted/${id}/games`, null);

export const approveParticipant = async (id, username) =>
  await fetcher
    .get(
      `${HOST}/virtual/restricted/${id}/participants/${username}/approve`,
      null
    )
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const addParticipant = async (id, username) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/${id}/participants/${username}/add`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const removeParticipant = async (id, username) =>
  await fetcher
    .get(
      `${HOST}/virtual/restricted/${id}/participants/${username}/remove`,
      null
    )
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const switchMemberRole = async (id, username) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/${id}/participants/${username}/role`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const removeClassGame = async (id, gameId) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/${id}/games/${gameId}/remove`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const createClassGame = async (id, gameBody) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/${id}/games/create`, gameBody)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const editVE = async (id, veBody) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/edit/${id}`, veBody)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVeIntersWithRecommendation = async (id) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/assisted/${id}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const setLinearVE = async (id, body) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/assisted/${id}/linear`, body)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const saveLinerVE = async (id, body) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/assisted/${id}/linear/save`, body)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVeIntersForManual = async (id) =>
  await fetcher
    .get(`${HOST}/virtual/restricted/manual/${id}`, null)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const saveReorderedVEInters = async (id, inters) =>
  await fetcher
    .post(`${HOST}/virtual/restricted/manual/${id}/mutate`, inters)
    .then((data) => {
      if (data?.ok === false) return Promise.reject(data);
      return Promise.resolve(data);
    });

export const getVirtualEditionByAcronym = async (acrn) =>
  await fetcher.get(`${HOST}/virtual/edition/acronym/${acrn}`, null);

export const getVeUser = async (username) =>
  await fetcher.get(`${HOST}/virtual/edition/user/${username}`, null);

export const getCategoryData = async (acronym, category) =>
  await fetcher.get(
    `${HOST}/virtual/edition/acronym/${acronym}/category/${category}`,
    null
  );

export const getVeTaxonomy = async (acronym) =>
  await fetcher.get(
    `${HOST}/virtual/edition/acronym/${acronym}/taxonomy`,
    null
  );

export const getVeGame = async (gameId) =>
  await fetcher.get(`${HOST}/virtual/virtual-editions/game/${gameId}`, null);
