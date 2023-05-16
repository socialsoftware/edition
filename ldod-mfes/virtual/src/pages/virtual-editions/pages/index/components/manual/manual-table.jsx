/** @format */

import { isDev } from '@src/utils';
import { textFragmentInter } from '../../../../../../external-deps';
import AddFragments from './add-fragments';

const getCurrIndex = (node, id) => node.inters.map(({ externalId }) => externalId).indexOf(id);

const getNewIndex = (node, id, up) => {
	const currIndex = getCurrIndex(node, id);
	if (up) {
		return currIndex > node.getFirstVisibleRowIndex()
			? node.getPrevVisibleRowIndex(id)
			: currIndex;
	} else {
		return currIndex < node.getLastVisibleRowIndex()
			? node.getNextVisibleRowIndex(id)
			: currIndex;
	}
};

const onTop = (node, externalId, reverse = false) => {
	const oldIndex = getCurrIndex(node, externalId);
	const newIndex = reverse ? node.getLastVisibleRowIndex() : node.getFirstVisibleRowIndex();
	if (oldIndex === newIndex) return;
	node.rowUpdate({ externalId, newIndex, oldIndex, changed: true });
};

const onRemove = (node, externalId) => {
	node.rowHide({ externalId, hidden: true });
};

export const addTableRow = (node, inter, index) => {
	const useInter = inter.usedList[0];
	const id = inter.externalId;
	return {
		externalId: id,
		newNumber: ++index,
		number: inter.number,
		title: inter.title,
		actions: (
			<div
				style={{
					display: 'flex',
					justifyContent: 'center',
					gap: '8px',
				}}>
				<span
					is="ldod-span-icon"
					icon="angles-up"
					size="14px"
					fill="#333"
					title="Move to top"
					onClick={() => onTop(node, id)}></span>
				<span
					is="ldod-span-icon"
					icon="angles-down"
					size="14px"
					fill="#333"
					title="Move to bottom"
					onClick={() => onTop(node, id, true)}></span>
				<span
					is="ldod-span-icon"
					icon="arrow-up"
					size="14px"
					fill="#333"
					title="Move up"
					onClick={() =>
						node.changePosition(id, getCurrIndex(node, id), getNewIndex(node, id, true))
					}></span>
				<span
					is="ldod-span-icon"
					icon="arrow-down"
					size="14px"
					fill="#333"
					title="Move down"
					onClick={() =>
						node.changePosition(
							id,
							getCurrIndex(node, id),
							getNewIndex(node, id, false)
						)
					}></span>
				<span
					is="ldod-span-icon"
					icon="trash"
					size="14px"
					fill="#dc3545"
					title="Remove fragment"
					onClick={() => onRemove(node, id)}></span>
			</div>
		),
		useEdition: (
			<a
				is="nav-to"
				content
				target="_blank"
				to={`${textFragmentInter(useInter.xmlId, useInter.urlId)}`}>
				{`-> ${useInter.shortName}`}
			</a>
		),
		search: JSON.stringify(inter),
	};
};

const getTableData = node => {
	return node.inters.map((inter, index) => addTableRow(node, inter, index));
};

const onAddFragments = node => {
	node.querySelector('ldod-bs-modal#virtual-add-fragments-modal').toggleAttribute('show');
};

export default ({ node }) => {
	return (
		<div id="virtual-manual-table-wrapper">
			<div class="button-container">
				<button class="btn btn-primary" onClick={node.onOpenAddFragsModal}>
					{node.getConstants('addFrags')}
				</button>
			</div>

			<ldod-table
				id="virtual-manualTable"
				classes="table  table-hover"
				headers={node.constants.manualHeaders}
				data={getTableData(node)}
				data-rows={node.inters.length}
				constants={node.constants[node.language]}
				data-searchkey="externalId"></ldod-table>
		</div>
	);
};
