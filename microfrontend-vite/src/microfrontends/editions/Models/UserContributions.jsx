import parseHTML, { domToReact } from 'html-react-parser';
import { Link } from 'react-router-dom';

const options = {
  replace: ({ type, name, attribs, children }) =>
    type === 'tag' &&
    name === 'a' && <Link to={attribs.to ?? '/'}>{domToReact(children)}</Link>,
};

const getUserEditions = ({ publicEditionList }) =>
  publicEditionList
    ?.reduce(
      (prev, { acronym, title }) => [
        ...prev,
        `<a to="/edition/acronym/${acronym}">${title}</a>`,
      ],
      []
    )
    .join(', ');

const getGames = ({ games }) =>
  games
    ?.reduce(
      (prev, { virtualExternalId, externalId, interTitle, virtualTitle }) => [
        ...prev,
        `<a to="/virtualeditions/${virtualExternalId}/classificationGame/${externalId}">${virtualTitle} - ${interTitle}</a>`,
      ],
      []
    )
    .join(', ');

const UserContributionEntry = (entry) => {
  const result = {
    title: (
      <Link to={`/fragments/fragment/${entry?.xmlId}/inter/${entry?.urlId}`}>
        {entry?.title}
      </Link>
    ),
    edition: (
      <Link to={`/edition/acronym/${entry?.acronym}`}>{entry?.acronym}</Link>
    ),
    category: parseHTML(
      entry?.categoryList
        ?.reduce(
          (prev, { acronym, urlId, name }) => [
            ...prev,
            `<a to="/edition/acronym/${acronym}/category/${urlId}">${name}</>`,
          ],
          []
        )
        .join(', ') ?? '',
      options
    ),
    useEditions: parseHTML(
      entry?.usedList
        ?.reduce(
          (prev, { urlId, xmlId, shortName }) => [
            ...prev,
            `-> <a to="/fragments/fragment/${xmlId}/inter/${urlId}">${shortName}</>`,
          ],
          []
        )
        .join(', '),
      options
    ),
  };
  return {
    ...result,
    searchData: JSON.stringify(result),
  };
};

export function UserContributionData(data) {
  return {
    title: `${data.userFirst} ${data.userLast} (${data.username})`,
    editions: parseHTML(getUserEditions(data), options),
    games: parseHTML(getGames(data), options),
    score: data.score,
    position: data.position,
    fragments: data.fragInterSize,
    tableData: data.fragInterSet?.map((entry) => UserContributionEntry(entry)),
  };
}
