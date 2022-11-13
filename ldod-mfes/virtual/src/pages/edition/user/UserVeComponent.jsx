import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export default ({ node }) => {
  return (
    <div>
      <h3 class="text-center">
        <span>{node.title}</span>
      </h3>
      <div id="userVeInfo">
        <p id="editions">
          <strong data-virtual-key="editions">
            {node.getConstants('editions')}
          </strong>
          <span>: </span>
          {node.veUser.publicVirtualEditions.map(
            ({ acronym, title }, index) => (
              <>
                <a is="nav-to" to={`/virtual/edition/acronym/${acronym}`}>
                  {title}
                </a>
                {index === node.veUser.publicVirtualEditions.length - 1
                  ? ''
                  : ', '}
              </>
            )
          )}
        </p>
        {node.veUser.gameDtos?.length > 0 && (
          <>
            <p id="participant">
              <strong data-virtual-key="participant">
                {node.getConstants('participant')}
              </strong>
              <span>: </span>
              {node.veUser.gameDtos.map((game, index) => (
                <>
                  <a
                    is="nav-to"
                    to={`/virtual/virtual-editions/${game.veExternalId}/game/${game.externalId}`}>
                    {`${game.veTitle} - ${game.title}`}
                  </a>
                  {index === node.veUser.gameDtos.length - 1 ? '' : ', '}
                </>
              ))}
            </p>

            <p id="points">
              <strong data-virtual-key="score">
                {node.getConstants('score')}
              </strong>
              <span>: </span>
              {node.veUser.score}
            </p>
            <p id="position">
              <strong data-virtual-key="position">
                {node.getConstants('position')}
              </strong>
              <span>: </span>
              {node.veUser.position}
            </p>
          </>
        )}

        <p id="fragments">
          <strong>{node.veUser.fragInters.length || '0'} </strong>
          <strong data-virtual-key="fragments">
            {node.getConstants('fragments')}
          </strong>
        </p>
      </div>
      <ldod-table
        id="virtual-userVeTable"
        classes="table table-bordered table-hover"
        headers={node.constants.userVeHeaders}
        data={node.veUser.fragInters.map((inter) => {
          const frag = inter.usedList[0];
          return {
            externalId: inter.externalId,
            data: () => ({
              title: (
                <a
                  is="nav-to"
                  to={`/text/fragment/${inter.xmlId}/inter/${inter.urlId}`}>
                  {inter.title}
                </a>
              ),
              edition: (
                <a
                  is="nav-to"
                  to={`/virtual/edition/acronym/${inter.shortName}`}>
                  {inter.shortName}
                </a>
              ),
              category: inter.categories.map((cat, index, arr) => (
                <>
                  <a
                    is="nav-to"
                    to={`/virtual/edition/acronym/${inter.shortName}/category/${cat}`}>
                    {cat}
                  </a>
                  {index === arr.length - 1 ? '' : ', '}
                </>
              )),
              useEdition: (
                <>
                  <span>{'-> '}</span>
                  <a
                    is="nav-to"
                    to={`/text/fragment/${frag.xmlId}/inter/${frag.urlId}`}>
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