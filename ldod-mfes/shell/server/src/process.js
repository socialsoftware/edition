import fs from 'fs';
import { parse } from 'node-html-parser';
import { htmlPath } from './constants.js';
import { getIndexHtml } from './static.js';

export const appendProcessScript = () => {
	const script =
		process.env.HOST &&
		parse(/*html */ `
			<script id="process">
				window.LDOD_PRODUCTION = true;
					window.process = {
						host: "${process.env.HOST}",
						apiHost: "${process.env.API_HOST}"
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
