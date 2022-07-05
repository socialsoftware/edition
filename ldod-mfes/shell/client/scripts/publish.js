import { addStaticAssets } from '../../server/src/static.js';
import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

addStaticAssets({ from: distPath, name: '' });
