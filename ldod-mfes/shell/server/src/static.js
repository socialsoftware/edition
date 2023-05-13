/** @format */

import fs, { mkdirSync, rmSync } from 'fs';
import path, { resolve } from 'path';
import tar from 'tar';
import { indexHTML, originalHTML, staticPath, tempPath } from './constants.js';

function removeStaticAssets({ name }) {
	try {
		const dirToRemove = fs.readdirSync(staticPath).find(dir => dir === name);
		if (!dirToRemove) return;
		fs.rmSync(path.resolve(staticPath, name), {
			recursive: true,
			force: true,
		});
	} catch (error) {
		console.error(error);
	}
}

function addStaticAssets({ from, name }) {
	removeStaticAssets({ name });
	const dest = path.resolve(staticPath, name);
	try {
		fs.cpSync(from, dest, {
			recursive: true,
			force: true,
		});
	} catch (error) {
		console.log(error);
	}
}

export function rmTempContent() {
	rmSync(tempPath, { recursive: true, force: true });
	mkdirSync(tempPath);
}

async function extractTarball(fileInfo, id) {
	const dest = resolve(fileInfo.destination, id);
	const source = resolve(fileInfo.destination, id, fileInfo.originalname);
	fs.mkdirSync(dest);
	fs.renameSync(fileInfo.path, source);

	await tar.extract({
		cwd: dest,
		file: source,
	});

	fs.rmSync(resolve(fileInfo.destination, id, fileInfo.originalname));
}

export function getOriginalHTML() {
	try {
		return fs.readFileSync(originalHTML, 'utf8');
	} catch (error) {
		return;
	}
}

export function getIndexHTML(path = indexHTML) {
	try {
		return fs.readFileSync(path, 'utf8');
	} catch (error) {
		return;
	}
}

export { extractTarball, addStaticAssets, removeStaticAssets };
