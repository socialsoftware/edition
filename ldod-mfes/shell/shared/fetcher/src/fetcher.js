import { getPartialStorage } from '../../store/store.js';

const handleError = (errorData) => {
  const { message } = errorData;
  const errorEv = new CustomEvent('ldod-error', { detail: { message } });
  window.dispatchEvent(errorEv);
};

const handleLoading = (isLoading) =>
  window.dispatchEvent(
    new CustomEvent('ldod-loading', { detail: { isLoading } })
  );

const getStorageToken = () => getPartialStorage('ldod-store', ['token'])?.token;

const request = async (method, url, data, token) => {
  handleLoading(true);
  const options = {};
  const accessToken = token ? token : getStorageToken();

  options.headers = new Headers();
  options.headers.append(
    'Authorization',
    accessToken ? `Bearer ${accessToken}` : ''
  );

  if (data && typeof data !== 'object')
    throw new Error('Data must be an Object');

  options.headers.append('Content-Type', 'application/json');
  options.headers.append('Access-Control-Allow-Origin', '*');
  options.method = method;
  if (data) options.body = JSON.stringify(data);

  try {
    const res = await fetch(url, options);
    handleLoading(false);
    if (res.status === 401) return Promise.reject({ message: 'unauthorized' });
    if (res.status === 403)
      return handleError({ message: 'Not authorized to access this resource' });
    return await res.json();
  } catch (error) {
    console.log('FETCH ERROR: ', error);
    handleLoading(false);
    handleError({ message: 'Something went wrong' });
  }
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
  options.headers.set('Authorization', accessToken && '');
  headers.forEach((header) =>
    Object.entries(header).forEach(([key, value]) => {
      options.headers.set(key, value);
    })
  );
  options.method = method;
  if (body) options.body = body;

  try {
    const res = await fetch(url, options);
    handleLoading(false);
    return await res.json();
  } catch (error) {
    console.log('FETCH ERROR: ', error);
    handleLoading(false);
    handleError({ message: 'Something went wrong' });
  }
};
