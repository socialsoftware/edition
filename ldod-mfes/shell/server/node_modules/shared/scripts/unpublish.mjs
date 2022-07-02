import { removeFromImportmaps } from '../../server/importmaps.mjs';
import { removeStaticAssets } from '../../server/static.mjs';
import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

removeStaticAssets({ name: 'shared' });
removeFromImportmaps({ name: 'shared/' });
