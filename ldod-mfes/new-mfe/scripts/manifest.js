/** @format */

import pkg from '../package.json' assert { type: 'json' };
import manifest from '../build/manifest.json' assert { type: 'json' };
import fs from 'fs';
import path from 'path';

manifest.dependencies = pkg.dependencies;
manifest.importmaps = pkg.importmaps;
fs.writeFileSync(path.resolve('build', 'manifest.json'), JSON.stringify(manifest));
