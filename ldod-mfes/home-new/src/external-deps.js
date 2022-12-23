async function getReferences(mfe) {
  if (typeof window !== 'undefined') return window?.references?.[mfe] || {};
  else return globalThis.references?.[mfe] || {};
}

export const textReferences = await getReferences('text');
export const readingReferences = await getReferences('reading');
export const searchReferences = await getReferences('search');
export const virtualReferences = await getReferences('virtual');
export const aboutReferences = await getReferences('about');
export const socialReferences = await getReferences('social');
export const userReferences = await getReferences('user');
