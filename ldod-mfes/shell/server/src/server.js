import cors from 'cors';
import express from 'express';
import multer from 'multer';
import './shell-modules.js';
import { htmlPath, staticPath } from './constants.js';
import {
  publishMFE,
  sendClassificationGameIndex,
  sendIndex,
  sendLdodVisualIndex,
  unPublishMFE,
} from './endpoints.js';
import { resolve } from 'path';
import { getIndexHtml } from './static.js';
import { parse } from 'node-html-parser';
import { writeFileSync } from 'fs';

const upload = multer({ dest: staticPath });
const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));

app.use(express.static(staticPath));

app.get('/', sendIndex);
app.get('/ldod-visual/', sendLdodVisualIndex);
app.get('/classification-game/', sendClassificationGameIndex);
app.post('/publish', upload.single('file'), publishMFE);
app.post('/unpublish', unPublishMFE);

app.get('*', (req, res) => {
  sendIndex(req, res);
});

const addPathScript = (path) => {
  let html = getHTML();
  removePathScript();
  const pathScript = `<script id="path-script">
    window.history.pushState({path: "${path}"},undefined, "/");
  </script>`;
  html.querySelector('head').appendChild(parse(pathScript));
  saveHTML(html.toString());
};

const removePathScript = () => {
  let html = getHTML();
  let scripts = html.querySelectorAll('script#path-script');
  scripts && scripts.forEach((s) => s.remove());
  saveHTML(html.toString());
};

const getHTML = () => parse(getIndexHtml());
const saveHTML = (html) => writeFileSync(htmlPath, html);

const port = process.env.PORT || 9000;

app.listen(port, () => {
  console.log(`Server running at port ${port}.`);
});
