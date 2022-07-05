import { Link } from 'react-router-dom';
import parseHTML, { domToReact } from 'html-react-parser';

const getSurfaceList = (surfaces, altIdentifier, displayDoc) => (
  <>
    {surfaces?.map((item, index) => (
      <a key={index} onClick={() => displayDoc(item)}>
        {altIdentifier}
      </a>
    ))}
  </>
);

const isNotAssigned = (heteronym) => heteronym === "não atribuído"

const getDimensionList = (dimensions) =>
  `${
    dimensions
      ?.reduce(
        (prev, cur) => [...prev, `${cur?.height}cm X ${cur?.width}cm`],
        []
      )
      .join(', ') ?? ''
  }`;

export const getSourceData = (
  {
    altIdentifier,
    heteronym,
    heteronymNull,
    form,
    material,
    columns,
    hadLdoDLabel,
    typeNoteDto,
    handNoteDto,
    surfaceString,
    notes,
    date,
    desc,
    title,
    journal,
    issue,
    pubPlace,
    startPage,
    sourceType,
    endPage,
    dimensionDtoList,
  },
  messages,
  displayDoc
) => {
  const identifiers = surfaceString
    ?.reduce((prev, curr) => [...prev, `${curr.split('.jpg')[0]}`], [])
    .join(',\n');

  const keyValues = {
    altIdentifier:
      sourceType === 'MANUSCRIPT' && identifiers,
    //
    //title: sourceType === 'PRINTED' && title ? `: ${title}` : '',
    title,
    heteronym: heteronymNull ? messages?.notAssigned : heteronym,
    journal,
    issue,
    startPage:
      (startPage || startPage !== 0) &&
      `${startPage}${endPage && endPage !== startPage ? ` - ${endPage}` : ''}`,
    pubPlace: pubPlace,
    form: sourceType === 'MANUSCRIPT' && (
      <>
        {messages?.[form]}
        <small>{` (${getDimensionList(dimensionDtoList)})`}</small>
      </>
    ),
    material: messages?.[material],
    columns,
    hadLdoDLabel: messages?.[hadLdoDLabel],
    handNoteDto: handNoteDto?.note,
    typeNoteDto: typeNoteDto?.note,
    date: date && `${date} ${desc ? `(${desc})` : ''}`,
    notes: notes,
    surfaceString: (
      <>
        {getSurfaceList(surfaceString, altIdentifier, displayDoc)}
        <br />
      </>
    ),
  };

  const sourceData = Object.entries(keyValues).map(([key, val], index) => {
    if (val) {
      const getKey =
        key === 'handNoteDto'
          ? parseHTML(messages.handNoteDto(handNoteDto.desc))
          : key === 'typeNoteDto'
          ? parseHTML(messages.typeNoteDto(typeNoteDto.desc))
          : parseHTML(`<strong>${messages?.[key]}: </strong>`);
      return (
        <div key={index}>
          {getKey}
          {val}
        </div>
      );
    }
  });

  return sourceData;
};

export const getExpertData = (
  {
    title,
    heteronym,
    volume,
    number,
    startPage,
    endPage,
    date,
    notes,
    heteronymNull,
    annexNoteDtoList,
  },
  messages
) => {
  const keyValues = {
    title: title,
    heteronym: isNotAssigned(heteronym) || heteronymNull ? messages?.notAssigned : heteronym,
    volume: volume,
    number: number || number === 0 ? `${number}` : '',
    startPage:
      endPage && endPage !== startPage
        ? `${startPage} - ${endPage}`
        : startPage,
    date: date,
    notes: notes,
  };

  const options = {
    replace: ({ type, name, attribs, children }) =>
      type === 'tag' &&
      name === 'a' && (
        <Link to={attribs.href ?? '/'}>{domToReact(children)}</Link>
      ),
  };

  const annexNote = annexNoteDtoList?.map(({ presentationText }, index) => (
    <div key={`anex-${index}`}>
      <strong>{messages?.notes}: </strong>
      {parseHTML(presentationText, options)}
      <br />
    </div>
  ));

  const expertDate = Object.entries(keyValues).map(([key, val], index) => {
    if (val)
      return (
        <div key={index}>
          <strong>{messages?.[key]}: </strong>
          {typeof val === 'string' ? parseHTML(val) : val}
          <br />
        </div>
      );
  });

  return expertDate.concat(annexNote);
};

export function EncodedFragment(
  { title, expertEditionInterDtoMap, sourceInterDtoList, fragmentXmlId },
  messages,
  displayDoc
) {
  const result = {
    title: <Link to={`/fragments/fragment/${fragmentXmlId}`}>{title}</Link>,
    JPC: getExpertData(expertEditionInterDtoMap.JPC ?? '', messages),
    TSC: getExpertData(expertEditionInterDtoMap.TSC ?? '', messages),
    RZ: getExpertData(expertEditionInterDtoMap.RZ ?? '', messages),
    JP: getExpertData(expertEditionInterDtoMap.JP ?? '', messages),
    sourceInterDtoList_0:
      sourceInterDtoList?.[0] &&
      getSourceData(sourceInterDtoList?.[0] ?? {}, messages, displayDoc),
    sourceInterDtoList_1:
      sourceInterDtoList?.[1] &&
      getSourceData(sourceInterDtoList?.[1] ?? {}, messages, displayDoc),
    sourceInterDtoList_2:
      sourceInterDtoList?.[2] &&
      getSourceData(sourceInterDtoList?.[2] ?? {}, messages, displayDoc),
  };
  return { ...result, searchData: JSON.stringify(result) };
}
