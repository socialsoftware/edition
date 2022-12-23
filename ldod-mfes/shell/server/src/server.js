import cors from 'cors';
import express from 'express';
import multer from 'multer';
import './shell-modules.js';
import { sharedPath, staticPath } from './constants.js';
import asyncRouter from 'async-express-decorator';

import './deps-install.js';
import {
  publishMFE,
  sendClassificationGameIndex,
  sendIndex,
  sendLdodVisualIndex,
  unPublishMFE,
} from './endpoints.js';
import { processReferences } from './mfesReferences.js';
import path from 'node:path';

const upload = multer({ dest: staticPath });
const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

const router = asyncRouter(express.Router());
app.use(
  '/ldod-mfes/vendor',
  express.static(path.resolve(sharedPath, 'node_modules'))
);
app.use('/ldod-mfes', express.static(staticPath));
app.get('/', (req, res) => res.redirect('/ldod-mfes'));

app.use('/ldod-mfes', router);
router.get('/', sendIndex);
router.post('/publish', upload.single('file'), publishMFE);
router.post('/unpublish', unPublishMFE);
router.get('/ldod-visual/', sendLdodVisualIndex);
router.get('/classification-game/', sendClassificationGameIndex);

app.get('/user/sign-up*', (req, res) => res.redirect(`/ldod-mfes${req.url}`));

app.get('*', sendIndex);

app.use((err, req, res, next) => {
  return res.status(400).send(`Error: ${err.message}`);
});

const port = process.env.PORT || 9000;

await processReferences();

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});
