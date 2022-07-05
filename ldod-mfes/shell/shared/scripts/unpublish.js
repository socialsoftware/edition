import { removeFromImportmaps } from '../../server/importmaps.mjs';
import { removeStaticAssets } from '../../server/static.mjs';
import path from 'path';

removeStaticAssets({ name: 'shared' });
removeFromImportmaps({ name: 'shared/' });
