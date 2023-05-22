/** @format */

import references from '../../references';

const getTag = (acrn, urlId, name) => (
	<div>
		<a is="nav-to" to={references.category(acrn, urlId)}>
			{name}
		</a>
	</div>
);

export default ({ root }) => {
	return (
		<div>
			<h4 class="text-center" data-virtual-key="veCompare">
				{root.getConstants('veCompare')}
			</h4>
			<div>
				{root.inters.map((inter, index) => {
					const rows = inter.tags.concat(inter.annotations);

					return (
						<div style={{ padding: '20px' }}>
							<h5>
								<strong data-virtual-key="edition">
									{root.getConstants('edition')}
								</strong>
								: {inter.acronym}
							</h5>
							<ldod-table
								id="virtual-intersTaxonomyComparison"
								classes="table table-bordered table-hover table-striped"
								headers={root.constants.taxonomyCompareHeaders}
								language={root.language}
								constants={root.constants}
								data={rows.map(row => {
									return {
										quote: row.quote || '---',
										comment: row.human ? (
											row.text
										) : row.human === false ? (
											<>
												<div>
													<a href={row.sourceLink} target="_blank">
														tweet
													</a>
												</div>
												<div>
													<a href={row.profileURL} target="_blank">
														profile
													</a>
												</div>
												<div>{row.date}</div>
												{row.country !== 'unknown' && (
													<div>{row.country}</div>
												)}
											</>
										) : (
											'---'
										),
										user: (
											<a is="nav-to" to={references.user(row.username)}>
												{row.username}
											</a>
										),
										tags: row.tags
											? row.tags.map(tag =>
													getTag(tag.acronym, tag.urlId, tag.name)
											  )
											: row.acronym
											? getTag(row.acronym, row.urlId, row.name)
											: row.tagList?.length
											? row.tagList.map(tag =>
													getTag(inter.acronym, tag, tag)
											  )
											: '---',
									};
								})}></ldod-table>
						</div>
					);
				})}
			</div>
		</div>
	);
};
