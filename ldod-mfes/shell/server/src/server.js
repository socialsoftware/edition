import express from 'express';
import { resolve } from 'path';
import fs from 'fs';
import cors from 'cors';
import multer from 'multer';
import { staticPath } from './constants.js';

import {
  sendIndex,
  sendClassificationGameIndex,
  sendLdodVisualIndex,
  publishMFE,
  unPublishMFE,
} from './endpoints.js';
import { exec } from 'child_process';

const upload = multer({ dest: 'static' });
const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(resolve(process.cwd(), 'static')));

app.get('/', sendIndex);

app.get('/ldod-visual/', sendLdodVisualIndex);
app.get('/classification-game/', sendClassificationGameIndex);

app.post('/publish', upload.single('file'), publishMFE);

app.post('/unpublish', unPublishMFE);

const port = process.env.PORT || 9000;

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});

fs.watchFile(
  resolve(staticPath, 'index.html'),
  (curr, prev) => prev.mtime !== curr.mtime && exec('yarn dev')
);
