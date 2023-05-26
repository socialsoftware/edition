/** @format */

import { execSync } from 'node:child_process';
import { resolve } from 'node:path';
import fs from 'node:fs';
import { getRootRelPath } from './utils.js';

const scriptsDir = getRootRelPath('scripts');

export default name => {
	const scriptsPath = resolve(name, 'scripts');
	fs.mkdirSync(scriptsPath);

	fs.readdirSync(scriptsDir).forEach(script => {
		console.log(`generating ${script}`);
		execSync(`chmod +x ${scriptsDir}/${script}`);
		fs.copyFileSync(`${scriptsDir}/${script}`, resolve(scriptsPath, script));
	});
};
