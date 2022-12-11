const text = window?.references?.text || {};

/**
 *
 * @param {string} xmlId
 * @returns {string}
 */
export const textFrag = (xmlId) => text.fragment?.(xmlId);
