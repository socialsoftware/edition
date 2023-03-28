/** @format */

import { createClassGame } from '@src/restricted-api-requests';

const addZero = num => String(num).padStart(2, '0');

const getCurrentDateTime = () => {
	const date = new Date();
	return `${date.getFullYear()}-${addZero(date.getMonth() + 1)}-${addZero(
		date.getDate()
	)}T${addZero(date.getHours())}:${addZero(date.getMinutes())}`;
};

const onCreateCG = (e, node) => {
	e.preventDefault();
	const form = e.target;
	const gameBody = Object.fromEntries(new FormData(form));
	createClassGame(node.edition.externalId, gameBody).then(data => {
		node.updateTable(data);
		form.reset();
	});
};

export default ({ node }) => {
	return (
		<>
			<h5 class="text-center">{node.getConstants('createCG')}</h5>
			<div class="mb-4">
				<label class="control-label">
					<strong>{node.getConstants('players')}:</strong>
				</label>
				<em>
					{node.publicAnnotation
						? node.getConstants('all')
						: node.getConstants('members')}
				</em>
			</div>
			<form onSubmit={e => onCreateCG(e, node)}>
				<div
					style={{
						display: 'flex',
						justifyContent: 'space-between',
						columnGap: '16px',
					}}>
					<div style={{ width: '90%' }}>
						<div
							style={{
								display: 'flex',
								justifyContent: 'space-between',
								columnGap: '16px',
								marginBottom: '16px',
							}}>
							<div style={{ width: '100%' }} class="form-floating">
								<input
									id="gameDescription"
									class="form-control"
									type="text"
									name="description"
									required
									placeholder={node.getConstants('description')}
								/>
								<label>{node.getConstants('description')}</label>
							</div>

							<div style={{ width: '100%' }} class="form-floating">
								<input
									id="gameDateTime"
									class="form-control"
									type="datetime-local"
									name="date"
									value={getCurrentDateTime()}
									required
									placeholder={node.getConstants('date')}
								/>
								<label>{node.getConstants('date')}</label>
							</div>
						</div>

						<div class="form-floating">
							<select
								name="interExternalId"
								class="form-select"
								id="gameInter"
								required
								placeholder={node.getConstants('inter')}>
								{node.inters.map(inter => (
									<option key={crypto.randomUUID()} value={inter.externalId}>
										{inter.title}
									</option>
								))}
							</select>
							<label for="public">{node.getConstants('inter')}</label>
						</div>
					</div>
					<button style={{ width: '10%' }} type="submit" class="btn btn-primary">
						{node.getConstants('create')}
					</button>
				</div>
			</form>
		</>
	);
};
