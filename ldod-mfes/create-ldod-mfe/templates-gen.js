/** @format */
import { fileURLToPath } from 'node:url';
import fs from 'node:fs';
import path, { resolve } from 'node:path';

const templatesDir = `${path.dirname(fileURLToPath(import.meta.url))}/templates`;

export default name => {
	fs.readdirSync(templatesDir).forEach(file => {
		if (file === '.env') return generateEnvVariables(name);
		if (file === 'src')
			return fs.readdirSync(resolve(templatesDir, file)).forEach(subfile => {
				copyFile('src/' + subfile, name);
			});
		copyFile(file, name);
	});
};

function copyFile(path, name) {
	fs.copyFileSync(
		resolve(templatesDir, path),
		resolve(
			process.cwd(),
			name,
			path.endsWith('src/replace.js') ? path.replace('replace', name) : path
		)
	);
}

function generateEnvVariables(name) {
	let content = fs.readFileSync(resolve(templatesDir, '.env'));
	content += `\nVITE_MFE_NAME=${name}`;
	fs.writeFileSync(resolve(process.cwd(), name, '.env'), content);
}

function generateMfeEntry(name) {
	let content = fs.readFileSync(`${templatesDir}/src/replace.js`);
	fs.writeFileSync(`${process.cwd()}/${name}/src/${name}.js`, content);
}
