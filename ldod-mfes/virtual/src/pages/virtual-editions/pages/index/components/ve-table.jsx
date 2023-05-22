/** @format */

import references from '@src/references';
import CancelParticipantIcon from './edition-actions/CancelParticipantIcon';
import ManageVEIcon from './edition-actions/ManageVEIcon';
import SelectVEInput from './edition-actions/SelectVEInput';
import SubmitParticipantIcon from './edition-actions/SubmitParticipantIcon';

const LDOD_ARCIVE_ARCHIVE = 'LdoD-Arquivo';

const isMember = ve => ve.member?.active;
const isPending = ve => ve.member?.pending;
const canBeAdded = ve => ve.pub && ve.member?.canBeAdded;
const isLdodEdition = ve => ve.acronym === LDOD_ARCIVE_ARCHIVE;

const getTableData = node => {
	return node.virtualEditions
		.filter(
			ve => (ve.pub && !isLdodEdition(ve)) || ve.activeMembers.includes(node.user?.username)
		)
		.map(ve => {
			return {
				externalId: ve.externalId,
				data: () => ({
					acronym: ve.acronym,
					title: (
						<a is="nav-to" to={references.virtualEdition(ve.acronym)}>
							{ve.title}
						</a>
					),
					date: ve.date,
					access: (
						<span data-key={ve.pub ? 'public' : 'private'}>
							{ve.pub ? node.getConstants('public') : node.getConstants('private')}
						</span>
					),

					actions: (
						<div class="actions-containers">
							{!isLdodEdition(ve) && <SelectVEInput node={node} edition={ve} />}
							{isMember(ve) && <ManageVEIcon node={node} edition={ve} />}
							{canBeAdded(ve) && <SubmitParticipantIcon node={node} edition={ve} />}
							{isPending(ve) && <CancelParticipantIcon node={node} edition={ve} />}
						</div>
					),
				}),
				search: JSON.stringify(ve),
			};
		});
};

export default ({ node, constants }) => {
	return (
		<>
			<ldod-table
				language={node.language}
				id="virtual-virtualEditionsTable"
				classes="table  table-hover"
				headers={constants.headersVe}
				data={getTableData(node)}
				constants={constants}
				data-searchkey="externalId"></ldod-table>
			{[1, 2, 3, 4].map(ind => (
				<ldod-tooltip
					data-ref={`table#virtual-virtualEditionsTable thead>tr th:nth-child(${ind})`}
					data-virtualtooltipkey={`headerVeInfo_${ind}`}
					placement="left"
					content={node.getConstants(`headerVeInfo_${ind++}`)}></ldod-tooltip>
			))}
		</>
	);
};
