import Checkboxes from './checkboxes';
import { dom } from '@shared/utils.js';
import { isSingleAndSourceInter } from '../utils';
import Title from './title';
import { getSourceInter } from '../../fragments/components/source-inter';
import VariationsTable from './variations-table';
import EditorialInterTranscription from './editorial-inter-transcription';

const getAlignClass = node => (node.transcriptionCheckboxes.align ? 'font-monospace' : 'font-georgia');

export default ({ node, inters }) => {
	return (
		<>
			<Checkboxes node={node} checkboxes={['line', 'align']} />
			<div id="text-transcriptionsSideBySide" class="sideBySide">
				{inters.map((inter, key) => {
					return isSingleAndSourceInter([inter]) ? (
						<div class="text-sideBySideWrapper">
							<Title title={node.data.title} />
							<div class="well authorialStyle" id="transcriptionContainer">
								<p class={getAlignClass(node)}>{dom(node.data.transcriptions[key])}</p>
							</div>
						</div>
					) : (
						<div class="text-sideBySideWrapper">
							<Title title={node.data.title} />
							<EditorialInterTranscription node={node} clazz={getAlignClass(node)} key={key} />
						</div>
					);
				})}
			</div>
			<div class="sideBySide">
				{inters.map(inter => (
					<>
						<br />
						<div id="interMetaIndo" class="well" style={{ width: '50%' }}>
							{getSourceInter(inter, node, 0)}
						</div>
					</>
				))}
			</div>
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
