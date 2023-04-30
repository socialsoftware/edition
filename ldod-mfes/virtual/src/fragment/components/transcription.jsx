/** @format */

import { dom } from '@shared/utils.js';
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
						{dom(inter.transcription)}
					</div>
					{taxonomy.canManipulateAnnotation && (
						<div
							id="virtual-associateButton"
							style={{ display: 'flex', justifyContent: 'end' }}>
							<button
								id="virtual-categoryAssociationBtn"
								title={`Associate new category to '${inter.title}' interpretation`}
								type="button"
								class="btn btn-sm btn-primary"
								onClick={root.associateTag}>
								<span
									class="icon icon-plus"
									style={{
										margin: '0',
										pointerEvents: 'none',
									}}></span>
							</button>
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
