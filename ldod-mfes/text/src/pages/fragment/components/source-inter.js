/** @format */

import { getSourceInter } from '../../fragments/components/source-inter';
import { parseRawHTML } from '../../../utils';
import checkboxes from './checkboxes';
import title from './title';
import sourceInterTranscription from './source-inter-transcription';
const cbs = ['diff', 'del', 'ins', 'sub', 'note', 'fac'];

export default ({ root, inter }) => {
	const frag = parseRawHTML(/*html*/ `
    ${checkboxes({ root, checkboxes: cbs })} ${cbs
		.map(cb => getTooltip(root, cb))
		.join('')} ${title({
		title: root.data.title,
	})}
    <div id="source-transcription"></div>
    <br />
    <div id="interMetaInfo" class="well"></div>
  `);

	frag.querySelector('div#source-transcription').replaceWith(
		sourceInterTranscription({ root, inter })
	);
	frag.querySelector('div#interMetaInfo').appendChild(getSourceInter(inter, root, 0));
	return frag;
};

function getTooltip(root, cb) {
	return /*html*/ `
  <ldod-tooltip
  data-ref="${`div#${cb}`}"
  data-tooltip-key="${`${cb}Info`}"
  placement="top"
  content="${root.getConstants(`${cb}Info`)}"></ldod-tooltip>
  `;
}
