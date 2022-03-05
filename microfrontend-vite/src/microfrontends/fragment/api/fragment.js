import fetcher from '../../../config/axios';
import FragmentExpert from '../components/FragmentExpert';
import { FragmentExpertData, FragmentNavData, FragmentSourceData } from '../dataExtraction';
import {
  getVirtualEditionsAcronyms,
  setFragmentSource,
  setFragmentNavData,
  setFragmentExpert,
} from '../fragmentStore';
const FRAGMENT_BASE_URL = '/microfrontend/fragment';

export const getNoAuthFragment = (xmlid) =>
  fetcher
    .post(
      `${FRAGMENT_BASE_URL}/virtual/no-auth/${xmlid}`,
      getVirtualEditionsAcronyms()
    )
    .then(({ data }) =>  setFragmentNavData(FragmentNavData(data)))
    .catch((err) => {
      console.error(err);
    });

export const getNoAuthFragmentInter = (xmlid, urlid) =>
  fetcher
    .post(
      `${FRAGMENT_BASE_URL}/virtual/no-auth/${xmlid}/inter/${urlid}`,
      getVirtualEditionsAcronyms()
    )
    .then(({ data }) => {
      setFragmentNavData(FragmentNavData(data));
      setFragmentSource(FragmentSourceData(data));
      setFragmentExpert(FragmentExpertData(data));
    })
    .catch((err) => {
      console.error(err);
    });
