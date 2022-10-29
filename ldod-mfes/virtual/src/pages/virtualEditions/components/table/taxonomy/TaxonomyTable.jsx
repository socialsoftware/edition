export default ({ node }) => {
  return (
    <ldod-table
      id="virtual-taxonomyTable"
      classes="table  table-hover"
      headers={node.constants.taxonomyHeaders}
      data-rows={node.taxonomy.categories.length}
      data={node.taxonomy.categories
        .sort((a, b) => a.name.localeCompare(b.name))
        .map((cat) => ({
          externalId: cat.externalId,
          data: () => ({
            category: (
              <a
                is="nav-to"
                to={`/virtual/edition/acronym/${cat.veAcronym}/category/${cat.name}`}>
                {cat.name}
              </a>
            ),
            fragments: cat.veInters.map((inter) => (
              <div>
                <a
                  is="nav-to"
                  to={`/text/fragment/${inter.xmlId}/inter/${inter.urlId}`}>
                  {inter.title}
                </a>
              </div>
            )),
            actions: (
              <div class="flex-center">
                {cat.veInters.length > 1 && (
                  <>
                    <span
                      id="extractIcon"
                      class="icon icon-unmerge"
                      onClick={() => node.onExtractFrags(cat)}></span>
                    <ldod-tooltip
                      data-ref={`tr[id='${cat.externalId}'] span#extractIcon`}
                      data-virtual-tooltip-key="extractIcon"
                      width="300px"
                      placement="left"
                      content={node.getConstants('extractInfo')}></ldod-tooltip>
                  </>
                )}
                <span
                  title="Edit category name"
                  class="icon icon-edit"
                  onClick={() => node.onOpenEditModal(cat)}></span>
                <span
                  title="Delete category from the Taxonomy"
                  style={{ marginLeft: '0' }}
                  class="icon icon-trash"
                  onClick={() =>
                    node.onDeleteCategories(cat.externalId)
                  }></span>
              </div>
            ),
          }),
          search: JSON.stringify(cat),
        }))}
      constants={node.constants[node.language]}
      data-searchkey="externalId"></ldod-table>
  );
};
