import { resolve } from 'path';
import { extractTarball, removeStaticAssets } from './static.js';
import {
  addToImportmaps,
  removeFromImportmaps,
  getIndexHtml,
} from './importmaps.js';

const sendIndex = (req, res) => res.send(getIndexHtml());

const sendLdodVisualIndex = (req, res) =>
  res.send(getIndexHtml(resolve(process.cwd(), 'static/ldod-visual')));

const sendClassificationGameIndex = (req, res) =>
  res.send(getIndexHtml(resolve(process.cwd(), 'static/classification-game')));

const publishMFE = async (req, res) => {
  const fileInfo = req.file;
  const { id, name, entry } = req.body;
  await extractTarball(fileInfo, id, name);
  await addToImportmaps({
    name,
    entry: name !== entry ? `/${id}/${entry}` : `/${entry}`,
  });

  return res.sendStatus(200);
};
const unPublishMFE = async (req, res) => {
  const { name } = req.body;
  removeStaticAssets({ name });
  removeFromImportmaps({ name });
  return res.sendStatus(200);
};

export {
  sendIndex,
  sendClassificationGameIndex,
  sendLdodVisualIndex,
  publishMFE,
  unPublishMFE,
};
