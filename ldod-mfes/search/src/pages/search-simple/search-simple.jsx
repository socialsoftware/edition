/** @format */

import './ldod-search-simple';

const mount = async (lang, ref) => {
	document
		.querySelector(ref)
		.appendChild(<ldod-search-simple language={lang}></ldod-search-simple>);
};

const unMount = () => document.querySelector('ldod-search-simple')?.remove();

const path = '/simple';

export { mount, unMount, path };
