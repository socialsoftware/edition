import fetcher from '../../../config/axios';
import { TwiterCitation } from '../CitationModel';
import { setCitations } from '../readingStore';

const READING_BASE_URL = '/microfrontend/reading';

export const getReadingExperts = async () =>
  await fetcher.get(`${READING_BASE_URL}`);

export const getStartReadingFragment = async (acronym, data) =>
  await fetcher.post(`${READING_BASE_URL}/edition/${acronym}/start`, data);

export const getPrevNextReadingFragment = async (xmlid, urlid, data, type) =>
  await fetcher.post(
    `${READING_BASE_URL}/fragment/${xmlid}/inter/${urlid}/${type}`,
    data
  );

export const getCurrentReadingFragment = async (xmlid, urlid, data) =>
  await fetcher.post(
    `${READING_BASE_URL}/fragment/${xmlid}/interJson/${urlid}`,
    data
  );

export const resetPrevRecom = async (data) =>
  await fetcher.post(`${READING_BASE_URL}/inter/prev/recom/reset`, data);

export const getPrevRecom = async (data) =>
  await fetcher.post(`${READING_BASE_URL}/inter/prev/recom`, data);

export const getTwitterCitations = () =>
  fetcher
    .get(`${READING_BASE_URL}/citations`)
    .then(({ data }) =>
      setCitations(data?.map((entry) => TwiterCitation(entry)))
    )
    .catch((err) => {
      console.error(err);
    });
