/** @format */

import { getExpertEditionByAcrn } from '@src/api-requests';
import { LdodEditionAcrn } from './ldod-edition-acrn';

const path = '/acronym/:acrn';

const mount = async (lang, ref) => {
	const acrn = history.state?.acrn;
	const data = await getExpertEditionByAcrn(acrn);
	if (data) document.querySelector(ref).appendChild(new LdodEditionAcrn(lang, data));
};

const unMount = () => document.querySelector('ldod-edition-acrn')?.remove();

export { path, mount, unMount };
