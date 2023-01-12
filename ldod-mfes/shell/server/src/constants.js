import { dirname, resolve } from 'path';
import { fileURLToPath } from 'url';

const rootPath = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const staticPath = resolve(rootPath, 'static');
const sharedPath = resolve(staticPath, 'shared');
const clientDist = resolve(process.cwd(), 'client/dist');
const sharedDist = resolve(process.cwd(), 'shared/dist');
const importmapPath = resolve(rootPath, 'importmap.json');
const mfesPath = resolve(rootPath, 'mfes.json');
const htmlPath = resolve(staticPath, 'index.html');
const visualPath = resolve(staticPath, 'visual');
const gamePath = resolve(staticPath, 'game');

export { staticPath, importmapPath, mfesPath, htmlPath, visualPath, gamePath, sharedPath, sharedDist, clientDist };
