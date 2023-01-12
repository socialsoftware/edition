import fs from 'fs';
import { mfesPath } from './constants.js';

export function createOrUpdateMfes() {
	const mfes = loadMfes();
	saveMfes(mfes);
}

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
	const mfes = new Set(loadMfes());
	name && mfes.add(name);
	saveMfes([...mfes]);
}

export function removeMfe(name) {
	saveMfes(JSON.stringify(loadMfes().filter(mfe => mfe !== name)));
}
