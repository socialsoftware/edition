import fs from 'fs';
import { parse } from 'node-html-parser';
import { htmlPath } from './constants.js';
import { getIndexHtml } from './static.js';

export const addProcessScript = () => {
  const script =
    process.env.HOST &&
    parse(`<script id="process">
  window.process = {
    host: "${process.env.HOST}",
  };
</script>`);
  let indexHTML = getIndexHtml();
  if (!indexHTML || !script) return;
  indexHTML = parse(indexHTML);
  const headScript = indexHTML.querySelector('head>script#process');
  if (headScript) return;
  indexHTML.querySelector('head').appendChild(script);
  fs.writeFileSync(htmlPath, indexHTML.toString());
};
