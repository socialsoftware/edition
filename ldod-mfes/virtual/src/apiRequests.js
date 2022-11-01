import { fetcher } from 'shared/fetcher.js';

const HOST = `${import.meta.env.VITE_HOST}/virtual/virtual-editions`;

export const getVirtualEditions = async () =>
  await fetcher.get(HOST, null, window.token);

export const getVeGame = async (gameId) =>
  await fetcher.get(`${HOST}/game/${gameId}`, null);
