import { parse } from 'node-html-parser';

export async function preRenderIndexHtml(dom) {
	await preRenderModal(dom);
}

async function preRenderModal(dom) {
	await import('../static/shared/ssr-modal.js')
		.then(({ staticGeneration }) => {
			dom.querySelectorAll('ldod-modal').forEach(modal => {
				modal.querySelector('template')?.remove();
				const template = staticGeneration(modal.getAttribute('dialog-class'), modal.hasAttribute('no-footer'));
				modal.appendChild(parse(template));
			});
		})
		.catch(e => console.error(e));
}
