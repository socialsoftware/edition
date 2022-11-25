import { removeClassGame } from '@src/restrictedApiRequests';
import { virtualReferences } from '../../../../../../virtual';
import constants from './constants';

const onRemove = async (id, gameId, node) => {
  const data = await removeClassGame(id, gameId);
  node.updateData(data);
};

const getTableData = (node) => {
  return node.games.map((game) => {
    return {
      externalId: game.externalId,
      data: () => ({
        description: game.description,
        title: (
          <>
            <span
              class={!game.active ? 'success icon icon-asteris' : ''}></span>
            <a
              is="nav-to"
              to={virtualReferences.game(
                game.veExternalId,
                game.externalId
              )}></a>
            {game.title}
          </>
        ),
        date: game.date,
        publicAnnotation: game.publicAnnotation
          ? node.getConstants('all')
          : node.getConstants('members'),
        category: game.category,
        players: game.players.map((player) => {
          return (
            <a is="nav-to" to={virtualReferences.user(player.username)}>
              {player.firstname} ${player.lastname}
            </a>
          );
        }),
        winner: game.winner,
        responsible: game.responsible,
        remove: game.canBeRemoved && (
          <div class="center-container">
            <span
              class="icon-size icon-trash"
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
