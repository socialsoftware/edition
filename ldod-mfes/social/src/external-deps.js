/** @format */

const text = window?.references?.text || {};

export const textFrag = xmlId => text.fragment?.(xmlId);
