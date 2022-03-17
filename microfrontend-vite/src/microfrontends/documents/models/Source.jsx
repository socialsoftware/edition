import parserHTML from 'html-react-parser';
import { Link } from 'react-router-dom';

const getDimensionList = (dimensions) =>
  `${
    dimensions
      ?.reduce(
        (prev, cur) => [...prev, ` ${cur.height}cm X ${cur.width}cm`],
        []
      )
      .join('<br />') ?? ''
  }`;

const getSurfaceList = (surfaces, title) =>
  `${
    surfaces
      ?.reduce(
        (prev, curr, index) => [
          ...prev,
          `<a id=${curr}>(${index + 1}) ${title}</a>`,
        ],
        []
      )
      .join('<br />') ?? ''
  }`;

export function Source(
  {
    title,
    transcription,
    date,
    xmlId,
    urlId,
    handNoteDto,
    typeNoteDto,
    sourceType,
    dimensionDtoList,
    surfaceString,
    hadLdoDLabel,
  },
  messages,
  displayDoc
) {
  const handNote = handNoteDto?.desc;
  const typeNote = typeNoteDto?.desc;
  const type = `${messages?.[sourceType]?.[handNote] ?? ''}\n${
    messages?.[sourceType]?.[typeNote] ?? ''
  }`;

  const onClickOption = {
    replace: (node) => {
      if (node?.type === 'tag' && node?.name === 'a') {
        const id = { ...node.attribs }.id;
        node.attribs = {
          onClick: () => displayDoc(id),
        };
      }
    },
  };

  const result = {
    title,
    transcription: (
      <Link to={`/fragments/fragment/${xmlId}/inter/${urlId}`}>
        {transcription}
      </Link>
    ),
    date,
    sourceType: type === '\n' ? `${messages?.otherType[sourceType]}` : type,
    hadLdoDLabel: messages?.[hadLdoDLabel],
    dimensionDtoList: parserHTML(getDimensionList(dimensionDtoList)),
    surfaceString: parserHTML(getSurfaceList(surfaceString, title), onClickOption),
  };
  return { ...result, searchData: JSON.stringify(result) };
}
