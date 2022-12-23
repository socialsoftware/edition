import pkg from '../package.json' assert { type: 'json' };
import manifest from '../build/manifest.json' assert { type: 'json' };
import fs from 'fs';
import path from 'path';

manifest.dependencies = pkg.externalDependencies;
fs.writeFileSync(
  path.resolve('build', 'manifest.json'),
  JSON.stringify(manifest)
);
