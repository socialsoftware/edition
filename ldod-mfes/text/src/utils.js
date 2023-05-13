/** @format */

import textReferences from './references';
export const isDev = () => import.meta.env.DEV;

export function parseRawHTML(rawHTML) {
	return document.createRange().createContextualFragment(rawHTML);
}

export function processFragmentAnchor(docFrag) {
	docFrag.querySelectorAll('a[href]').forEach(anchor => {
		if (anchor.href.includes('/fragments/fragment/Fr')) {
			const xmlId = anchor.href.slice(anchor.href.indexOf('Fr'));
			const newAnchor = document.createElement('a', { is: 'nav-to' });
			newAnchor.setAttribute('to', textReferences.fragment(xmlId));
			newAnchor.innerHTML = anchor.innerHTML;
			anchor.replaceWith(newAnchor);
		}
	});
	return docFrag;
}

/**
 *
 * @param {string} word
 */
export function capitalize(word) {
	if (!word) return;
	return word[0].toUpperCase().concat(word.slice(1));
}

export const sleep = async ms => new Promise(r => setTimeout(r, ms));
export const htmlRender = (...rawHtml) => document.createRange().createContextualFragment(rawHtml);
