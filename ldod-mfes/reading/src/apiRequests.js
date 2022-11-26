import { fetcher } from 'shared/fetcher.js';
import { getState } from './store';

const PATH = '/reading';

export async function getStartReading() {
  return await fetcher.get(PATH, null);
}

export async function getExpertEditionInter(xmlId, urlId) {
  return await fetcher
    .post(`${PATH}/fragment/${xmlId}/inter/${urlId}`, getState())
    .then((res) => {
      if (res.ok === false) return Promise.reject({ message: res.message });
      return Promise.resolve(res);
    });
}
