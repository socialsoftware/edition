const textReferences = window?.references?.text || {};

/**
 *
 * @param {string} xmlId
 * @param {string} urlId
 * @returns {string}
 */
export const textFragInter = (xmlId, urlId) =>
  textReferences.fragmentInter?.(xmlId, urlId);
