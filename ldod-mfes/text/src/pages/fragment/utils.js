/** @format */

import { getFragmentInter } from '../../api-requests';

export const checkBoxes = {
	diff: false,
	del: false,
	sub: false,
	ins: true,
	note: true,
	fac: false,
	line: false,
	align: false,
	pbText: null,
};

export const getNewFragInter = async (xmlId, urlId) =>
	await getFragmentInter(xmlId, urlId, {
		inters: [],
		...checkBoxes,
	});

const isSourceInter = type => type === 'manuscript' || type === 'printed';

export const isSingleAndSourceInter = inters =>
	inters.length === 1 && isSourceInter(inters[0].sourceType);

export const isSingleAndEditorial = inters =>
	inters.length === 1 && !isSourceInter(inters[0].sourceType);

export const isSideBySide = data => data.transcriptions?.length > 1;

export const isLineByLine = data => data.transcriptions?.length === 1 && data.inters?.length > 1;

export const isVirtualInter = urlId =>
	urlId && ['_ED_CRIT_', '_MS_'].every(key => !urlId.includes(key));
