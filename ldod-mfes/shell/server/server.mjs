import express from 'express';
import fs from 'fs';
import { resolve } from 'path';
import cors from 'cors';
import multer from 'multer';
import tar from 'tar';
import {
  addToImportmaps,
  removeFromImportmaps,
  getIndexHtml,
} from './importmaps.mjs';
import { removeStaticAssets } from './static.mjs';

const upload = multer({ dest: 'static' });
const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));

const port = process.env.PORT || 8080;

app.use(express.static(resolve(process.cwd(), 'static')));

const getAlternativeIndexHtml = () =>
  fs.readFileSync(resolve(process.cwd(), 'index.html'), 'utf8');

const sendIndex = (req, res) => {
  res.send(getIndexHtml() ?? getAlternativeIndexHtml());
};

app.get('/', sendIndex);

app.post('/publish', upload.single('file'), async (req, res) => {
  const fileInfo = req.file;
  const { id, name, entry } = req.body;
  await extractTarball(fileInfo, id, name);
  addToImportmaps({
    name,
    entry: name !== entry ? `/${id}/${entry}` : `/${entry}`,
  });

  return res.sendStatus(200);
});

app.post('/unpublish', async (req, res) => {
  const { name } = req.body;
  console.log(name);
  removeStaticAssets({ name });
  removeFromImportmaps({ name });
  return res.sendStatus(200);
});

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});

async function extractTarball(fileInfo, id, name) {
  fs.rmSync(`${fileInfo.destination}/${id}`, { recursive: true, force: true });
  fs.mkdirSync(`${fileInfo.destination}/${id}`, { recursive: true });
  fs.renameSync(
    fileInfo.path,
    `${fileInfo.destination}/${id}/${fileInfo.originalname}`
  );

  await tar.extract({
    cwd: `${fileInfo.destination}/${id}`,
    file: `${fileInfo.destination}/${name}/${fileInfo.originalname}`,
  });

  fs.rmSync(`./${fileInfo.destination}/${id}/${fileInfo.originalname}`);
}
