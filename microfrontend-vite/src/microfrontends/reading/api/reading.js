import fetcher from '../../../config/axios';
import { TwiterCitation } from '../CitationModel';
import { setCitations, setExperts, setFragment, setRecommendation } from '../readingStore';

const READING_BASE_URL = '/microfrontend/reading';

export const getReadingExperts = () =>
  fetcher
    .get(`${READING_BASE_URL}`)
    .then(({ data }) => setExperts(data))
    .catch((err) => console.error(err));

export const getStartReadingFragment = async (acronym, data) =>
  await fetcher
    .post(`${READING_BASE_URL}/edition/${acronym}/start`, data)
    .then(({ data }) => {
      setRecommendation(data?.readingRecommendation);
      setFragment(data);
    })
    .catch((err) => console.error(error));

export const getPrevNextReadingFragment = (xmlid, urlid, data, type) =>
  fetcher
    .post(`${READING_BASE_URL}/fragment/${xmlid}/inter/${urlid}/${type}`, data)
    .then(({ data }) => {
      setRecommendation(data?.readingRecommendation);
      setFragment(data);
    })
    .catch((err) => console.error(err));;

export const getCurrentReadingFragment = async (xmlid, urlid, data) =>
  fetcher
    .post(`${READING_BASE_URL}/fragment/${xmlid}/interJson/${urlid}`, data)
    .then(({ data }) => {
      setRecommendation(data?.readingRecommendation)
      setFragment(data);
    })
    .catch((err) => console.error(err));

export const resetPrevRecom = async (data) =>
  await fetcher.post(`${READING_BASE_URL}/inter/prev/recom/reset`, data);

export const getPrevRecom = (data) =>
  fetcher
    .post(`${READING_BASE_URL}/inter/prev/recom`, data)
    .then(({ data }) => {
      setRecommendation(data?.readingRecommendation)
      setFragment(data);
    })    .catch((err) => console.error(err));

export const getTwitterCitations = () =>
  fetcher
    .get(`${READING_BASE_URL}/citations`)
    .then(({ data }) =>
      setCitations(data?.map((entry) => TwiterCitation(entry)))
    )
    .catch((err) => {
      console.error(err);
    });
