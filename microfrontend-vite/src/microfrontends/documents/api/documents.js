import fetcher from '../../../config/axios';
import { setEncodedFragments, setSourceList } from '../documentsStore';
import { EncodedFragment } from '../models/Fragment';
import { Source } from '../models/Source';

const DOCUMENTS_BASE_URL = '/microfrontend/documents';
export const getFragmentList = (messages, displayDocument) =>
  fetcher
    .get(`${DOCUMENTS_BASE_URL}/fragments`)
    .then(({ data }) =>
      setEncodedFragments(
        data.map((item) => EncodedFragment(item, messages, displayDocument))
      )
    )
    .catch((err) => {
      console.error(err);
      setEncodedFragments();
    });

export const getSourceList = (messages, displayDocument) =>
  fetcher
    .get(`${DOCUMENTS_BASE_URL}/sources`)
    .then(({ data }) =>
      setSourceList(data.map((item) => Source(item, messages, displayDocument)))
    )
    .catch((err) => {
      console.error(err);
      setSourceList();
    });
