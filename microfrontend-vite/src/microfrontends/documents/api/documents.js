import fetcher from '../../../config/axios';

const DOCUMENTS_BASE_URL = '/microfrontend/documents';
export const getFragmentList = async () =>
  await fetcher.get(`${DOCUMENTS_BASE_URL}/fragments`);

export const getSourceList = async () =>
  await fetcher.get(`${DOCUMENTS_BASE_URL}/sources`);
