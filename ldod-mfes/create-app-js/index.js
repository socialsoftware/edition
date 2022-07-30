#!/usr/bin/env node

'use strict';

import { spawnSync, execSync } from 'child_process';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import fs from 'node:fs';

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
const portIndex = getArrayIndex(args, '-p');
const port = hasOption(portIndex) ? args[portIndex + 1] : undefined;

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
  main: 'index.js',
  license: 'MIT',
  scripts: {
    test: 'yarn transpile && jest',
    start: `trap 'kill 0' SIGNINT; babel src -w -d build & serve -C -l ${
      port ? port : 5000
    }`,
    transpile: 'babel src -d transpiled-src',
    build: `if [ -d build ]; then rm -r build;fi && yarn transpile && rollup -c`,
    pack: `yarn build && ./scripts/pack.sh ${appName}`,
    publish: `yarn run pack && ./scripts/publish.sh ${appName} ${appName}.js  @./dist/${appName}.tgz`,
    unpublish: `./scripts/unpublish.sh ${appName}`,
  },

  jest: {
    moduleNameMapper: {
      '^shared/(.*)$': '<rootDir>/node_modules/shared/dist/$1',
    },
  },
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
  execSync(
    `yarn add -D  serve @babel/core @babel/cli @babel/preset-react @babel/preset-env babel-jest rollup rollup-plugin-terser @rollup/plugin-dynamic-import-vars @rollup/plugin-dynamic-import-vars rollup-plugin-import-assert jest jest-environment-jsdom @types/jest acorn-import-assertions`,
    { cwd }
  );
  execSync('yarn install', { cwd });
  execSync('yarn link "shared"', { cwd });
  execSync('yarn transpile', { cwd });
} catch (error) {
  console.error(error.toString());
  undo();
}
