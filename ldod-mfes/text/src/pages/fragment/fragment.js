import { getFragment } from '@src/api-requests.js';
import { isDev } from '../../utils.js';
import { LdodFragment } from './ldod-fragment.jsx';
import { getNewInter, isVirtualInter } from './utils.js';

let virtual;
async function loadVirtualFrags() {
	if (virtual) return;
	virtual =
		window.mfes?.includes('virtual') && isDev()
			? await import('virtual/virtual-dev.js').catch(e => console.error(e))
			: await import('virtual').catch(e => console.error(e));

	virtual.default.bootstrap();
}

const mount = async (lang, ref) => {
	const { xmlId, urlId } = history.state;
	let data;
	if (!xmlId) data = '';
	else data = urlId && !isVirtualInter(urlId) ? await getNewInter(xmlId, urlId) : await getFragment(xmlId);
	document.querySelector(ref).appendChild(new LdodFragment(lang, data, xmlId, urlId));
	loadVirtualFrags();
};

const unMount = () => document.querySelector('ldod-fragment')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
