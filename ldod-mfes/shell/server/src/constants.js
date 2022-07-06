import { dirname, resolve } from 'path';
import { fileURLToPath } from 'url';

const rootPath = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const staticPath = resolve(rootPath, 'static');
const importmapPath = resolve(rootPath, 'importmap.json');
const mfesPath = resolve(rootPath, 'mfes.json');

export { staticPath, importmapPath, mfesPath };
