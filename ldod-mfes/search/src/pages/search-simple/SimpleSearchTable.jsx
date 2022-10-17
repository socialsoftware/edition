import constants from '../../constants';

const getTableData = (inters) => {
  return inters.map((inter) => {
    return {
      externalId: inter.externalId,
      fragments: (
        <a is="nav-to" to={`/text/fragment/${inter.xmlId}`}>
          {inter.title}
        </a>
      ),
      interpretations: (
        <a
          is="nav-to"
          to={`/text/fragment/${inter.xmlId}/inter/${inter.urlId}`}>
          {`${
            inter.acronym
              ? `${inter.interTitle} (${inter.acronym})`
              : inter.shortName
          } 
          `}
        </a>
      ),
      search: JSON.stringify(inter),
    };
  });
};

export default ({ node }) => {
  return (
    <div id="search-tableContainer">
      <hr />
      <ldod-table
        id="search-simpleTable"
        classes="table table-bordered table-hover"
        headers={constants().headers}
        data={getTableData(node.data)}
        constants={
          constants(node.numberOfFragments, node.numberOfInters)[node.language]
        }
        data-searchkey="externalId"></ldod-table>
    </div>
  );
};
