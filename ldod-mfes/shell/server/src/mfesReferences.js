import { htmlPath, staticPath } from './constants.js';
import { parse } from 'node-html-parser';
import fs from 'fs';
import { loadMfesFromJson } from './mfes.js';
import { getIndexHtml } from './static.js';

export async function processReferences() {
	const references = await getMfesReferences();
	writeReferencesToFile(references);
	appendReferencesScriptTagOnIndexHTML();
	global.globalReferences = await references;
}

async function getMfesReferences() {
	const mfes = loadMfesFromJson();
	return await mfes.reduce(async (refs, mfe) => {
		let result = await refs;
		const entryPoint = `${staticPath}/${mfe}/${mfe}.js`;
		const api = await import(entryPoint).catch(onError);
		if (!api) return;

		result = { ...result, [mfe]: api.default.references };
		return result;
	}, Promise.resolve({}));
}
function writeReferencesToFile(references) {
	fs.writeFileSync(`${staticPath}/references.js`, generateReferencesJsCode(references));
}

function appendReferencesScriptTagOnIndexHTML() {
	const dom = parse(getIndexHtml());
	const src = '/ldod-mfes/references.js';
	let script = dom.querySelector('head script#mfes-references');
	if (script && script.attributes.src === src) return;
	script = parse(`<script id="mfes-references" src="${src}"></script>`);
	dom.querySelector('head').appendChild(script);
	fs.writeFileSync(htmlPath, dom.toString());
}

function generateReferencesJsCode(references) {
	return `
	if (typeof window !== "undefined") {
	  window.references = {
		${Object.keys(references || [])
			.map(key => {
				if (!references[key]) return `${key}: ${null}`;
				return `${key}: {
					${Object.entries(references[key])
						.map(([fnKey, fn]) => {
							return `${fnKey}: ${fn}`;
						})
						.join(',\n')}
				}`;
			})
			.join(',\n')}
	  }
	}
	`;
}

function onError(e) {
	console.error(e);
}
