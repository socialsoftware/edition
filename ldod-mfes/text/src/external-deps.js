const reading = window.references?.reading || {};
const virtual = window.references?.virtual || {};

/**
 * Returns the path to the virtual edition with acronym acrn
 * @param {string} acrn virtual edition acronym
 * @returns {string}
 */
export const virtualEdition = (acrn) => virtual.virtualEdition?.(acrn);

/**
 *
 * @param {string} xmlId
 * @param {string} urlId
 * @returns
 */
export const readingExpertEdition = (xmlId, urlId) =>
  reading.editionInterPath?.(xmlId, urlId);
