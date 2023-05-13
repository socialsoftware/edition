/** @format */

import { fetcher } from '@core';

const PATH = `/virtual/restricted`;

export const createVirtualEdition = async body =>
	await fetcher.post(`${PATH}/create`, body, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const toggleSelectedVE = async ({ externalId, selected }) =>
	await fetcher
		.get(`${PATH}/update-selected-ve/${selected}/${externalId}`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const submitParticipation = async externalId =>
	await fetcher
		.get(`${PATH}/participants/submit/${externalId}`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const cancelParticipation = async externalId =>
	await fetcher
		.get(`${PATH}/participants/cancel/${externalId}`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const veAdminDelete = async id =>
	await fetcher.get(`${PATH}/delete/${id}`, null, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const getVirtualEdition = async id =>
	await fetcher.get(`${PATH}/virtual-edition/${id}`, null, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const getEditionGames = async id =>
	await fetcher.get(`${PATH}/${id}/games`, null, window.token);

export const approveParticipant = async (id, username) =>
	await fetcher
		.get(`${PATH}/${id}/participants/${username}/approve`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const addParticipant = async (id, username) =>
	await fetcher
		.get(`${PATH}/${id}/participants/${username}/add`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const removeParticipant = async (id, username) =>
	await fetcher
		.get(`${PATH}/${id}/participants/${username}/remove`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const switchMemberRole = async (id, username) =>
	await fetcher
		.get(`${PATH}/${id}/participants/${username}/role`, null, window.token)
		.then(data => {
			if (data?.ok === false) return Promise.reject(data);
			return Promise.resolve(data);
		});

export const removeClassGame = async (id, gameId) =>
	await fetcher.get(`${PATH}/${id}/games/${gameId}/remove`, null, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const createClassGame = async (id, gameBody) =>
	await fetcher.post(`${PATH}/${id}/games/create`, gameBody, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const editVE = async (id, veBody) =>
	await fetcher.post(`${PATH}/edit/${id}`, veBody, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const getVeIntersWithRecommendation = async id =>
	await fetcher.get(`${PATH}/assisted/${id}`, null, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const setLinearVE = async (id, body) =>
	await fetcher.post(`${PATH}/assisted/${id}/linear`, body, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const saveLinerVE = async (id, body) =>
	await fetcher.post(`${PATH}/assisted/${id}/linear/save`, body, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const getVeIntersForManual = async id =>
	await fetcher.get(`${PATH}/manual/${id}`, null, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});

export const saveReorderedVEInters = async (id, inters) =>
	await fetcher.post(`${PATH}/manual/${id}/mutate`, inters, window.token).then(data => {
		if (data?.ok === false) return Promise.reject(data);
		return Promise.resolve(data);
	});
