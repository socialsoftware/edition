import { getPartialStorage } from 'shared/store.js';
import { ldodEventBus } from 'shared/ldod-events.js';
import { navigateTo } from 'shared/router.js';

const HOST = window.process?.apiHost || 'http://localhost:8000/api';

function eventPublisher(eventName, payload) {
  ldodEventBus.publish(`ldod:${eventName}`, payload);
}

const handleLoading = (isLoading) => eventPublisher('loading', isLoading);
const handleLogout = () => eventPublisher('logout');
const handleError = (message) => eventPublisher('error', message);

const getStorageToken = () => getPartialStorage('ldod-store', ['token'])?.token;

const fetchRequest = async (url, options) => {
  try {
    const res = await fetch(url, options);
    handleLoading(false);
    if (res.ok) return await res.json();
    if (res.status === 401) handleLogout();
    handleError(res.message || 'Not authorized to access this resource');
    return Promise.reject();
  } catch (error) {
    console.error('FETCH ERROR: ', error.stack);
    handleLoading(false);
    handleError('Something went wrong');
    navigateTo('/');
  }
};

const request = async (method, path, data, token) => {
  handleLoading(true);
  const options = {};
  const accessToken = token ? token : getStorageToken();
  if (!accessToken) handleLogout();

  if (data && typeof data !== 'object')
    throw new Error('Data must be an Object');

  options.headers = new Headers();
  options.headers.append('Authorization', `Bearer ${accessToken || ''}`);
  options.headers.append('Content-Type', 'application/json');
  options.headers.append('Access-Control-Allow-Origin', '*');

  if (path.includes('restricted') || path.includes('admin'))
    options.headers.append('Cache-Control', 'private');

  options.method = method;
  if (data) options.body = JSON.stringify(data);
  return await fetchRequest(HOST.concat(path), options);
};

export const fetcher = ['get', 'post', 'put', 'delete'].reduce(
  (fetcher, method) => {
    fetcher[method] = (url, data = {}, token) =>
      request(method.toUpperCase(), url, data, token);
    return fetcher;
  },
  {}
);

export const xmlFileFetcher = async ({
  url,
  body,
  method = 'POST',
  token,
  headers = [],
}) => {
  handleLoading(true);
  const options = {};
  const accessToken = token ? token : getStorageToken();
  options.headers = new Headers();
  accessToken && options.headers.set('Authorization', accessToken);
  headers.forEach((header) =>
    Object.entries(header).forEach(([key, value]) => {
      options.headers.set(key, value);
    })
  );
  options.method = method;
  if (body) options.body = body;
  return await fetchRequest(HOST.concat(url), options);
};
