/** @format */

import { removeClassGame } from '@src/restricted-api-requests';
import references from '@src/references';
import constants from './constants';

const onRemove = async (id, gameId, node) => {
	const data = await removeClassGame(id, gameId);
	node.updateTable(data);
};

const getTableData = node => {
	return node.games.map(game => {
		return {
			externalId: game.externalId,
			data: () => ({
				description: game.description,
				title: (
					<>
						{!game.active && (
							<span
								is="ldod-span-icon"
								icon="asterisk"
								class="success"
								fill="#333"
								size="12px"
								style={{ marginRight: '8px' }}></span>
						)}
						<a
							is="nav-to"
							target="_blank"
							to={references.game(game.veExternalId, game.externalId)}>
							{game.title}
						</a>
					</>
				),
				date: game.date,
				publicAnnotation: game.publicAnnotation
					? node.getConstants('all')
					: node.getConstants('members'),
				category: game.category,
				players: game.players.map(player => {
					return (
						<a
							key={crypto.randomUUID()}
							is="nav-to"
							to={references.user(player.username)}>
							{player.firstname} ${player.lastname}
						</a>
					);
				}),
				winner: game.winner,
				responsible: game.responsible,
				remove: game.canBeRemoved && (
					<div class="center-container">
						<span
							is="ldod-span-icon"
							icon="trash-can"
							fill="#dc3545"
							size="1.25rem"
							onClick={() =>
								onRemove(game.veExternalId, game.externalId, node)
							}></span>
					</div>
				),
			}),
			search: JSON.stringify(game),
		};
	});
};

export default ({ node }) => {
	return (
		<ldod-table
			language={node.language}
			id="virtual-veGames"
			classes="table  table-hover"
			headers={constants.gamesHeaders}
			data={getTableData(node)}
			constants={constants}
			data-searchkey="externalId"></ldod-table>
	);
};
