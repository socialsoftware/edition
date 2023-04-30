/** @format */
import { parseRawHTML } from '../../../utils';
export default ({ title, variations, headers }) => {
	const frag = parseRawHTML(/*html*/ `
		<div>
			<h4 data-key="variations">
				${title}
				<span>${variations.length}</span>
			</h4>
			<table id="text-variationsTable" class="table table-condensed">
				<thead>
					<tr>
						${headers
							.map(
								({ editor, shortName, title }) => /*html*/ `
								<th>
								${shortName || editor}
								<br />
								${title}
							</th>
							`
							)
							.join('')}
						</tr>
					</thead>
					<tbody>
						${variations
							.map(
								row => /*html*/ `
						<tr>
							${row
								?.map(
									variation => /*html*/ `
							<td>${variation}</td>
							`
								)
								.join('')}
						</tr>
						`
							)
							.join('')}
					</tbody>
				</table>
			</div>
			`);
	return frag;
};
