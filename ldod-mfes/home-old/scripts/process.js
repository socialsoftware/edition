import { parse } from 'node-html-parser';
import fs from 'fs';
const indexHTML = parse(fs.readFileSync('./index.html'));

const args = process.argv.slice(2);

const host = args[0];

if (!host) throw new Error('Host information is required');

const script = parse(`<script id=process>
  window.process = {
    host: "${host}",
  };
</script>`);

const headScript = indexHTML.querySelector('head>script#process');
headScript && headScript.remove();
indexHTML.querySelector('head').appendChild(script);
fs.writeFileSync('./index.html', indexHTML.toString());
