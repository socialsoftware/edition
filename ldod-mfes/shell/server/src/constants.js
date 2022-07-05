import { dirname, join, resolve } from 'path';
import { fileURLToPath } from 'url';

const rootPath = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const staticPath = resolve(rootPath, 'static');
const importmapPath = resolve(rootPath, 'importmap.json');

export { staticPath, importmapPath };
