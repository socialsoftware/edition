import fs from 'fs';
import { parse } from 'node-html-parser';
import { importmapPath, htmlPath } from './constants.js';
import { getIndexHtml } from './static.js';

export function loadImportmap() {
	let importmap = { imports: {} };
	try {
		importmap = JSON.parse(fs.readFileSync(importmapPath, 'utf-8'));
	} catch (error) {
		console.error(error);
	}
	return importmap;
}

export function saveImportmap(importmap) {
	fs.writeFileSync(importmapPath, JSON.stringify(importmap));
}

export async function addToImportmap({ name, entry }) {
	const importmap = loadImportmap();
	importmap.imports[name] = entry;
	saveImportmap(importmap);
}

export const removeFromImportmaps = ({ name }) => {
	const importmap = loadImportmap();
	delete importmap.imports[name];
	saveImportmap(importmap);
};
