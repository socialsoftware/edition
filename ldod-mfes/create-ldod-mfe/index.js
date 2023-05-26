#!/usr/bin/env node
/** @format */

'use strict';

import { getMfeName, checkNodeVersion, undo } from './src/utils.js';
import scriptsGen from './src/scripts-gen.js';
import templatesGen from './src/templates-gen.js';
import jsonPkgGen from './src/json-package-gen.js';
import fs from 'node:fs';

checkNodeVersion();
const mfeName = getMfeName();
process.on('SIGINT', () => undo(mfeName));
fs.mkdirSync(mfeName);
scriptsGen(mfeName);
templatesGen(mfeName);
jsonPkgGen(mfeName);

console.log(`cd ${mfeName}`);
console.log(`npm install`);
