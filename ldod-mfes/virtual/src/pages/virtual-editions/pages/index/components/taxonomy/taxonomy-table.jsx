/** @format */

import references from '@src/references';
import { textFragmentInter } from '../../../../../../external-deps';

export default ({ node }) => {
	return (
		<ldod-table
			id="virtual-taxonomyTable"
			classes="table  table-hover"
			headers={node.constants.taxonomyHeaders}
			data-rows={node.taxonomy.categories.length}
			data={node.taxonomy.categories
				.sort((a, b) => a.name.localeCompare(b.name))
				.map(cat => ({
					externalId: cat.externalId,
					data: () => ({
						category: (
							<a
								is="nav-to"
								target="_blank"
								to={references.category(cat.veAcronym, cat.name)}>
								{cat.name}
							</a>
						),
						fragments: cat.veInters.map(inter => (
							<div key={crypto.randomUUID()}>
								<a
									is="nav-to"
									target="_blank"
									to={textFragmentInter(inter.xmlId, inter.urlId)}>
									{inter.title}
								</a>
							</div>
						)),
						actions: (
							<div class="flex-center">
								{cat.veInters.length > 1 && (
									<>
										<span
											is="ldod-span-icon"
											id="extract-icon"
											icon="object-ungroup"
											fill="#0c4ef6"
											size="15px"
											onClick={() => node.onExtractFrags(cat)}></span>
										<ldod-tooltip
											data-ref={`tr[id='${cat.externalId}'] span#extract-icon`}
											data-virtual-tooltip-key="extractIcon"
											width="300px"
											placement="left"
											content={node.getConstants(
												'extractInfo'
											)}></ldod-tooltip>
									</>
								)}
								<span
									title="Edit category name"
									is="ldod-span-icon"
									id="edit-icon"
									icon="pen-to-square"
									fill="#0c4ef6"
									size="15px"
									onClick={() => node.onOpenEditModal(cat)}></span>
								<span
									title="Delete category from the Taxonomy"
									is="ldod-span-icon"
									id="trash-icon"
									icon="trash-can"
									fill="#dc3545"
									size="15px"
									onClick={() => node.onDeleteCategories(cat.externalId)}></span>
							</div>
						),
					}),
					search: JSON.stringify(cat),
				}))}
			constants={node.constants[node.language]}
			data-searchkey="externalId"></ldod-table>
	);
};
