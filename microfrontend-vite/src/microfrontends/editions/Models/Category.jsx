import parseHTML, { domToReact } from 'html-react-parser';
import { Link } from 'react-router-dom';

const options = {
  replace: ({ type, name, attribs, children }) =>
    type === 'tag' &&
    name === 'a' && <Link to={attribs.to ?? '/'}>{domToReact(children)}</Link>,
};

const CategoryEntry = (data) => {
  const result = {
    title: (
      <Link to={`/fragments/fragment/${data.xmlId}/inter/${data.urlId}`}>
        {data.title}
      </Link>
    ),
    virtualEdition: <Link to={`/edition/acronym/${data.acronym}`}>{parseHTML(data.editionTitle ?? "")}</Link>,
    user:  parseHTML(
      data.userDtoList
        ?.reduce(
          (prev, curr) => [
            ...prev,
            `<a to="/edition/user/${curr.userName}">${curr.firstName} ${curr.lastName} (${curr.userName})</a>`,
          ],
          []
        )
        .join('<br />'),
      options
    ),
    useEditions: parseHTML(
      data.usedList
        ?.reduce(
          (prev, { shortName, urlId, xmlId }) => [
            ...prev,
            `-> <a to="/fragments/fragment/${xmlId}/inter/${urlId}">${shortName}</a>`,
          ],
          []
        )
        .join('<br />') ?? '',
      options
    ),
  };

  return {
    ...result,
    searchData: JSON.stringify(result),
  };
};

export function CategoryModel(data) {
  return {
    ...data,
    title: data.name,
    subtitle: (
      <Link to={`/edition/acronym/${data.acronym}/taxonomy`}>
        {parseHTML(data.title ?? '')}
      </Link>
    ),
    tableData: data.sortedIntersList?.map((entry) => CategoryEntry(entry)),

  };
}
