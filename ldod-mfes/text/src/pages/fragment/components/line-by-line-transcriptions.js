/** @format */

import { parseRawHTML } from '../../../utils';
import checkboxes from './checkboxes';
import title from './title';
import variationsTable from './variations-table';

const getAlignClass = node =>
	node.transcriptionCheckboxes.align ? 'font-monospace' : 'font-georgia';

export default ({ root, inters }) => {
	const frag = parseRawHTML(/*html*/ `
		${checkboxes({ root, checkboxes: inters.length <= 2 ? ['line', 'align'] : ['align'] })}
		<div style="display: flex; justify-content: center;">
			${title({ title: root.data.title })}
			<span id="lineByLineTranscriptions" is="ldod-span-icon" icon="circle-info" size="12px"></span>
			<ldod-tooltip
				data-ref="span#lineByLineTranscriptions"
				data-tooltipkey="lineByLineInfo"
				placement="top"
				light
				width="200px"
				content=${root.getConstants('lineByLineInfo')}></ldod-tooltip>
		</div>
		<div id="text-transcriptionsLineByLine" class="lineByLine">
		<div class="well authorialStyle" id="transcriptionContainer">
			<p class="${getAlignClass(root)}">${root.data.transcriptions[0]}</p>
		</div>
	</div>
	<br />
	<div id="text-variations-line-by-line"></div>
	`);

	frag.querySelector('div#text-variations-line-by-line').appendChild(
		variationsTable({
			title: root.getConstants('variations'),
			headers: inters.map(({ shortName, editor }) => ({
				editor,
				shortName,
				title: root.data.title,
			})),
			variations: root.data.variations,
		})
	);
	return frag;
};
