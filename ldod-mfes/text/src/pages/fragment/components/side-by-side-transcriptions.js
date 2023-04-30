/** @format */

import { isSingleAndSourceInter } from '../utils';
import { getSourceInter } from '../../fragments/components/source-inter';
import { parseRawHTML } from '../../../utils';
import checkboxes from './checkboxes';
import title from './title';
import editorialInterTranscription from './editorial-inter-transcription';
import variationsTable from './variations-table';

const getAlignClass = root =>
	root.transcriptionCheckboxes.align ? 'font-monospace' : 'font-georgia';

export default ({ root, inters }) => {
	const frag = parseRawHTML(/*html*/ `		
		${checkboxes({ root, checkboxes: ['line', 'align'] })}
		<div id="text-transcriptionsSideBySide" class="sideBySide">
			${inters.map((inter, key) => getTranscrition(inter, root, key)).join('')}
		</div>
		<div id="text-meta-side-by-side" class="sideBySide"></div>
		<div id="text-variations-side-by-side"></div>
	`);

	frag.querySelector('div#text-meta-side-by-side').append(
		...inters.map(inter => getMetaInfo(root, inter))
	);

	frag.querySelector('div#text-variations-side-by-side').appendChild(
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

function getTranscrition(inter, root, key) {
	return /*html*/ `
				<div class="text-sideBySideWrapper">
					${title({ title: root.data.title })} 
					${
						isSingleAndSourceInter(inter)
							? /*html */ `
					<div class="well authorialStyle" id="transcriptionContainer">
						<p class="${getAlignClass(root)}">
							${root.data.transcriptions[key]}
						</p>
					</div>
					`
							: editorialInterTranscription({
									root,
									clazz: getAlignClass(root),
									key,
							  })
					}
				</div>
				
	`;
}

function getMetaInfo(root, inter) {
	const frag = parseRawHTML/*html*/ `<div id="interMetaInfo" class="well" style="width: 50%;"></div>`;
	frag.querySelector('div#interMetaInfo').appendChild(getSourceInter(inter, root, 0));
	return frag;
}
