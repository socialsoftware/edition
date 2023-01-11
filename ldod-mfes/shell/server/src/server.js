import asyncRouter from 'async-express-decorator';
import cors from 'cors';
import express from 'express';
import multer from 'multer';
import path from 'node:path';
import { sharedPath, staticPath } from './constants.js';
import './deps-install.js';
import { publishMFE, sendClassificationGameIndex, sendIndex, sendLdodVisualIndex, unPublishMFE } from './endpoints.js';
import { processReferences } from './mfesReferences.js';
import { preRenderIndexHtml } from './pre-render.js';
import {
	addSharedStaticAssets,
	addSharedToImportmaps,
	addShellClientStaticAssets,
	addVendorToImportmaps,
} from './shell-modules.js';
import { updateMfesList } from './mfes.js';
import { appendProcessScript } from './process.js';

addShellClientStaticAssets();
addSharedStaticAssets();
addSharedToImportmaps();
addVendorToImportmaps();
updateMfesList();
appendProcessScript();
preRenderIndexHtml();
await processReferences();

const upload = multer({ dest: staticPath });
const app = express();

app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

const router = asyncRouter(express.Router());
app.use('/ldod-mfes/vendor', express.static(path.resolve(sharedPath, 'node_modules')));
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

app.listen(port, () => {
	console.log(`Server running at port ${port}.`);
});
