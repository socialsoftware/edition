/** @format */

import fs from 'fs';
import path from 'path';

let manifest;
let pkg;
const manifestPath = 'build/manifest.json';
fs.mkdirSync(path.dirname(manifestPath), { recursive: true });

try {
	pkg = (await import('../package.json', { assert: { type: 'json' } })).default;
} catch (error) {
	onError('package.json not found');
}

if (pkg) {
	if (!manifest) manifest = {};
	manifest.dependencies = pkg.externalDependencies;
	manifest.importmaps = pkg.importmaps;
	fs.writeFileSync(manifestPath, JSON.stringify(manifest));
}

export function onError(msg) {
	console.error(msg);
	process.exit(1);
}
