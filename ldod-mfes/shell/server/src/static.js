/** @format */

import fs, { mkdirSync, rmSync } from 'fs';
import path, { resolve } from 'path';
import tar from 'tar';
import { htmlPath, staticPath, tempPath } from './constants.js';

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
	//fs.rmSync(dest, { recursive: true, force: true });
	fs.mkdirSync(dest);
	fs.renameSync(fileInfo.path, source);

	await tar.extract({
		cwd: dest,
		file: source,
	});

	fs.rmSync(resolve(fileInfo.destination, id, fileInfo.originalname));
}

const getIndexHtml = () => {
	try {
		return fs.readFileSync(htmlPath, 'utf8');
	} catch (error) {
		return;
	}
};

export { extractTarball, addStaticAssets, removeStaticAssets, getIndexHtml };
