import { getPartialStorage } from '../../store/store.js';

const headers = new Headers({
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
  Authorization: '',
});

const handleError = (errorData) => {
  const { message } = errorData;
  const errorEv = new CustomEvent('ldod-error', { detail: { message } });
  window.dispatchEvent(errorEv);
};

const handleLoading = (isLoading) =>
  window.dispatchEvent(
    new CustomEvent('ldod-loading', { detail: { isLoading } })
  );

const getStorageToken = () => getPartialStorage('ldod-store', 'token')?.token;

const request = async (method, url, data, token) => {
  handleLoading(true);
  let accessToken;
  const options = {};

  if (data && typeof data !== 'object')
    throw new Error('Data must be an Object');

  options.headers = headers;
  options.method = method;
  if (data) options.body = JSON.stringify(data);
  if (!token) accessToken = getStorageToken();
  else accessToken = token;

  if (accessToken)
    options.headers.set('Authorization', `Bearer ${accessToken}`);
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
