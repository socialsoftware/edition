import { addToImportmaps } from '../../server/importmaps.mjs';
import { addStaticAssets } from '../../server/static.mjs';
import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

addStaticAssets({ from: distPath, name: 'shared' });
addToImportmaps({ name: 'shared/', entry: '/shared/' });
