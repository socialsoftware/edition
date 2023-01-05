import Checkboxes from './checkboxes';
import VariationsTable from './variations-table';
import { dom } from '@shared/utils.js';
import Title from './title';

const getAlignClass = node => (node.transcriptionCheckboxes.align ? 'font-monospace' : 'font-georgia');

export default ({ node, inters }) => {
	return (
		<>
			<Checkboxes node={node} checkboxes={inters.length <= 2 ? ['line', 'align'] : ['align']} />
			<div style={{ display: 'flex', justifyContent: 'center' }}>
				<Title title={node.data.title} />
				<span id="lineByLineTranscriptions" class="icon-flex icon-info"></span>
				<ldod-tooltip
					data-ref="span#lineByLineTranscriptions"
					data-tooltipkey="lineByLineInfo"
					placement="top"
					light
					width="200px"
					content={node.getConstants('lineByLineInfo')}></ldod-tooltip>
			</div>

			<div id="text-transcriptionsLineByLine" class="lineByLine">
				<div class="well authorialStyle" id="transcriptionContainer">
					<p class={getAlignClass(node)}>{dom(node.data.transcriptions[0])}</p>
				</div>
			</div>
			<br />
			<div>
				<VariationsTable
					title={node.getConstants('variations')}
					headers={inters.map(({ shortName, editor }) => ({
						editor,
						shortName,
						title: node.data.title,
					}))}
					variations={node.data.variations}
				/>
			</div>
		</>
	);
};
