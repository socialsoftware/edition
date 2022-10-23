import { getExpertEditionByAcrn } from '@src/apiRequests';
import { LdodEditionAcrn } from './LdodEditionAcrn';

const path = '/acronym/:acrn';

const mount = async (lang, ref) => {
  const acrn = history.state?.acrn;
  const data = await getExpertEditionByAcrn(acrn);
  document.querySelector(ref).appendChild(new LdodEditionAcrn(lang, data));
};

const unMount = () => document.querySelector('ldod-edition-acrn')?.remove();

export { path, mount, unMount };
