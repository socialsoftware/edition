import { fetcher } from 'shared/fetcher.js';

const PATH = '/social';
const ADMIN_PATH = '/admin/social';

export const getCitationsList = async () =>
  await fetcher.get(`${PATH}/twitter-citations`, null);

export const getTweetsToManage = async () =>
  await fetcher.get(`${ADMIN_PATH}/tweets`, null);

export const generateCitations = async () =>
  await fetcher.post(`${ADMIN_PATH}/tweets/generateCitations`, {});

export const removeTweets = async () =>
  await fetcher.post(`${ADMIN_PATH}/remove-tweets`, {});
