import fs from 'fs';
import { parse } from 'node-html-parser';
import { mfesPath, htmlPath } from './constants.js';
import { getIndexHtml } from './static.js';

export const updateMfesList = async name => {
	let mfes = new Set(loadMfesFromJson());
	name && mfes.add(name);
	saveAndUpdateHTML(JSON.stringify(Array.from(mfes).filter(Boolean)));
};

export const removeFromMfes = name => {
	let mfes = loadMfesFromJson();
	mfes = mfes.filter(mfe => mfe !== name);
	saveAndUpdateHTML(JSON.stringify(mfes));
};

function saveAndUpdateHTML(mfes) {
	saveMfesToJson(mfes);
	appendMfesScriptTag(mfes);
	global.globalMfes = mfes;
}

const appendMfesScriptTag = mfes => {
	const indexHTML = getIndexHtml();
	if (!indexHTML) return;
	const html = parse(indexHTML);
	const mfesScript = html.querySelector('script#mfes');
	if (mfesScript) mfesScript.remove();
	const script = parse(`<script id="mfes">window.mfes = ${mfes}</script>`);
	html.querySelector('head').appendChild(script);
	fs.writeFileSync(htmlPath, html.toString());
};

export function loadMfesFromJson() {
	try {
		return JSON.parse(fs.readFileSync(mfesPath).toString());
	} catch (error) {
		return [];
	}
}

function saveMfesToJson(mfes) {
	fs.writeFileSync(mfesPath, mfes);
}
