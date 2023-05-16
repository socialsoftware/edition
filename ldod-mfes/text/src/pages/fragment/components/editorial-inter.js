/** @format */

import checkboxes from './checkboxes';
import { getExpertEdition } from '../../fragments/components/expert-edition';
import { readingExpertEdition } from '../../../external-deps';
import editorialInterTranscription from './editorial-inter-transcription';
import title from './title';

export default async ({ root, inter }) => {
	const template = document.createElement('template');
	template.innerHTML = String.raw`
			${checkboxes({ root, checkboxes: ['diff'] })}
			<div style="display: flex;justify-content: center;">
				${title({ title: root.data.title })}
				<a is="nav-to" to="${readingExpertEdition(inter.xmlId, inter.urlId)}" style="margin: 20px 10px;">
					<span is="ldod-span-icon" icon="eye" size="20px" hover-fill="#0d6efd"></span>
				</a>
			</div>
			${editorialInterTranscription({ root, key: 0 })}
			<div id="interMetaInfo" class="well"></div>	`;

	template.content.querySelector('#interMetaInfo').appendChild(getExpertEdition([inter], root));
	return template.content;
};
