/** @format */

import.meta.env.DEV ? await import('@ui/table-dev.js') : await import('@ui/table.js');

export default ({ node }) => {
	return (
		<div>
			<h3 class="text-center">
				<span data-virtual-key="game">{node.getConstants('game')}</span>:{' '}
				<span>{node.game.veTitle}</span>
			</h3>
			<div id="gameData">
				<h4>
					<div class="text-center">
						<span data-virtual-key="winner">{node.getConstants('winner')}</span>
						<span>: </span>
						<span>{node.game.winnerUsername}</span>
					</div>
					<div class="text-center">
						<span data-virtual-key="category">{node.getConstants('category')}</span>
						<span>: </span>
						<span>{node.game.winnerUsername}</span>
					</div>
				</h4>
			</div>
			<ldod-table
				id="virtual-veClassGameTable"
				classes="table  table-hover"
				headers={node.constants.gameTable}
				data={node.game.participants
					.sort((a, b) => b.score - a.score)
					.map((inter, index) => {
						return {
							externalId: inter.externalId,
							position: ++index,
							username: inter.username,
							score: inter.score.toString(),
							search: JSON.stringify(inter),
						};
					})}
				constants={node.constants}
				language={node.language}
				data-searchkey="externalId"></ldod-table>{' '}
		</div>
	);
};
