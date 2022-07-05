import { addToImportmaps } from '../../server/src/importmaps.js';
import { addStaticAssets } from '../../server/src/static.js';
import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

addStaticAssets({ from: distPath, name: 'shared' });
addToImportmaps({ name: 'shared/', entry: '/shared/' });
