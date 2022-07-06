import { addStaticAssets } from '../../server/src/static.js';
import { addToImportmaps } from '../../server/src/importmaps.js';
import { addToMfes } from '../../server/src/mfes.js';

import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

addStaticAssets({ from: distPath, name: '' });
addToImportmaps({});
addToMfes();
