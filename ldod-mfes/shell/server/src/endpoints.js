import { extractTarball, getIndexHtml, removeStaticAssets } from './static.js';
import { addToImportmaps, removeFromImportmaps } from './importmaps.js';
import { addToMfes, removeFromMfes } from './mfes.js';
import { gamePath, visualPath } from './constants.js';

const sendIndex = (req, res) => res.send(getIndexHtml());

const sendLdodVisualIndex = (req, res) => res.send(getIndexHtml(visualPath));

const sendClassificationGameIndex = (req, res) =>
  res.send(getIndexHtml(gamePath));

const publishMFE = async (req, res) => {
  const fileInfo = req.file;
  const { id, name, entry } = req.body;
  await extractTarball(fileInfo, id);
  name &&
    (await addToImportmaps({
      name,
      entry:
        name !== entry ? `/${process.env.BASE}/${id}/${entry}` : `/${entry}`,
    }));
  await addToMfes(id);
  return res.sendStatus(200);
};
const unPublishMFE = async (req, res) => {
  const { id, name } = req.body;
  removeStaticAssets({ name: id });
  removeFromImportmaps({ name });
  removeFromMfes(id);
  return res.sendStatus(200);
};

export {
  sendIndex,
  sendClassificationGameIndex,
  sendLdodVisualIndex,
  publishMFE,
  unPublishMFE,
};
