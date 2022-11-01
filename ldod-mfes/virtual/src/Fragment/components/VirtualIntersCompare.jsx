const getTag = (acrn, urldId, name) => (
  <div>
    <a is="nav-to" to={`/virtual/edition/acronym/${acrn}/category/${urldId}`}>
      {name}
    </a>
  </div>
);

export default ({ node }) => {
  return (
    <div>
      <h4 class="text-center" data-virtual-key="veCompare">
        {node.getConstants('veCompare')}
      </h4>
      <div>
        {node.inters.map((inter, index) => {
          const rows = inter.tags.concat(inter.annotations);

          return (
            <div style={{ padding: '20px' }}>
              <h5>
                <strong data-virtual-key="edition">
                  {node.getConstants('edition')}
                </strong>
                : {inter.acronym}
              </h5>
              <ldod-table
                id="virtual-intersTaxonomyComparison"
                classes="table table-bordered table-hover"
                headers={node.constants.taxonomyCompareHeaders}
                language={node.language}
                constants={node.constants}
                data={rows.map((row) => {
                  console.log(row);
                  return {
                    quote: row.quote || '---',
                    comment: row.human ? (
                      row.text
                    ) : row.human === false ? (
                      <>
                        <div>
                          <a href={row.sourceLink} target="_blank">
                            tweet
                          </a>
                        </div>
                        <div>
                          <a href={row.profileURL} target="_blank">
                            profile
                          </a>
                        </div>
                        <div>{row.date}</div>
                        {row.country !== 'unknown' && <div>{row.country}</div>}
                      </>
                    ) : (
                      '---'
                    ),
                    user: (
                      <a
                        is="nav-to"
                        to={`/virtual/edition/user/${row.username}`}>
                        {row.username}
                      </a>
                    ),
                    tags: row.tags
                      ? row.tags.map((tag) =>
                          getTag(tag.acronym, tag.urlId, tag.name)
                        )
                      : row.acronym
                      ? getTag(row.acronym, row.urlId, row.name)
                      : '---',
                  };
                })}></ldod-table>
            </div>
          );
        })}
      </div>
    </div>
  );
};
