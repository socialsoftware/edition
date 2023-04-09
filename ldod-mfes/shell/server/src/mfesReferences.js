/** @format */

import { staticPath } from './constants.js';
import fs from 'fs';
import { loadMfes } from './mfes.js';
import { minify } from 'html-minifier';

export async function generateMfesReferences() {
	const references = await getMfesReferences();
	globalThis.references = references;
	writeReferencesToFile(references);
}

async function getMfesReferences() {
	const mfes = loadMfes();
	return await mfes.reduce(async (refs, mfe) => {
		let result = await refs;
		const entryPoint = `${staticPath}/${mfe}/${mfe}.js`;
		const api = await import(entryPoint).catch(onError);
		if (!api) return;
		result = {
			...result,
			[mfe]: api.default.references,
		};
		return result;
	}, Promise.resolve({}));
}

function writeReferencesToFile(references) {
	fs.writeFileSync(
		`${staticPath}/references.js`,
		minify(generateReferencesJsCode(references), { minifyJS: true, collapseWhitespace: true })
	);
}

function generateReferencesJsCode(references) {
	return `
	if (typeof window !== "undefined") {
	  window.references = {
		${Object.keys(references || [])
			.map(key => {
				if (!references[key]) return `"${key}": ${null}`;
				return `"${key}": {
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
