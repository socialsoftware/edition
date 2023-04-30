/** @format */

import {
	addStaticAssets,
	extractTarball,
	getIndexHTML,
	removeStaticAssets,
	rmTempContent,
} from './static.js';
import { addToImportmap, removeFromImportmaps } from './importmap.js';
import { addMfe, removeMfe } from './mfes.js';
import { gamePath, tempPath, visualPath } from './constants.js';
import { isMainThread, Worker } from 'worker_threads';
import { generateMfesReferences } from './mfesReferences.js';
import { emitter } from './event-bus.js';
import { updateIndexHTML } from './html-template.js';
import { resolve } from 'path';

const sendIndex = (req, res) => {
	res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
	res.send(getIndexHTML());
};

const sendLdodVisualIndex = (req, res) => res.send(getIndexHTML(visualPath));

const sendClassificationGameIndex = (req, res) => res.send(getIndexHTML(gamePath));

const checkMfeApiCompliance = async entry => {
	const worker = new Worker('./server/src/mfeApiChecker.js', {
		workerData: entry,
	});

	return new Promise((resolve, reject) => {
		worker.once('message', e => isMainThread && reject(e));
		worker.on('error', error => console.error(error));
		worker.on('exit', () => {
			console.log(`MFE on ${entry} is compliant`);
			resolve();
		});
	});
};

const publishMFE = async (req, res) => {
	const fileInfo = req.file;
	const { id, name, entry } = req.body;
	const entryPoint = `${id}/${entry}`;
	await extractTarball(fileInfo, id);

	if (entry)
		await checkMfeApiCompliance(`${tempPath}/${entryPoint}`)
			.then(() => addStaticAssets({ from: resolve(tempPath, id), name: id }))
			.catch(e => {
				throw new Error(e);
			})
			.finally(() => rmTempContent());

	if (name)
		await addToImportmap({
			name,
			entry: name !== entry ? `/${process.env.BASE}/${entryPoint}` : `/${entry}`,
		});

	await addMfe(id);
	await updateIndexHTML();
	await generateMfesReferences();
	emitter.emit('mfe:published', { name });
	return res.sendStatus(200);
};

const unPublishMFE = async (req, res) => {
	const { id, name } = req.body;
	removeFromImportmaps({ name });
	removeMfe(id);
	removeStaticAssets({ name: id });
	await updateIndexHTML();
	await generateMfesReferences();
	return res.sendStatus(200);
};

export { sendIndex, sendClassificationGameIndex, sendLdodVisualIndex, publishMFE, unPublishMFE };
