import fs from 'fs';
import { parse } from 'node-html-parser';
import { mfesPath, htmlPath } from './constants.js';
import { getIndexHtml } from './static.js';

const loadMfes = () => {
  try {
    return JSON.parse(fs.readFileSync(mfesPath).toString());
  } catch (error) {
    return [];
  }
};

const saveMfes = (mfes) => {
  fs.writeFileSync(mfesPath, mfes);
};

export const addToMfes = async (name) => {

  let mfes = new Set(loadMfes());
  name && mfes.add(name);
  saveAndUpdateHTML(JSON.stringify(Array.from(mfes).filter(Boolean)));
};

export const removeFromMfes = (name) => {
  let mfes = loadMfes();
  mfes = mfes.filter((mfe) => mfe !== name);
  saveAndUpdateHTML(JSON.stringify(mfes));
};

function saveAndUpdateHTML(mfes) {
  saveMfes(mfes);
  updateMfes(mfes);
  global.globalMfes = mfes;
}

const updateMfes = (mfes) => {
  const indexHTML = getIndexHtml();
  if (!indexHTML) return;
  const html = parse(indexHTML);
  const mfesScript = html.querySelector('script#mfes');
  if (mfesScript) mfesScript.remove();
  const script = parse(`<script id="mfes">window.mfes = ${mfes}</script>`);
  html.querySelector('head').appendChild(script);
  fs.writeFileSync(htmlPath, html.toString());
};
