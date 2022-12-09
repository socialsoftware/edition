const text = window?.references?.text || {};
/**
 * 
 * @param {string} xmlId fragment xmlId
 * @param {string} urlId fragment interpretation urlId
 * @returns {string} path to text fragment interpretation
 */
export const textFragmentInter = (xmlId, urlId) => text.fragmentInter?.(xmlId, urlId);