import { parse } from 'node-html-parser';
import { getIndexHtml } from './static.js';
import fs from 'fs';
import { htmlPath } from './constants.js';
const dom = () => parse(getIndexHtml());

export function preRenderIndexHtml() {
	preRenderModal();
}

function preRenderModal() {
	const html = dom();
	import('../static/shared/ssr-modal.js')
		.then(({ staticGeneration }) => {
			html.querySelectorAll('ldod-modal').forEach(modal => {
				modal.querySelector('template')?.remove();
				const template = staticGeneration(modal.getAttribute('dialog-class'), modal.getAttribute('noFooter'));
				modal.appendChild(parse(template));
			});
			fs.writeFileSync(htmlPath, html.toString());
		})
		.catch(e => console.error(e));
}
