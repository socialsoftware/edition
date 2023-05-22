/** @format */

import references from '@src/references';
import { textFragmentInter } from '../../../external-deps';

import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export default ({ node }) => {
	return (
		<div>
			<h3 class="text-center">
				<span data-virtual-key="category">{node.getConstants('category')}</span>:{' '}
				<span>{node.name}</span>
			</h3>
			<div id="categoryInfo">
				<p id="taxonomy">
					<strong data-virtual-key="taxonomy">{node.getConstants('taxonomy')}</strong>
					<span>: </span>
					{
						<a is="nav-to" to={references.taxonomy(node.category?.veAcronym)}>
							{node.category?.veTitle}
						</a>
					}
				</p>

				<p id="fragments">
					<strong>{node.category?.veInters.length || '0'} </strong>
					<strong data-virtual-key="fragments">{node.getConstants('fragments')}</strong>
				</p>
			</div>
			<ldod-table
				id="virtual-categoryVeTable"
				classes="table table-bordered table-hover"
				headers={node.constants.categoryVeHeaders}
				data={node.category.veInters.map(inter => {
					const frag = inter.usedList[0];
					const users = inter.users;
					return {
						externalId: inter.externalId,
						data: () => ({
							title: (
								<a is="nav-to" to={textFragmentInter(inter.xmlId, inter.urlId)}>
									{inter.title}
								</a>
							),
							virtualEdition: (
								<a
									is="nav-to"
									to={references.virtualEdition(inter.shortName)}>
									{inter.editionTitle}
								</a>
							),
							user: users.map(user => (
								<a
									is="nav-to"
									to={references.user(
										user.username
									)}>{`${user.firstname} ${user.lastname} (${user.username})`}</a>
							)),

							useEdition: (
								<>
									<span>{'-> '}</span>
									<a is="nav-to" to={textFragmentInter(frag.xmlId, frag.urlId)}>
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
