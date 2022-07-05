import fs from 'fs';
import { parse } from 'node-html-parser';
import { resolve } from 'path';
import { importmapPath, staticPath } from './constants.js';

const loadImportmaps = () => {
  try {
    return JSON.parse(fs.readFileSync(importmapPath).toString());
  } catch (error) {
    return { imports: {} };
  }
};

const saveImportmaps = (importmap) => {
  fs.writeFileSync(importmapPath, importmap);
};

export const getIndexHtml = (path = staticPath) => {
  try {
    return fs.readFileSync(resolve(path, 'index.html'), 'utf8');
  } catch (error) {
    return;
  }
};

export const addToImportmaps = async ({ name, entry }) => {
  let importmap = loadImportmaps();
  importmap.imports[name] = entry;
  saveAndUpdateHTML(JSON.stringify(importmap));
};

export const removeFromImportmaps = ({ name }) => {
  let importmap = loadImportmaps();
  delete importmap.imports[name];
  saveAndUpdateHTML(JSON.stringify(importmap));
};

function saveAndUpdateHTML(importmap) {
  saveImportmaps(importmap);
  updateImportmapScript(importmap);
}

const updateImportmapScript = (importmap) => {
  const indexHTML = getIndexHtml();
  if (!indexHTML) return;
  const html = parse(indexHTML);
  const importmapScript = html.querySelector('script[type=importmap]');
  importmapScript.set_content(importmap);
  fs.writeFileSync(resolve(staticPath, 'index.html'), html.toString());
};
