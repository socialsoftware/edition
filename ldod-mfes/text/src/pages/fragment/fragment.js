/** @format */

import { isDev } from '../../utils.js';
import './frag-orchestrator/frag-orchestrator.js';

let virtual;
const onError = e => console.error(e);
async function loadVirtualFrags() {
	if (virtual) return;
	virtual =
		window.mfes?.includes('virtual') && isDev()
			? await import('virtual/virtual-dev.js').catch(onError)
			: await import('virtual').catch(onError);
	virtual.loadFragment();
}

const mount = async (lang, ref) => {
	const element = document.querySelector(ref);
	element.innerHTML = /*html*/ `<frag-orchestrator language="${lang}"></frag-orchestrator>`;
	loadVirtualFrags();
};

const unMount = () => document.querySelector('ldod-fragment')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
