/** @format */

import { toggleSelectedVE } from '@src/restricted-api-requests';
import { errorPublisher } from '../../../../../../event-bus/event-bus.js';
import {
	selectedVirtualEditions,
	updateVeSelection,
} from '../../../../../../store/selected-ve-store';

const onCheckboxChange = async (node, edition, target) => {
	const ed = {
		name: edition.acronym,
		externalId: edition.externalId,
		selected: target.checked,
	};
	const vEdition = node.user
		? await toggleSelectedVE(ed).catch(({ message }) => errorPublisher(message))
		: node.virtualEditions.find(ve => {
				if (ve.externalId === ed.externalId) {
					ve.selected = ed.selected;
					return ve;
				}
		  });

	if (vEdition) {
		updateVeSelection(ed);
		node.updateEdition(vEdition);
	}
};

export default ({ node, edition }) => {
	return (
		<>
			<input
				id="action_1"
				type="checkbox"
				style={{ height: '15px', width: '15px' }}
				checked={edition.selected || selectedVirtualEditions.includes(edition.acronym)}
				onChange={({ target }) => onCheckboxChange(node, edition, target)}
			/>
			<ldod-tooltip
				data-ref={`tr[id="${edition.externalId}"] #action_1`}
				data-virtualtooltipkey={`action_1`}
				placement="left"
				content={node.getConstants(`action_1`)}></ldod-tooltip>
		</>
	);
};
