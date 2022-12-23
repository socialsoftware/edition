import { addToImportmaps } from './importmaps.js';
import { addToMfes } from './mfes.js';
import { addProcessScript } from './process.js';
import { addStaticAssets } from './static.js';
import { clientDist, sharedDist } from './constants.js';

addStaticAssets({ from: clientDist, name: '' });
addStaticAssets({ from: sharedDist, name: 'shared' });
addToImportmaps({ name: 'shared/', entry: '/ldod-mfes/shared/' });
addToImportmaps({ name: 'vendor/', entry: '/ldod-mfes/vendor/' });

addToMfes();
addProcessScript();
import('./static-generation.js');
