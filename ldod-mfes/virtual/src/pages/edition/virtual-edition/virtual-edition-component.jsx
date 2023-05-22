/** @format */

import references from '@src/references';
import { textFragmentInter } from '../../../external-deps';

import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export default ({ node }) => {
	return (
		<div>
			<h3 class="text-center">
				<span data-virtual-key="virtualEdition">{node.getConstants('virtualEdition')}</span>
				: <span>{node.title}</span>
			</h3>
			<div id="virtualEditionInfo">
				<p id="editors">
					<strong data-virtual-key="editors">{node.getConstants('editors')}</strong>
					<span>: </span>
					{node.participants.map((editor, index) => (
						<>
							<a
								is="nav-to"
								to={references.user(
									editor.username
								)}>{`${editor.firstname} ${editor.lastname}`}</a>
							{index === node.participants.length - 1 ? '' : ', '}
						</>
					))}
				</p>
				<p id="synopse">
					<strong data-virtual-key="synopse">{node.getConstants('synopse')}</strong>
					<span>: </span>
					{node.virtualEdition?.synopsis}
				</p>
				<p id="taxonomy">
					<strong data-virtual-key="taxonomy">{node.getConstants('taxonomy')}</strong>
					<span>: </span>
					<a is="nav-to" to={references.taxonomy(node.virtualEdition?.acronym)}>
						{node.virtualEdition?.title}
					</a>
				</p>
				<p id="fragments">
					<strong>{node.virtualEdition?.virtualEditionInters.length} </strong>
					<strong data-virtual-key="fragments">{node.getConstants('fragments')}</strong>
				</p>
			</div>
			<ldod-table
				id="virtual-virtualEditionTable"
				classes="table table-bordered table-hover"
				headers={node.constants.veHeaders}
				data={node.virtualEdition.virtualEditionInters.map(inter => {
					const frag = inter.usedList[0];
					return {
						externalId: inter.externalId,
						data: () => ({
							number: inter.number,
							title: (
								<a
									is="nav-to"
									content
									to={textFragmentInter(inter.xmlId, inter.urlId)}>
									{inter.title}
								</a>
							),
							category: inter.categories.map((cat, i) => (
								<div key={crypto.randomUUID()}>
									<a is="nav-to" to={references.category(inter.shortName, cat)}>
										{cat}
									</a>
								</div>
							)),
							useEdition: (
								<>
									<span>{'-> '}</span>
									<a
										is="nav-to"
										content
										to={textFragmentInter(frag.xmlId, frag.urlId)}>
										{frag.shortName}
									</a>
								</>
							),
						}),
						search: JSON.stringify(inter),
					};
				})}
				constants={node.constants}
				language={node.language}
				data-searchkey="externalId"></ldod-table>
		</div>
	);
};
