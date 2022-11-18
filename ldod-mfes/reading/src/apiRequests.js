import { fetcher } from 'shared/fetcher';

const READING_HOST = `${import.meta.env.VITE_HOST}/reading`;

export async function getStartReading() {
  return await fetcher.get(READING_HOST, null);
}
