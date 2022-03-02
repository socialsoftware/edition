import { Link } from 'react-router-dom';

const getDimensionList = (dimensions) =>
  `${
    dimensions
      ?.reduce(
        (prev, cur) => [...prev, ` ${cur?.height}cm X ${cur.width}cm`],
        []
      )
      .join(', \n') ?? ''
  }`;

const getSurfaceList = (surfaces, title, displayDoc) => (
  <>
    {surfaces?.map((item, index) => (
      <div key={index}>
        <a onClick={() => displayDoc(item)}>{`(${index + 1}) ${title}`}</a>
        <br />
      </div>
    ))}
  </>
);

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
  const result = {
    title,
    transcription: (
      <Link to={`/fragments/fragment${xmlId}/inter/${urlId}`}>
        {transcription}
      </Link>
    ),
    date,
    sourceType: type === '\n' ? `${messages?.otherType[sourceType]}` : type,
    hadLdoDLabel: messages?.[hadLdoDLabel],
    dimensionDtoList: getDimensionList(dimensionDtoList),
    surfaceString: getSurfaceList(surfaceString, title, displayDoc),
  };
  return { ...result, searchData: JSON.stringify(result) };
}
