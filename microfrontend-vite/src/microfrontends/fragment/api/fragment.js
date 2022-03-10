import fetcher from '../../../config/axios';
import { FragmentNavData, extractData } from '../dataExtraction';
import {
  getVirtualEditionsAcronyms,
  setFragmentInter,
  setFragmentNavData,
  getCheckboxesState,
} from '../fragmentStore';
const FRAGMENT_BASE_URL = '/microfrontend/fragment';

export const getNoAuthFragment = (xmlid) =>
  fetcher
    .post(
      `${FRAGMENT_BASE_URL}/virtual/${xmlid}`,
      getVirtualEditionsAcronyms()
    )
    .then(({ data }) => {
      setFragmentNavData(FragmentNavData(data));
    })
    .catch((err) => {
      console.error(err);
    });


export const getFragmentInter = (xmlid, urlid, state) => {

  fetcher
    .post(
      `${FRAGMENT_BASE_URL}/virtual/no-auth/${xmlid}/inter/${urlid}/${
        state?.prevNext ?? ''
      }`,
      {
        selectedVE: getVirtualEditionsAcronyms(),
        ...getCheckboxesState(),
        inters: state?.inters ?? [],
      }
    )
    .then(({ data }) => {
      setFragmentNavData(FragmentNavData(data));
      setFragmentInter(extractData(data, state));
    })
    .catch((err) => {
      setFragmentInter();
      getNoAuthFragment(xmlid);
      console.error(err);
    });
};
