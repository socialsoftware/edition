import fs from 'fs';
import { importmapPath } from './constants.js';

export function createOrUpdateImportmap() {
	const importmap = loadImportmap();
	console.log(!('@shared/' in importmap));
	if (!('@shared/' in importmap)) addToImportmap({ name: '@shared/', entry: '/ldod-mfes/shared/' });
	if (!('@vendor/' in importmap)) addToImportmap({ name: '@vendor/', entry: '/ldod-mfes/vendor/' });
}

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
