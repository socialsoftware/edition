/** @format */

import { loadMfes } from './mfes.js';
import { preRenderIndexHtml } from './pre-render.js';
import fs from 'fs';
import { getIndexHtml } from './static.js';
import { loadImportmap } from './importmap.js';
import { htmlPath } from './constants.js';
import { parse } from 'node-html-parser';
import { minify } from 'html-minifier';

export async function updateIndexHTML() {
	const html = getIndexHtml();
	if (!html) return;
	const dom = parse(html);
	updateImportmapScript(dom);
	updateLdodProcessScript(dom);
	updateMfesReferencesScript(dom);
	//await preRenderIndexHtml(dom);
	fs.writeFileSync(
		htmlPath,
		minify(dom.outerHTML, {
			minifyCSS: true,
			minifyJS: true,
			collapseWhitespace: true,
		})
	);
}

function updateImportmapScript(dom) {
	const importmapScript = dom.querySelector('script[type=importmap]');
	importmapScript.set_content(JSON.stringify(loadImportmap()));
}

function updateLdodProcessScript(dom) {
	if (!process.env.HOST) return;
	const mfes = JSON.stringify(loadMfes());
	const ldodProcessScript = parse(/*html */ `
                <script id="ldod-process">
                    window.LDOD_PRODUCTION = true;
                    window.mfes = ${mfes}
                    window.process = {
                            host: "${process.env.HOST}",
                            apiHost: "${process.env.API_HOST}"
                        };
                </script>`);

	const script = dom.querySelector('head script#ldod-process');
	if (script) script.replaceWith(ldodProcessScript);
	else dom.querySelector('head').appendChild(ldodProcessScript);
}

function updateMfesReferencesScript(dom) {
	const src = '/ldod-mfes/references.js';
	let script = dom.querySelector('head script#mfes-references');
	if (script && script.attributes.src === src) return;
	script = parse(`<script id="mfes-references" src="${src}" async></script>`);
	dom.querySelector('head').appendChild(script);
}
