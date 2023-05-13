/** @format */

import { emitter } from './event-bus.js';
import { resolve } from 'path';
import { readFileSync, writeFileSync } from 'node:fs';
import { staticPath } from './constants.js';
import { execSync } from 'child_process';
import { addStaticAssets } from './static.js';
import {addToImportmap} from "./importmap.js"

emitter.on('mfe:published', e => setImmediate(() => installDeps(e)));

async function installDeps({ name }) {
	const vendor = resolve(process.cwd(), 'vendor');
	const vendorModules = `${vendor}/node_modules`;

	let manifest;

	try {
		manifest = readFileSync(resolve(staticPath, `${name}/manifest.json`));
	if (!manifest) return;
	const {importmaps, dependencies} = JSON.parse(manifest)
	addImportmaps(importmaps);
	addDeps(dependencies);
	install();

	} catch (error) {
		return;
	}

	

}


function addDeps(deps) {
	const vendorPkgFile = `${vendor}/package.json`;
	const vendorPkg = JSON.parse(readFileSync(vendorPkgFile));
	vendorPkg.dependencies = { ...vendorPkg.dependencies, ...deps };
	writeFileSync(vendorPkgFile, JSON.stringify(vendorPkg));

}
function addImportmaps(importmaps){
	Object.entries(importmaps).forEach(([name,entry]) => addToImportmap({ name , entry }));	

}
function install(){
	execSync('yarn', { cwd: vendor });
	addStaticAssets({ from: vendorModules, name: 'shared/node_modules' });
}