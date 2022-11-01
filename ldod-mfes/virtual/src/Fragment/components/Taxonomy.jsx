try {
  await import('shared/table.js');
} catch (error) {
  await import('shared/table-dev.js');
}

const getTableData = (inter) => {
  return inter.categories.map((cat) => ({
    tag: (
      <a
        is="nav-to"
        to={`/virtual/edition/acronym/${cat.veAcronym}/category/${cat.name}`}>
        {cat.name}
      </a>
    ),
    user: cat.users.map((user) => (
      <a
        is="nav-to"
        to={`/virtual/edition/user/${user.username}`}>{`${user.firstname} ${user.lastname} (${user.username})`}</a>
    )),
  }));
};

export default ({ node, inter }) => {
  return (
    <ldod-table
      id="virtual-interTaxonomyTable"
      classes="table  table-hover"
      headers={node.constants.taxonomyHeaders}
      data={getTableData(inter)}
      constants={node.constants.taxonomyHeadersIcons}></ldod-table>
  );
};
