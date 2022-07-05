import fs from 'fs';
import { parse } from 'node-html-parser';
import { staticPath } from './constants.js';

const indexHTML = parse(fs.readFileSync(`${staticPath}/index.html`));

const args = process.argv.slice(2);

const host = args[0];

if (!host) throw new Error('Host information is required');
console.log(host);
const script = parse(`
<script id=process>
  window.process = {
    host: "${host}",
  };
</script>`);

const headScript = indexHTML.querySelector('head>script#process');
headScript && headScript.remove();
indexHTML.querySelector('head').appendChild(script);
fs.writeFileSync(`${staticPath}/index.html`, indexHTML.toString());
