/** @format */

import { isDev } from '../../utils.js';
import './frag-orchestrator/frag-orchestrator.js';

let virtual;

async function loadVirtualFrags() {
	if (virtual || !window.mfes?.includes('virtual')) return;
	virtual = isDev()
		? await import('virtual/virtual-dev.js').catch(console.error)
		: await import('virtual').catch(console.error);
	virtual?.loadFragment();
}

const mount = async (lang, ref) => {
	const element = document.querySelector(ref);
	element.innerHTML = /*html*/ `<frag-orchestrator language="${lang}"></frag-orchestrator>`;
	loadVirtualFrags();
};

const unMount = () => customElements.get('frag-orchestrator')?.instance?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
