/** @format */
import './associate-tag-btn.js';
import { htmlRender } from '@core';
import Taxonomy from './taxonomy';

export default ({ root, inter, taxonomy }) => {
	return (
		<>
			<h4>
				{`${inter.editionTitle} - `}
				<span data-virtual-key="uses">{root.getConstants('uses')}</span>
				<span>{` ${inter.usesEditionReference} (${inter.usesReference})`}</span>
			</h4>
			<div id="virtual-nodeReference">
				<div id="virtual-content">
					<h4 class="text-center">{inter.title}</h4>
					<div id="virtual-transcription" class="well">
						<p></p>
						{htmlRender(inter.transcription)}
					</div>
					{taxonomy.canManipulateAnnotation && (
						<div
							id="virtual--associate-tag"
							style={{ display: 'flex', justifyContent: 'end' }}>
							<button
								is="virtual-associate-tag"
								title={`Associate new category to '${inter.title}' interpretation`}></button>
						</div>
					)}
				</div>
			</div>
			<div id="virtual-taxonomy">
				<Taxonomy root={root} inter={inter} />
			</div>
		</>
	);
};
