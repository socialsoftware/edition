import cors from 'cors';
import express from 'express';
import multer from 'multer';
import './shell-modules.js';
import { staticPath } from './constants.js';
import {
  publishMFE,
  sendClassificationGameIndex,
  sendIndex,
  sendLdodVisualIndex,
  unPublishMFE,
} from './endpoints.js';

const upload = multer({ dest: staticPath });
const app = express();
const router = express.Router();

app.use(express.static(staticPath));
app.use('/ldod-mfes', express.static(staticPath));

app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use('/ldod-mfes', router);
router.get('/', sendIndex);
router.post('/publish', upload.single('file'), publishMFE);
router.post('/unpublish', unPublishMFE);
router.get('/', sendLdodVisualIndex);
router.get('/classification-game/', sendClassificationGameIndex);

router.get('/*', sendIndex);
const port = process.env.PORT || 9000;

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});
