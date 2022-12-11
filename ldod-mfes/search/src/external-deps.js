const text = window?.references?.text;

/**
 *
 * @param {string} xmlId
 * @returns {string}
 */
export const textFrag = (xmlId) => text.fragment?.(xmlId);

/**
 *
 * @param {string} xmlId
 * @param {string} urlId
 * @returns {string}
 */
export const textFragInter = (xmlId, urlId) =>
  text.fragmentInter?.(xmlId, urlId);
