/** @format */

import { execSync } from 'node:child_process';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import fs from 'node:fs';

const scriptsDir = `${path.dirname(fileURLToPath(import.meta.url))}/scripts`;

export default name => {
	fs.readdirSync(scriptsDir).forEach(script => {
		console.log(`generating ${script}`);
		execSync(`chmod +x ${scriptsDir}/${script}`);
		fs.copyFileSync(`${scriptsDir}/${script}`, `${process.cwd()}/${name}/scripts/${script}`);
	});
};
