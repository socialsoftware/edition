import { Link } from 'react-router-dom';

const getEditorialtitle = ({ title, editor }) =>
  `${title} ${editor ? `(${editor})` : ''}`;

const SimpleSearchResultEntry = (entry) => {
  const result = {
    fragment: (
      <Link to={`/fragments/fragment/${entry.xmlId}`}>
        {entry.fragment_title}
      </Link>
    ),
    interpts: (
      <Link to={`/fragments/fragment/${entry.xmlId}/inter/${entry.urlId}`}>
        {entry.type === 'EDITORIAL'
          ? getEditorialtitle(entry)
          : entry.shortName}
      </Link>
    ),
  };

  return {
    ...result,
    searchData: JSON.stringify(result),
  };
};

export function SimpleSearchResultsModel(data) {
  return {
    fragCount: data.fragCount,
    interCount: data.interCount,
    tableData: data.listFragments.map((entry) =>
      SimpleSearchResultEntry(entry)
    ),
  };
}
