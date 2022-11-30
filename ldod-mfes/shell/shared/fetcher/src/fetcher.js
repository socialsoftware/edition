import { getPartialStorage } from '../../store/store.js';

const HOST = window.process?.apiHost || 'http://localhost:8000/api';

const customEvent = (type, detail) => new CustomEvent(type, { detail });
const dispatchCustomEvent = (event) => window.dispatchEvent(event);

const handleLoading = (isLoading) =>
  dispatchCustomEvent(customEvent('ldod-loading', { isLoading }));

const handleLogout = (token) =>
  dispatchCustomEvent(customEvent('ldod-logout', { token }));

const handleError = (message) =>
  dispatchCustomEvent(customEvent('ldod-error', { message }));

const getStorageToken = () => getPartialStorage('ldod-store', ['token'])?.token;

const fetchRequest = async (url, options) => {
  try {
    const res = await fetch(url, options);
    handleLoading(false);
    if (res.status === 401) {
      handleLogout();
      return Promise.reject({ message: 'unauthorized' });
    }
    if (res.status === 403 || res.status === 404) {
      handleError(res.message || 'Not authorized to access this resource');
      return Promise.reject();
    }

    if (!res.ok) return Promise.reject();

    return await res.json();
  } catch (error) {
    console.error('FETCH ERROR: ', error);
    handleLoading(false);
    handleError('Something went wrong');
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
