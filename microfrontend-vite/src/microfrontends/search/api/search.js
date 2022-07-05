import fetcher from '../../../config/axios';
import { SimpleSearchResultsModel } from '../models/SimpleSearchResults';
import { setSearchResult } from '../searchStore';
const SEARCH_BASE_URL = '/microfrontend/search';

export const getSimpleSearchResults = (searchParameters) =>
  fetcher
    .post(`${SEARCH_BASE_URL}/simple`, searchParameters)
    .then(({ data }) => setSearchResult(SimpleSearchResultsModel(data)))
    .catch(error => console.error(error))
