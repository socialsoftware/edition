import { addStaticAssets } from '../../server/static.mjs';
import path from 'path';

const distPath = path.resolve(process.cwd(), 'dist');

addStaticAssets({ from: distPath, name: '' });
