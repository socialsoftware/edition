import { staticGeneration } from '../static/shared/ssr-modal.js';
import { parse } from 'node-html-parser';
import { getIndexHtml } from './static.js';
import fs from 'fs';
import { htmlPath } from './constants.js';
const dom = parse(getIndexHtml());

dom.querySelectorAll('ldod-modal').forEach((modal) => {
  modal.querySelector('template')?.remove();
  const template = staticGeneration(
    modal.getAttribute('dialog-class'),
    modal.getAttribute('noFooter')
  );
  modal.appendChild(parse(template));
});

fs.writeFileSync(htmlPath, dom.toString());
