/** @format */

import { dirname, resolve } from 'path';
import { fileURLToPath } from 'url';

const rootPath = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const staticPath = resolve(rootPath, 'static');
const shellPath = resolve(rootPath, 'static', 'shell');
const indexHTML = resolve(staticPath, 'index.html');
const sharedPath = resolve(staticPath, 'shared');
const clientDist = resolve(process.cwd(), 'client/dist');
const sharedDist = resolve(process.cwd(), 'shared/dist');
const importmapPath = resolve(rootPath, 'importmap.json');
const mfesPath = resolve(rootPath, 'mfes.json');
const originalHTML = resolve(shellPath, 'index.html');
const visualPath = resolve(staticPath, 'visual');
const gamePath = resolve(staticPath, 'game');
const tempPath = resolve(rootPath, 'temp');
const templatePath = resolve(rootPath, 'templates');
const staticSWPath = resolve(staticPath, 'service-worker.js');
const templateSWPath = resolve(templatePath, 'service-worker.js');

export {
	staticPath,
	importmapPath,
	mfesPath,
	originalHTML,
	visualPath,
	gamePath,
	sharedPath,
	sharedDist,
	clientDist,
	tempPath,
	shellPath,
	indexHTML,
	staticSWPath,
	templateSWPath,
};
