/** @format */

import '@ui/tooltip.js';

const countries = [
	'Portugal',
	'Spain',
	'Brazil',
	'United Kingdom',
	'United States',
	'Lebanon',
	'Angola',
	'Mozambique',
];

export default ({ node }) => {
	return (
		<>
			<form id="virtual-editVE" onSubmit={node.onSave} style={{ minWidth: '500px' }}>
				<div class="form-floating" style={{ margin: '20px' }}>
					<input
						class="form-control"
						disabled
						type="text"
						name="date"
						required
						value={node.edition.date}
						placeholder="Date"
					/>
					<label>{node.getConstants('date')}</label>
				</div>
				<div
					style={{
						display: 'grid',
						gap: '20px',
						margin: '20px',
						gridTemplateColumns: 'auto auto auto',
					}}>
					<div class="input-group">
						<span class="input-group-text">LdoD-</span>
						<div class="form-floating">
							<input
								name="acronym"
								type="text"
								class="form-control"
								id="headerVeInfo_1"
								required
								value={node.edition.acronym.substring(5)}
								placeholder="Acronym"
							/>
							<label data-virtualkey="acronym">{node.getConstants('acronym')}</label>
						</div>
					</div>

					<div class="form-floating">
						<input
							id="headerVeInfo_2"
							class="form-control"
							type="text"
							name="title"
							required
							value={node.edition.title}
							placeholder="Title"
						/>
						<label data-virtualkey="title">{node.getConstants('title')}</label>
					</div>

					<div class="form-floating">
						<select name="pub" class="form-select" id="headerVeInfo_4">
							<option
								data-virtualkey="public"
								value="true"
								selected={node.edition.pub === true}>
								{node.getConstants('public')}
							</option>
							<option
								data-virtualkey="private"
								value="false"
								selected={node.edition.pub === false}>
								{node.getConstants('private')}
							</option>
						</select>
						<label for="public">{node.getConstants('access')}</label>
					</div>

					<div class="form-floating">
						<select
							id="headerVeInfo_6"
							name="taxonomyOpenManagement"
							class="form-select"
							value={node.edition.taxonomyOpenManagement}>
							<option value="true" selected={node.edition.taxonomyOpenManagement}>
								{node.getConstants('editionMembers')}
							</option>
							<option value="false" selected={!node.edition.taxonomyOpenManagement}>
								{node.getConstants('editionManagers')}
							</option>
						</select>
						<label for="public">{node.getConstants('taxonomyManagement')}</label>
					</div>

					<div class="form-floating">
						<select
							id="headerVeInfo_7"
							name="taxonomyOpenAnnotation"
							class="form-select"
							value={node.edition.taxonomyOpenAnnotation}>
							<option value="true" selected={node.edition.taxonomyOpenAnnotation}>
								{node.getConstants('regUsers')}
							</option>
							<option value="false" selected={!node.edition.taxonomyOpenAnnotation}>
								{node.getConstants('editionMembers')}
							</option>
						</select>
						<label for="public">{node.getConstants('fragAnnot')}</label>
					</div>

					<div class="form-floating">
						<select
							id="headerVeInfo_8"
							name="taxonomyOpenVocab"
							class="form-select"
							value={node.edition.taxonomyOpenVocab}>
							<option value="true" selected={node.edition.taxonomyOpenVocab}>
								{node.getConstants('open')}
							</option>
							<option value="false" selected={!node.edition.taxonomyOpenVocab}>
								{node.getConstants('closed')}
							</option>
						</select>
						<label for="public">{node.getConstants('vocab')}</label>
					</div>

					<div class="form-floating">
						<select name="mediaSource" class="form-select" id="headerVeInfo_9">
							<option
								value="Twitter"
								selected={node.edition.mediaSource === 'Twitter'}>
								Twitter
							</option>
							<option value="noMediaSource" selected={!node.edition.mediaSource}>
								{node.getConstants('noMediaSource')}
							</option>
						</select>
						<label for="public">{node.getConstants('socialNetwork')}</label>
					</div>

					<div class="form-floating">
						<input
							class="form-control"
							type="date"
							name="beginDate"
							value={node.edition.beginDate}
							placeholder={node.getConstants('citationsBeginDate')}
						/>
						<label>{node.getConstants('citationsBeginDate')}</label>
					</div>

					<div class="form-floating">
						<input
							class="form-control"
							type="date"
							name="endDate"
							value={node.edition.beginDate}
							placeholder={node.getConstants('citationsFinalDate')}
						/>
						<label>{node.getConstants('citationsFinalDate')}</label>
					</div>
				</div>

				<div style={{ display: 'flex', margin: '20px', gap: '20px' }}>
					<div style={{ width: '100%' }}>
						<label style={{ marginBottom: '8px' }}>
							{node.getConstants('location')} (multiple selection) :
						</label>
						<select
							style={{ height: '58px' }}
							multiple
							name="countries"
							class="form-select">
							{countries.map(country => (
								<option
									value={country}
									selected={
										node.edition.countries?.split(',').includes(country)
											? true
											: false
									}>
									{country}
								</option>
							))}
						</select>
					</div>
					<div style={{ width: '100%', marginTop: '28px' }} class="form-floating">
						<input
							id="headerVeInfo_10"
							min={1}
							class="form-control"
							type="number"
							name="minFrequency"
							value={node.edition.minFrequency || 1}
							placeholder={node.getConstants('minFrequency')}
						/>
						<label>{node.getConstants('minFrequency')}</label>
					</div>
				</div>

				<div style={{ margin: '20px' }}>
					<label style={{ marginBottom: '8px' }}>{node.getConstants('synopse')}</label>
					<textarea
						name="synopsis"
						class="form-control"
						id="floatingTextarea2"
						maxLength={1500}
						style={{
							fontFamily: 'sans-serif',
							height: '100px',
							minWidth: '100%',
							maxWidth: '100%',
						}}>
						{node.edition.synopsis}
					</textarea>
				</div>
				<div style={{ display: 'flex', justifyContent: 'end', margin: '20px' }}>
					<button type="submit" class="btn btn-primary">
						{node.getConstants('save')}
					</button>
				</div>
				{[1, 2, 4, 6, 7, 8, 9, 10].map(ind => (
					<ldod-tooltip
						data-ref={`form#virtual-editVE #headerVeInfo_${ind}`}
						data-virtualtooltipkey={`headerVeInfo_${ind}`}
						placement="top"
						width="300px"
						content={node.getConstants(`headerVeInfo_${ind}`)}></ldod-tooltip>
				))}
			</form>
		</>
	);
};
