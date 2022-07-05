import parseHTML, { domToReact } from 'html-react-parser';
import { Link } from 'react-router-dom';

const options = {
  replace: ({ type, name, attribs, children }) =>
    type === 'tag' &&
    name === 'a' && <Link to={attribs.to ?? '/'}>{domToReact(children)}</Link>,
};

const TaxonomyEntry = (data) => {
  const result = {
    category: (
      <Link to={`/edition/acronym/${data.acronym}/category/${data.urlId}`}>
        {data.name}
      </Link>
    ),
    users: parseHTML(
      data.sortedUserShortList
        ?.reduce(
          (prev, curr) => [
            ...prev,
            `<a to"/edition/user/${curr.userName}">${curr.firstName} ${curr.lastName} (${curr.userName})</a>`,
          ],
          []
        )
        .join('<br />'),
      options
    ),
    editions: data.sortedTitleList[0] && (
      <Link to={`/edition/acronym/${data.acronym}`}>
        {parseHTML(data.sortedTitleList[0])}
      </Link>
    ),
    interpts: parseHTML(
      data.sortedIntersShortList
        ?.reduce(
          (prev, curr) => [
            ...prev,
            `<a to"/fragments/fragment/${curr.xmlId}/inter/${curr.urlId}">${curr.title}</a>`,
          ],
          []
        )
        .join('<br />'),
      options
    ),
  };

  return {
    ...result,
    searchData: JSON.stringify(result),
  };
};

export function TaxonomyModel(data) {
  return {
    ...data,
    title: parseHTML(data.title ?? ""),
    subTitle: <Link to={`/edition/acronym/${data.acronym}`}>{parseHTML(data.title ?? "")}</Link>,
    tableData: data.categorySet?.map((entry) => TaxonomyEntry(entry)),
  };
}
