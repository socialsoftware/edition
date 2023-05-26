/** @format */

import { emitter } from './event-bus.js';
import { resolve } from 'path';
import { readFileSync, writeFileSync } from 'node:fs';
import { staticPath } from './constants.js';
import { execSync } from 'child_process';
import { addToImportmap } from './importmap.js';

emitter.on('mfe:published', e => setImmediate(() => installDeps(e)));

const vendor = resolve(process.cwd(), 'vendor');
const vendorPkgFile = `${vendor}/package.json`;
const vendorPkg = JSON.parse(readFileSync(vendorPkgFile));

async function installDeps({ name }) {
	let manifest;

	try {
		manifest = readFileSync(resolve(staticPath, `${name}/manifest.json`));
		if (!manifest) return;
		const { importmaps, dependencies } = JSON.parse(manifest);
		addImportmaps(importmaps ?? {});
		addDeps(dependencies ?? {});
		install();
	} catch (error) {
		console.error(error);
		return;
	}
}

function addDeps(deps) {
	vendorPkg.dependencies = { ...vendorPkg.dependencies, ...deps };
	writeFileSync(vendorPkgFile, JSON.stringify(vendorPkg));
}
function addImportmaps(importmaps) {
	Object.entries(importmaps).forEach(([name, entry]) => addToImportmap({ name, entry }));
}
function install() {
	execSync('yarn build:vendor', { cwd: process.cwd() });
}
