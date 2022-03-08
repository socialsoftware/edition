import fetcher from '../../../config/axios';
import { setEdition } from '../editionStore';
import { ExpertEditionEntry } from '../Models/ExpertEdition';
const EDITION_BASE_URL = '/microfrontend/edition';

export const getExpertEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/expert/acronym/${acronym}`)
    .then(({ data }) => {

      setEdition({
        ...data,
        tableData: data?.sortedInterpsList?.map((entry) =>
          ExpertEditionEntry(entry)
        ),
      });
    })
    .catch((err) => console.error(err));

export const getVirtualEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/virtual/acronym/${acronym}`)
    .then(({ data }) => setEdition(data))
    .catch((err) => console.error(err));
