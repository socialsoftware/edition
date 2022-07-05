import express from 'express';
import { resolve } from 'path';
import cors from 'cors';
import multer from 'multer';

import {
  sendIndex,
  sendClassificationGameIndex,
  sendLdodVisualIndex,
  publishMFE,
  unPublishMFE,
} from './endpoints.js';

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
