/** @format */

import { parseRawHTML } from '../../../utils';
import viewer from './viewer';

export default ({ root, inter }) => {
	const frag = parseRawHTML(/*html*/ `	
		<div
			class="${root.transcriptionCheckboxes.fac ? 'text-fac-transcription' : 'text-nofac-transcription'}"
		>	<div id="source-fac"></div>
			<div class="well authorialStyle" id="transcriptionContainer">
				${root.data.transcriptions[0]}
			</div>
		</div>
	`);
	if (root.transcriptionCheckboxes.fac) {
		frag.querySelector('div#source-fac').replaceWith(
			viewer({ surfaceList: inter.surfaceDetailsList ?? [] })
		);
	}

	return frag;
};
