#!/usr/bin/env node
/** @format */

'use strict';

import { spawnSync } from 'node:child_process';
import { getMfeName, checkNodeVersion, undo } from './utils.js';
import scriptsGen from './scripts-gen.js';
import templatesGen from './templates-gen.js';
import packageJSONGen from './packageJSON-gen.js';

checkNodeVersion();
const mfeName = getMfeName();
process.on('SIGINT', () => undo(mfeName));
spawnSync('mkdir', [mfeName]);
spawnSync('mkdir', [`${mfeName}/src`]);
spawnSync('mkdir', [`${mfeName}/scripts`]);
scriptsGen(mfeName);
templatesGen(mfeName);
packageJSONGen(mfeName);
