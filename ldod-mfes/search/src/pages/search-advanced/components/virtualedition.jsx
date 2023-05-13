/** @format */

import { htmlRender } from '@core';
export default ({ root, form }) => {
	return (
		<div id="wrapper">
			<div class="form-floating">
				<select name="inclusion" class="form-select">
					<option value="in" data-search-key="includedIn">
						{root.getConstants('includedIn')}
					</option>
					<option value="out" data-search-key="excludedFrom">
						{root.getConstants('excludedFrom')}
					</option>
				</select>
				<label data-search-key="veInclusion">{root.getConstants('veInclusion')}</label>
			</div>
			<div class="form-floating">
				<select name="edition" class="form-select">
					<option value="all" data-search-key="all">
						{root.getConstants('all')}
					</option>
					{root.data.virtualEditions.map(ve => (
						<option value={ve.veAcronym}>{htmlRender(ve.title)}</option>
					))}
				</select>
				<label data-search-key="virtualEdition">
					{root.getConstants('virtualEdition')}
				</label>
			</div>
		</div>
	);
};
