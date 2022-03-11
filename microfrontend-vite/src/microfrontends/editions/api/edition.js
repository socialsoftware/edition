import fetcher from '../../../config/axios';
import { setEdition } from '../editionStore';
import { ExpertEditionEntry } from '../Models/ExpertEdition';
import { Editors, Title, VirtualEditionEntry } from '../Models/VirtualEdition';
const EDITION_BASE_URL = '/microfrontend/edition';

export const getExpertEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/expert/acronym/${acronym}`)
    .then(({ data }) => {
      setEdition({
        ...data,
        type:"EXPERT",
        tableData: data?.sortedInterpsList?.map((entry) =>
          ExpertEditionEntry(entry)
        ),
      });
    })
    .catch((err) => console.error(err));

export const getVirtualEdition = (acronym) =>
  fetcher
    .get(`${EDITION_BASE_URL}/virtual/acronym/${acronym}`)
    .then(({ data }) =>
      setEdition({
        ...data,
        title: Title(data),
        editors: Editors(data),
        type: 'VIRTUAL',
        tableData: data?.sortedInterpsList?.map((entry) =>
          VirtualEditionEntry(entry)
        ),
      })
    )
    .catch((err) => console.error(err));
