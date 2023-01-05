import { dom } from '@shared/utils.js';

export default ({ title, variations, headers }) => {
	return (
		<div>
			<h4 data-key="variations">
				{title}
				<span> ({variations.length})</span>
			</h4>
			<table id="text-variationsTable" class="table table-condensed">
				<thead>
					<tr>
						{headers.map(({ editor, shortName, title }, index) => (
							<th key={index}>
								{shortName || editor}
								<br />
								{title}
							</th>
						))}
					</tr>
				</thead>
				<tbody>
					{variations.map((row, index) => (
						<tr key={index}>
							{row?.map((variation, index) => (
								<td key={index}>{dom(variation)}</td>
							))}
						</tr>
					))}
				</tbody>
			</table>
		</div>
	);
};
