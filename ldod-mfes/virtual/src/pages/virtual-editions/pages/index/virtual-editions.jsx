/** @format */

import { getVirtualEditions } from '@src/api-requests.js';
import './ldod-virtual-editions.jsx';

const mount = async (lang, ref) => {
	const virtualEditionsData = await getVirtualEditions();
	if (virtualEditionsData)
		document
			.querySelector(ref)
			.appendChild(
				<ldod-virtual-editions
					language={lang}
					virtualEditions={virtualEditionsData.virtualEditions}
					user={virtualEditionsData.user}
					selectedVE={virtualEditionsData.user?.selectedVE || []}></ldod-virtual-editions>
			);
};

const unMount = () => document.querySelector('ldod-virtual-editions')?.remove();

const path = '/virtual-editions';

export const index = () => ({
	mount,
	unMount,
	path,
});
