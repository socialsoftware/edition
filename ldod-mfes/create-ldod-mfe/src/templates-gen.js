/** @format */
import { resolve } from 'node:path';
import fs from 'fs';
import { getRootRelPath } from './utils.js';

export default name => extractDirectory(name, templatesDir);

const templatesDir = getRootRelPath('templates');

export function extractDirectory(name, dir, dest = resolve(name)) {
	!fs.existsSync(dest) && fs.mkdirSync(dest);
	fs.readdirSync(dir).forEach(item => {
		const itemPath = resolve(dir, item);
		const stat = fs.statSync(itemPath);
		if (stat.isDirectory()) extractDirectory(name, itemPath, resolve(dest, item));
		if (stat.isFile()) handleFile(name, itemPath, resolve(dest, item));
	});
}

function handleFile(name, srcPath, destinationPath) {
	console.log(destinationPath);
	if (srcPath.endsWith('.env')) return generateEnvVariables(name, srcPath, destinationPath);
	destinationPath = destinationPath.endsWith('src/entry-point.js')
		? destinationPath.replace('entry-point', name)
		: destinationPath;
	fs.copyFileSync(srcPath, destinationPath);
}

function generateEnvVariables(name, src, dest) {
	let content = fs.readFileSync(src);
	content += `\nVITE_MFE_NAME=${name}`;
	fs.writeFileSync(dest, content);
}
