/** @format */

import { readFileSync, writeFileSync } from 'node:fs';
import { staticSWPath, templateSWPath } from './constants.js';
import crypto from 'node:crypto';

const REGEX = /let\s+CACHE_VERSION\s*=\s*'([^']*)';/;

export function updateSWCache() {
	let content = readFileSync(templateSWPath, 'utf8');
	global.CACHE_VERSION = crypto.randomUUID();
	const newString = `let CACHE_VERSION = '${global.CACHE_VERSION}';`;
	content = content.replace(REGEX, newString);
	writeFileSync(staticSWPath, content);
}
