/** @format */

import fs from 'fs';
import { mfesPath } from './constants.js';

export function createOrUpdateMfes() {
	const mfes = loadMfes();
	saveMfes(mfes);
}

/**
 *
 * @returns {[]}
 */
export function loadMfes() {
	try {
		return JSON.parse(fs.readFileSync(mfesPath, 'utf-8'));
	} catch (error) {
		return [];
	}
}

function saveMfes(mfes) {
	fs.writeFileSync(mfesPath, JSON.stringify(mfes));
}

export async function addMfe(name) {
	if (!name) return;
	const mfes = new Set(loadMfes());
	mfes.add(name);
	saveMfes([...mfes]);
}

export function removeMfe(name) {
	saveMfes(loadMfes().filter(mfe => mfe !== name));
}
