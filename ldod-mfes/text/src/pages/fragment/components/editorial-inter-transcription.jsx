import { dom } from '@shared/utils.js';

export default ({ node, clazz, key }) => {
	return (
		<>
			<div class="well authorialStyle" id="transcriptionContainer">
				{clazz ? <p class={clazz}>{dom(node.data.transcriptions[key])}</p> : dom(node.data.transcriptions[0])}
			</div>
		</>
	);
};
