/** @format */

const text = window?.references?.text;
export const textFrag = xmlId => text?.fragment?.(xmlId);
export const textFragInter = (xmlId, urlId) => text?.fragmentInter?.(xmlId, urlId);
