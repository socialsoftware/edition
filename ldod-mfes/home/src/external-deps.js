/** @format */

export function getReferences(mfe) {
	return globalThis.references?.[mfe] || {};
}

export const textReferences = getReferences('text');
export const readingReferences = getReferences('reading');
export const searchReferences = getReferences('search');
export const virtualReferences = getReferences('virtual');
export const aboutReferences = getReferences('about');
export const socialReferences = getReferences('social');
export const userReferences = getReferences('user');
