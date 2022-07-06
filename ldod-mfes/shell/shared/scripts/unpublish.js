import { removeFromImportmaps } from '../../server/src/importmaps.js';
import { removeStaticAssets } from '../../server/src/static.js';

removeStaticAssets({ name: 'shared' });
removeFromImportmaps({ name: 'shared/' });
