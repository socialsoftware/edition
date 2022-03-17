import parserHTML, { domToReact } from 'html-react-parser';
import { Link } from 'react-router-dom';

export const isVirtualEdition = (edition) => edition?.type === 'VIRTUAL';

const options = {
  replace: ({ type, name, attribs, children }) =>
    type === 'tag' &&
    name === 'a' && <Link to={attribs.to ?? '/'}>{domToReact(children)}</Link>,
};

function VirtualEditionEntry(entry) {
  const xmlid = entry?.xmlId;
  const urlid = entry?.urlId;

  const result = {
    number: entry?.number,
    title: (
      <Link
        to={xmlid && urlid && `/fragments/fragment/${xmlid}/inter/${urlid}`}
      >
        {entry?.title}
      </Link>
    ),
    category: parserHTML(
      entry?.categoryList
        ?.reduce(
          (prev, { acronym, name, urlId }) => [
            ...prev,
            `<a to="/edition/acronym/${acronym}/category/${urlId}">${name}</a>`,
          ],
          []
        )
        .join('<br />') ?? '',
      options
    ),
    useEditions: parserHTML(
      entry?.usedList
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

  return { ...result, searchData: JSON.stringify(result) };
}

const Editors = ({ participantList }) =>
  parserHTML(
    participantList
      ?.reduce(
        (prev, user, index) => [
          ...prev,
          `<a to="/edition/user/${user?.userName}">${user?.firstName} ${user?.lastName}</a>`,
        ],
        []
      )
      .join(', ') ?? '',
    options
  );

const Title = ({ title }) => parserHTML(title ?? '');

export function VirtualEditionModel(data) {
  return {
    ...data,
    synopsis: parserHTML(data.synopsis),
    title: Title(data),
    editors: Editors(data),
    type: 'VIRTUAL',
    tableData: data.sortedInterpsList?.map((entry) =>
      VirtualEditionEntry(entry)
    ),
  };  
}
