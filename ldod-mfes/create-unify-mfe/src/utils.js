/** @format */

import { resolve, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import fs from 'node:fs';

const rootDir = resolve(dirname(fileURLToPath(import.meta.url)), '../');

export function checkNodeVersion() {
	let version = +process.versions.node.split('.')[0];
	if (version < 14) onError('Node version should 14 or higher');
}

export function getMfeName() {
	const args = process.argv.slice(2);
	const index = args.indexOf('-name');
	if (index === '-1') onError('Invalid directory name. It should be passed the "-name" argument');
	const name = args[index + 1];
	try {
		fs.statSync(`${process.cwd()}/${name}`);
		onError(`A directory named ${name} already exists.`);
	} catch (e) {
		return name;
	}
}

export function undo(name) {
	fs.rmSync(resolve(process.cwd(), name), {
		recursive: true,
		force: true,
	});
}

export function onError(msg) {
	console.error(msg);
	process.exit(1);
}

export function getRootRelPath(path) {
	return resolve(rootDir, path);
}
