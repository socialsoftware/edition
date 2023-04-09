/** @format */

import { emitter } from './event-bus.js';
import { resolve } from 'path';
import { readFileSync, writeFileSync } from 'node:fs';
import { staticPath } from './constants.js';
import { execSync } from 'child_process';
import { addStaticAssets } from './static.js';

emitter.on('mfe:published', e => setImmediate(() => installDeps(e)));

async function installDeps({ name }) {
	const vendor = resolve(process.cwd(), 'vendor');
	const vendorPkgFile = `${vendor}/package.json`;
	const vendorModules = `${vendor}/node_modules`;

	let manifest;

	try {
		manifest = readFileSync(resolve(staticPath, `${name}/manifest.json`));
	} catch (error) {
		return;
	}

	if (!manifest) return;
	const deps = JSON.parse(manifest).dependencies;
	const vendorPkg = JSON.parse(readFileSync(vendorPkgFile));
	vendorPkg.dependencies = { ...vendorPkg.dependencies, ...deps };
	writeFileSync(vendorPkgFile, JSON.stringify(vendorPkg));
	execSync('yarn', { cwd: vendor });
	addStaticAssets({ from: vendorModules, name: 'shared/node_modules' });
}
