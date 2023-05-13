/** @format */

import { fetcher } from '@core';

const PATH = `/virtual/virtual-editions`;

export const getVirtualEditions = async () => await fetcher.get(PATH, null, window.token);

export const getVeGame = async gameId => await fetcher.get(`${PATH}/game/${gameId}`, null);
