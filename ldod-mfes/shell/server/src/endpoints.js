/** @format */

import {
	addStaticAssets,
	extractTarball,
	getIndexHtml,
	removeStaticAssets,
	rmTempContent,
} from './static.js';
import { addToImportmap, getEntryPoint, removeFromImportmaps } from './importmap.js';
import { addMfe, removeMfe } from './mfes.js';
import { gamePath, tempPath, visualPath } from './constants.js';
import { isMainThread, Worker } from 'worker_threads';
import { generateMfesReferences } from './mfesReferences.js';
import { emitter } from './event-bus.js';
import { updateIndexHTML } from './html-template.js';
import { resolve } from 'path';
import { cleanUpMFE } from './pre-render.js';
import { parse } from 'node-html-parser';

const sendIndex = (req, res) => res.send(getIndexHtml());

const sendLdodVisualIndex = (req, res) => res.send(getIndexHtml(visualPath));

const sendClassificationGameIndex = (req, res) => res.send(getIndexHtml(gamePath));

const checkMfeApiCompliance = async entry => {
	const worker = new Worker('./server/src/mfeApiChecker.js', {
		workerData: entry,
	});

	return new Promise((resolve, reject) => {
		worker.once('message', e => {
			if (isMainThread) reject(e);
		});
		worker.on('exit', () => resolve());
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
	await generateMfesReferences();
	await updateIndexHTML({ entryPoint, action: 'publish' });
	emitter.emit('mfe:published', { name });
	return res.sendStatus(200);
};
const unPublishMFE = async (req, res) => {
	const { id, name } = req.body;
	const entryPoint = getEntryPoint(id);
	removeFromImportmaps({ name });
	removeMfe(id);
	await updateIndexHTML({ entryPoint, action: 'unpublish' });
	removeStaticAssets({ name: id });
	await generateMfesReferences();
	return res.sendStatus(200);
};

export { sendIndex, sendClassificationGameIndex, sendLdodVisualIndex, publishMFE, unPublishMFE };
