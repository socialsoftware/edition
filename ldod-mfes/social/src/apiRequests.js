import { fetcher } from 'shared/fetcher.js';

const HOST = import.meta.env.VITE_HOST;

export const getCitationsList = async () =>
  await fetcher.get(`${HOST}/social/twitter-citations`, null);

export const getTweetsToManage = async () =>
  await fetcher.get(`${HOST}/admin/social/tweets`, null);

export const generateCitations = async () =>
  await fetcher.post(`${HOST}/admin/social/tweets/generateCitations`, {});

export const removeTweets = async () =>
  await fetcher.post(`${HOST}/admin/social/remove-tweets`, {});
