import path from 'path';
import { addToImportmaps } from './importmaps.js';
import { addToMfes } from './mfes.js';
import { addProcessScript } from './process.js';
import { addStaticAssets } from './static.js';

const clientPath = path.resolve(process.cwd(), 'client/dist');
const sharedPath = path.resolve(process.cwd(), 'shared/dist');

addStaticAssets({ from: clientPath, name: '' });
addStaticAssets({ from: sharedPath, name: 'shared' });
addToImportmaps({ name: 'shared/', entry: '/shared/' });
addToMfes();
addProcessScript();
