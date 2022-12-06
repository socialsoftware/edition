import { extractTarball, getIndexHtml, removeStaticAssets } from './static.js';
import { addToImportmaps, removeFromImportmaps } from './importmaps.js';
import { addToMfes, removeFromMfes } from './mfes.js';
import { gamePath, staticPath, visualPath } from './constants.js';
import { isMainThread, Worker } from 'worker_threads';
import { processReferences } from './mfesReferences.js';

const sendIndex = (req, res) => res.send(getIndexHtml());

const sendLdodVisualIndex = (req, res) => res.send(getIndexHtml(visualPath));

const sendClassificationGameIndex = (req, res) =>
  res.send(getIndexHtml(gamePath));

const checkMfeApiCompliance = async (entry) => {
  const worker = new Worker('./server/src/mfeApiChecker.js', {
    workerData: entry,
  });

  return new Promise((resolve, reject) => {
    worker.once('message', (e) => {
      if (isMainThread) reject(e);
    });
    worker.on('exit', () => resolve());
  });
};

const publishMFE = async (req, res) => {
  const fileInfo = req.file;
  const { id, name, entry } = req.body;
  await extractTarball(fileInfo, id);

  if (entry)
    await checkMfeApiCompliance(`${staticPath}/${id}/${entry}`).catch((e) => {
      throw new Error(e);
    });

  if (name)
    await addToImportmaps({
      name,
      entry:
        name !== entry ? `/${process.env.BASE}/${id}/${entry}` : `/${entry}`,
    });
  await addToMfes(id);
  await processReferences();
  return res.sendStatus(200);
};
const unPublishMFE = async (req, res) => {
  const { id, name } = req.body;
  removeStaticAssets({ name: id });
  removeFromImportmaps({ name });
  removeFromMfes(id);
  await processReferences();
  return res.sendStatus(200);
};

export {
  sendIndex,
  sendClassificationGameIndex,
  sendLdodVisualIndex,
  publishMFE,
  unPublishMFE,
};
