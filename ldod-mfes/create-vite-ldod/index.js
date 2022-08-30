#!/usr/bin/env node

'use strict';

import { spawnSync, execSync } from 'child_process';
import path from 'path';
import { fileURLToPath } from 'url';
import fs from 'fs';

const templatesDir = `${path.dirname(
  fileURLToPath(import.meta.url)
)}/templates`;

const scriptsDir = `${path.dirname(fileURLToPath(import.meta.url))}/scripts`;

// check node version
(() => {
  let version = +process.versions.node.split('.')[0];
  if (version < 14) {
    console.error('Node version should 14 or higher');
    process.exit(1);
  }
})();

const getArrayIndex = (arr, flag) => Array.from(arr).indexOf(flag);
const hasOption = (index) => index !== -1;

// checking options
const args = process.argv.slice(2);
const appNameIndex = getArrayIndex(args, '-d');
const appName = hasOption(appNameIndex) && args[appNameIndex + 1];

// directory name is mandatory
if (!appName) throw new Error('Invalid directory name');

const undo = () =>
  fs.rmSync(path.resolve(process.cwd(), appName), {
    recursive: true,
    force: true,
  });

process.on('SIGINT', () => undo());

const packageJson = {
  name: `${appName}`,
  version: '1.0.0',
  license: 'MIT',
  private: true,
  type: 'module',
  entry: 'index.js',
  config: {
    docker: 'http://localhost/ldod-mfes',
    dev: 'http://localhost:9000/ldod-mfes',
  },
  scripts: {
    dev: 'vite',
    build: 'vite build',
    'build-dev': 'vite --config vite.config.dev.js build',
    prepreview: 'vite --config vite.config.dev.js build',
    preview: 'vite --config vite.config.dev.js preview',
    pack: `yarn build && ./scripts/pack.sh ${appName}`,
    'publish-dev': `yarn run pack && ./scripts/publish.sh ${appName} ${appName}.js @./dist/${appName}.tgz $npm_package_config_dev`,
    'unpublish-dev': `./scripts/unpublish.sh ${appName} $npm_package_config_dev`,
    publish: `yarn run pack && ./scripts/publish.sh ${appName} ${appName}.js @./dist/${appName}.tgz $npm_package_config_docker`,
    unpublish: `./scripts/unpublish.sh ${appName} $npm_package_config_docker`,
  },
  devDependencies: {},
};

try {
  fs.readdirSync(`${process.cwd()}/${appName}`);
  console.error(`A directory named ${appName} already exists.`);
  process.exit(1);
} catch (error) {}

spawnSync('mkdir', [appName]);
const cwd = `${appName}/`;

spawnSync('mkdir', ['src'], { cwd });
spawnSync('mkdir', ['scripts'], { cwd });

// copy script folder

fs.readdirSync(scriptsDir).forEach((script) => {
  console.log(`generating ${script}`);
  execSync(`chmod +x ${scriptsDir}/${script}`);
  fs.copyFileSync(
    `${scriptsDir}/${script}`,
    `${process.cwd()}/${appName}/scripts/${script}`
  );
});

fs.readdirSync(templatesDir).forEach((file) => {
  console.log(`generating ${file}`);

  fs.cpSync(`${templatesDir}/${file}`, `${process.cwd()}/${appName}/${file}`, {
    recursive: true,
  });
});

console.log('generating package.json');
try {
  fs.writeFileSync(`${appName}/package.json`, JSON.stringify(packageJson));
  console.log('installing dependencies');
  execSync('yarn add -D vite', { cwd });
  execSync('yarn install', { cwd });
} catch (error) {
  console.error(error.toString());
  undo();
}
