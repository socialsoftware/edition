import { addToImportmaps } from './importmaps.js';
import { addStaticAssets } from './static.js';
import { clientDist, sharedDist } from './constants.js';

export function addShellClientStaticAssets() {
	addStaticAssets({ from: clientDist, name: '' });
}

export function addSharedStaticAssets() {
	addStaticAssets({ from: sharedDist, name: 'shared' });
}

export function addSharedToImportmaps() {
	addToImportmaps({ name: '@shared/', entry: '/ldod-mfes/shared/' });
}
export function addVendorToImportmaps() {
	addToImportmaps({ name: '@vendor/', entry: '/ldod-mfes/vendor/' });
}
