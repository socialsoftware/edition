import fetcher from '../../../config/axios';
import { getVirtualEditions, setFragment, setVirtualFragment } from '../fragmentStore';
const FRAGMENT_BASE_URL = '/microfrontend/fragment';

export const getNoAuthFragment = (xmlid) =>
  fetcher
    .get(`${FRAGMENT_BASE_URL}/no-auth/${xmlid}`)
    .then(({ data }) => setFragment(data))
    .catch((err) => {
      console.error(err);
    });

export const getNoAuthVirtualFragment = (xmlid) =>
  fetcher
    .post(`${FRAGMENT_BASE_URL}/virtual/no-auth/${xmlid}`, getVirtualEditions())
    .then(({ data }) => setVirtualFragment(data))
    .catch((err) => {
      console.error(err);
    });
