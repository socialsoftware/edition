import fetcher from '../../../config/axios';
import { setData } from '../editionStore';
import { CategoryModel } from '../Models/Category';
import { ExpertEditionModel } from '../Models/ExpertEdition';
import { TaxonomyModel } from '../Models/Taxonomy';
import { UserContributionData } from '../Models/UserContributions';
import { VirtualEditionModel } from '../Models/VirtualEdition';
const EDITION_BASE_URL = '/microfrontend/edition';

export const getExpertEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/expert/acronym/${acronym}`)
    .then(({ data }) => setData(ExpertEditionModel(data)))
    .catch((err) => console.error(err));

export const getVirtualEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/virtual/acronym/${acronym}`)
    .then(({ data }) => setData(VirtualEditionModel(data)))
    .catch((err) => console.error(err));

export const getUserContributions = (username) =>
  fetcher
    .get(`${EDITION_BASE_URL}/user/${username}`)
    .then(({ data }) => setData(UserContributionData(data)))
    .catch((err) => console.error(err));

export const getEditionTaxonomy = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/acronym/${acronym}/taxonomy`)
    .then(({ data }) => setData(TaxonomyModel(data)))
    .catch((err) => console.error(err));

export const getCategoryFrags = (acronym, category) =>
  fetcher
    .get(`${EDITION_BASE_URL}/acronym/${acronym}/category/${category}`)
    .then(({ data }) => setData(CategoryModel(data)))
    .catch((err) => console.error(err));

export const getClassficationGame = (veId, gameId) => {
  fetcher
    .get(`${EDITION_BASE_URL}/game/${veId}/classificationGame/${gameId}`)
    .then(({ data }) => setData(data))
    .catch((err) => console.error(err));
};
