export default ({ node }) => {
  return (
    <ldod-table
      id="virtual-manageVeTable"
      classes="table table-bordered table-hover"
      headers={node.constants.headersManage}
      data={node.virtualEditions.map((row) => ({
        externalId: row.externalId,
        data: () => ({
          acronym: row.acronym,
          title: row.title,
          editors: row.activeMembers.join(', '),
          categories: row.categories.join(', '),
          annotations: row.annotations.join(', '),
          remove: (
            <span
              id={row.externalId}
              data-acrn={row.acronym}
              class="icon icon-trash"
              onClick={node.handleRemoveVE}></span>
          ),
        }),
        search: JSON.stringify(row),
      }))}
      constants={node.constants}
      language={node.language}
      data-searchkey="externalId"></ldod-table>
  );
};
