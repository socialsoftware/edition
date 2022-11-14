import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export default ({ node }) => {
  const data = node.taxonomy;
  return (
    <div>
      <h3 class="text-center">
        <span data-virtual-key="taxonomy">{node.getConstants('taxonomy')}</span>
        : <span>{data.veTitle}</span>
      </h3>
      <div id="categoryInfo">
        <p id="virtualEdition">
          <strong data-virtual-key="virtualEdition">
            {node.getConstants('virtualEdition')}
          </strong>
          <span>: </span>
          {
            <a is="nav-to" to={`/virtual/edition/acronym/${data.veAcronym}`}>
              {data.veTitle}
            </a>
          }
        </p>

        <p id="fragments">
          <strong>{data.categories.length || '0'} </strong>
          <strong data-virtual-key="fragments">
            {node.getConstants('fragments')}
          </strong>
        </p>
      </div>
      <ldod-table
        id="virtual-taxonomyVeTable"
        classes="table table-bordered table-hover"
        headers={node.constants.taxonomyHeaders}
        data={data.categories.map((inter) => {
          return {
            externalId: inter.externalId,
            data: () => ({
              category: (
                <a
                  is="nav-to"
                  to={`/virtual/edition/acronym/${inter.veAcronym}/category/${inter.name}`}>
                  {inter.name}
                </a>
              ),

              users: inter.users.map((user) => (
                <div>
                  <a
                    is="nav-to"
                    to={`/virtual/edition/user/${user.username}`}>{`${user.firstname} ${user.lastname} (${user.username})`}</a>
                </div>
              )),
              editions: inter.editions.map((edition) => (
                <div>
                  <a
                    is="nav-to"
                    to={`/virtual/edition/acronym/${edition.acronym}`}>
                    {edition.title}
                  </a>
                </div>
              )),
              inters: inter.veInters.map((inter) => (
                <div>
                  <a
                    is="nav-to"
                    to={`/text/fragment/${inter.xmlId}/inter/${inter.urlId}`}>
                    {inter.title}
                  </a>
                </div>
              )),
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
