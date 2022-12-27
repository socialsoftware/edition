import { emitter } from './event-bus.js';
import { resolve } from 'path';
import { readFileSync, writeFileSync } from 'node:fs';
import { sharedDist, staticPath } from './constants.js';
import { exec } from 'child_process';
import { addStaticAssets } from './static.js';

emitter.on('mfe:published', installDeps);

async function installDeps({ name }) {
  const sharedDirectory = resolve(process.cwd(), 'shared');
  const sharedPkgFile = `${sharedDirectory}/package.json`;
  let manifest;
  try {
    manifest = readFileSync(resolve(staticPath, `${name}/manifest.json`));
  } catch (error) {
    return;
  }
  if (!manifest) return;
  const deps = JSON.parse(manifest).dependencies;
  const sharedPkg = JSON.parse(readFileSync(sharedPkgFile));
  sharedPkg.dependencies = { ...sharedPkg.dependencies, ...deps };
  writeFileSync(sharedPkgFile, JSON.stringify(sharedPkg));

  exec('yarn build', { cwd: `${process.cwd()}/shared` }, () => {
    addStaticAssets({ from: sharedDist, name: 'shared' });
  });
}
