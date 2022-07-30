import cors from 'cors';
import express from 'express';
import multer from 'multer';
import './shell-modules.js';
import { staticPath } from './constants.js';
import asyncRouter from 'async-express-decorator';
import {
  publishMFE,
  sendClassificationGameIndex,
  sendIndex,
  sendLdodVisualIndex,
  unPublishMFE,
} from './endpoints.js';

const upload = multer({ dest: staticPath });
const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

const router = asyncRouter(express.Router());
app.use('/ldod-mfes', express.static(staticPath));
app.get('/', (req, res) => res.redirect('/ldod-mfes'));

app.use('/ldod-mfes', router);
router.get('/', sendIndex);
router.post('/publish', upload.single('file'), publishMFE);
router.post('/unpublish', unPublishMFE);
router.get('/', sendLdodVisualIndex);
router.get('/classification-game/', sendClassificationGameIndex);

app.get('*', sendIndex);

app.use((err, req, res, next) => {
  return res.status(400).send(`Error: ${err.message}`);
});

const port = process.env.PORT || 9000;

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});
