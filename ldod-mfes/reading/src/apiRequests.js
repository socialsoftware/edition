import { fetcher } from 'shared/fetcher.js';
import { getState } from './store';

const READING_HOST = `${import.meta.env.VITE_HOST}/reading`;

export async function getStartReading() {
  return await fetcher.get(READING_HOST, null);
}

export async function getExpertEditionInter(xmlId, urlId) {
  return await fetcher.post(
    `${READING_HOST}/fragment/${xmlId}/inter/${urlId}`,
    getState()
  );
}
