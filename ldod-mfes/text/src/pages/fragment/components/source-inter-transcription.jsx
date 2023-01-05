import { dom } from '@shared/utils.js';
import Viewer from './viewer';

export default ({ node, inter }) => {
	return (
		<>
			<div class={node.transcriptionCheckboxes.fac ? 'text-fac-transcription' : 'text-nofac-transcription'}>
				{node.transcriptionCheckboxes.fac && <Viewer surfaceList={inter.surfaceDetailsList ?? []} />}
				<div class="well authorialStyle" id="transcriptionContainer">
					{dom(node.data.transcriptions[0])}
				</div>
			</div>
		</>
	);
};
