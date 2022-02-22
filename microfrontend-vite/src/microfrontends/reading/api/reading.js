import fetcher from '../../../config/axios';

export const getReadingExperts = async () =>
  await fetcher.get('/microfrontend/reading');

export const getStartReadingFragment = async (acronym, data) => {
  return fetcher.post(`/microfrontend/reading/edition/${acronym}/start`, data);
};

export const getPrevNextReadingFragment = async (xmlid, urlid, data, type) => {
  return fetcher.post(
    `/microfrontend/reading/fragment/${xmlid}/inter/${urlid}/${type}`,
    data
  );
};

export const getCurrentReadingFragment = async (xmlid, urlid, data) => {
  return fetcher.post(
    `/microfrontend/reading/fragment/${xmlid}/interJson/${urlid}`,
    data
  );
};

export const resetPrevRecom = (data) => {
  return fetcher.post(`/microfrontend/reading/inter/prev/recom/reset`, data);
};
export const getPrevRecom = (data) => {
  return fetcher.post(`/microfrontend/reading/inter/prev/recom`, data);
};

export function getTwitterCitations() {
  return fetcher.get(`/microfrontend/reading/citations`);
}
