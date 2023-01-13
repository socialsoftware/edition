import Checkboxes from './checkboxes';
import Title from './title';
import { getExpertEdition } from '../../fragments/components/expert-edition';
import EditorialInterTranscription from './editorial-inter-transcription';
import { readingExpertEdition } from '../../../external-deps';

export default async ({ node, inter }) => {
	return (
		<>
			<Checkboxes node={node} checkboxes={['diff']} />
			<div style={{ display: 'flex', justifyContent: 'center' }}>
				<Title title={node.data.title} />
				<a is="nav-to" to={readingExpertEdition(node.xmlId, inter.urlId)}>
					<span
						is="ldod-span-icon"
						icon="eye"
						size="20px"
						hover-fill="#0d6efd"
						style={{ marginLeft: '10px' }}></span>
				</a>
			</div>
			<EditorialInterTranscription node={node} key={0} />
			<br />
			<div id="interMetaIndo" class="well">
				{getExpertEdition([inter], node)}
			</div>
		</>
	);
};
