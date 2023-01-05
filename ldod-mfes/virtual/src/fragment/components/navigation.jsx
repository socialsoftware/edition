import { textFragmentInter } from '../../external-deps';
import { virtualReferences } from '../../references';

export default ({ node }) => {
	return (
		<>
			<div id="virtual-virtualNavContainer" style={{ display: 'flex', justifyContent: 'center' }}>
				<h5 data-virtual-key="virtualEditions" class="text-center">
					{node.getConstants('virtualEditions')}
				</h5>
				<span
					is="ldod-span-icon"
					id="virtual-navigation-title-tooltip"
					icon="circle-info"
					class="icon-flex"
					size="18px"></span>
				<ldod-tooltip
					data-ref="div#virtual-virtualNavContainer span#virtual-navigation-title-tooltip"
					data-virtual-tooltip-key="virtualEditionsInfo"
					placement="bottom"
					light
					width="200px"
					content={node.getConstants('virtualEditionsInfo')}></ldod-tooltip>
			</div>
			{node.inters.map(inter => {
				return (
					<div class="text-center">
						{
							<table
								width="100%"
								style={{
									borderSpacing: '0',
									borderCollapse: 'collapse',
								}}>
								<caption class="text-center">
									<a is="nav-to" to={virtualReferences.virtualEdition(inter.acronym)}>
										{inter.acronym}
									</a>
								</caption>
								<thead>
									<tr>
										{['10%', '10%', '25%', '10%', '25%', '20%'].map((width, i) => (
											<th key={i} style={{ width }}></th>
										))}
									</tr>
								</thead>
								<tbody>
									{inter.canAddInter && inter.member ? (
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<button
													id="virtual-addFragmentInter"
													type="button"
													data-ve-id={inter.veExternalId}
													data-inter-id={inter.interExternalId}
													class="btn btn-primary"
													title={`Add fragment interpretation to ${inter.acronym}`}
													onClick={node.addInterToVe}>
													<span
														class="icon icon-plus"
														style={{
															margin: '0',
															pointerEvents: 'none',
														}}></span>
												</button>
											</td>
											<td></td>
											<td></td>
										</tr>
									) : (
										inter.inters.map((edition, i) => (
											<tr key={i}>
												<td></td>
												<td>
													<input
														name={edition.urlId}
														id={edition.externalId}
														type="checkbox"
														onChange={node.onCheckboxChange}
														checked={node.intersChecked.includes(edition.externalId)}
													/>
												</td>
												<td>
													<a
														is="nav-to"
														to={textFragmentInter(edition.prevXmlId, edition.prevUrlId)}>
														<span class="icon icon-chevron-left"></span>
													</a>
												</td>
												<td>
													<a is="nav-to" to={textFragmentInter(edition.xmlId, edition.urlId)}>
														{edition.number || '0'}
													</a>
												</td>
												<td>
													<a
														is="nav-to"
														to={textFragmentInter(edition.nextXmlId, edition.nextUrlId)}>
														<span class="icon icon-chevron-right"></span>
													</a>
												</td>
												<td></td>
											</tr>
										))
									)}
								</tbody>
							</table>
						}
					</div>
				);
			})}
		</>
	);
};
