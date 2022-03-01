import create from 'zustand';

export const documentsStore = create(() => ({
  source: null,
  sourceList: null,
  fragments: null,
  fragmentsList: null,
  dataFiltered: null,
}));

export const setSource = (data, messages) => {
  documentsStore.setState({ source: data });
  setSourceList(data, messages);
};

export const setSourceList = (data, messages) => {
  documentsStore.setState({ sourceList: formatSourceData(data, messages) });
  setDataFiltered(documentsStore.getState().sourceList);
};

export const setFragments = (data, messages) => {
  documentsStore.setState({ fragments: data });
  setFragmentsList(data, messages);
};

export const setFragmentsList = (data, messages) => {
  documentsStore.setState({
    fragmentsList: formatFragmentData(data, messages),
  });
  setDataFiltered(documentsStore.getState().fragmentsList); 
};

export const setDataFiltered = (data) =>
  documentsStore.setState({ dataFiltered: data });

const getDimensionList = (dimensions) =>
  `${
    dimensions
      ?.reduce(
        (prev, cur) => [...prev, ` ${cur?.height}cm X ${cur.width}cm`],
        []
      )
      .join(',<br />') ?? ''
  }`;

const getSurfaceList = (surfaces, title) =>
  `${
    surfaces
      ?.reduce(
        (prev, cur, index) => [
          ...prev,
          `<a id="${cur}"> (${index + 1}) ${title}</a>`,
        ],
        []
      )
      .join('<br />') ?? ''
  }`;

const formatSourceData = (data, messages) =>
  data?.map((entry) => {
    const {
      transcription,
      dimensionDtoList,
      surfaceString,
      xmlId,
      urlId,
      typeNoteDto,
      handNoteDto,
      sourceType,
    } = entry;
    const handNote = handNoteDto?.desc;
    const typeNote = typeNoteDto?.desc;
    const type = `${messages?.[sourceType]?.[handNote] ?? ''}\n${
      messages?.[sourceType]?.[typeNote] ?? ''
    }`;
    return {
      title: entry.title,
      transcription: `<a id="${xmlId}/inter/${urlId}">${transcription}</a>`,
      date: entry.date,
      hadLdoDLabel: messages?.[entry.hadLdoDLabel],
      sourceType: type === '\n' ? `${messages?.otherType[sourceType]}` : type,
      dimensionDtoList: getDimensionList(dimensionDtoList),
      surfaceString: getSurfaceList(surfaceString, entry.title),
    };
  });

const getExpertData = (
  {
    title,
    heteronym,
    volume,
    number,
    startPage,
    notes,
    heteronymNull,
    annexNoteDtoList,
  },
  messages
) => {
  const keyValues = {
    title: title,
    heteronym: heteronymNull ? messages?.notAssigned : heteronym,
    volume: volume,
    number: number,
    startPage: startPage,
    notes: notes,
  };

  const expertData = Object.entries(keyValues)
    .reduce(
      (prev, [key, val]) => [
        ...prev,
        val && `<strong>${messages?.[key]}: </strong>${val}<br />`,
      ],
      []
    )
    .join('');

  const annexNote = annexNoteDtoList
    ?.reduce(
      (prev, curr) => [
        ...prev,
        `<strong>${messages?.notes}: </strong>${curr.presentationText}`,
      ],
      []
    )
    .join('<br />');
  return `${expertData ?? ''}${annexNote ?? ''}`;
};

const getSourceData = (
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
    sourceType,
    title,
    journal,
    issue,
    pubPlace,
    startPage,
    endPage,
    dimensionDtoList,
  },
  messages
) => {
  const identifiers = surfaceString
    ?.reduce((prev, curr) => [...prev, `${curr.split('.jpg')[0]}`], [])
    .join(', <br />');

  const keyValues = {
    altIdentifier:
      sourceType === 'MANUSCRIPT' && identifiers ? `: ${identifiers}` : '',
    title: sourceType === 'PRINTED' && title ? `: ${title}` : '',
    heteronym: heteronymNull ? `: ${messages?.notAssigned}` : `: ${heteronym}`,
    journal: journal && `: ${journal}`,
    issue: issue && `: ${issue}`,
    startPage:
      startPage !== 0 || endPage !== 0
        ? `: ${startPage}${endPage ? ` - ${endPage}` : ''} `
        : '',
    pubPlace: pubPlace && `: ${pubPlace}`,
    form:
      form &&
      `: ${messages?.[form]} ${
        dimensionDtoList
          ? `<small>(${getDimensionList(dimensionDtoList)})</small>`
          : ''
      }`,
    material: material && `: ${messages?.[material]}`,
    columns: columns && `: ${columns}`,
    hadLdoDLabel: messages?.[hadLdoDLabel] && `: ${messages?.[hadLdoDLabel]}`,
    handNoteDto:
      handNoteDto &&
      `(<em>${handNoteDto?.desc}</em>): ${handNoteDto?.note ?? ''}`,
    typeNoteDto:
      typeNoteDto &&
      ` (<em>${typeNoteDto?.desc}</em>): ${typeNoteDto?.note ?? ''}`,
    date: date && `: ${date} ${desc ? `(${desc})` : ''}`,
    notes: notes && `: ${notes}`,
  };

  const sourceData = Object.entries(keyValues)
    .reduce(
      (prev, [key, val]) => [
        ...prev,
        val && `<strong>${messages?.[key]}</strong>${val}<br />`,
      ],
      []
    )
    .join('');

  const surfaceData = surfaceString
    ?.reduce(
      (prev, curr) => [...prev, `<a id="${curr}">${altIdentifier}</a>`],
      []
    )
    .join(', ');
  return `${sourceData ?? ''}<strong>${
    messages?.surfaceString
  }: </strong>${surfaceData}`;
};

const formatFragmentData = (data, messages) =>
  data?.map(
    ({
      title,
      expertEditionInterDtoMap,
      sourceInterDtoList,
      fragmentXmlId,
    }) => ({
      title: `<a id="${fragmentXmlId}">${title}</a>`,
      JPC: getExpertData(expertEditionInterDtoMap.JPC ?? '', messages),
      TSC: getExpertData(expertEditionInterDtoMap.TSC ?? '', messages),
      RZ: getExpertData(expertEditionInterDtoMap.RZ ?? '', messages),
      JP: getExpertData(expertEditionInterDtoMap.JP ?? '', messages),
      sourceInterDtoList_0:
        sourceInterDtoList?.[0] &&
        getSourceData(sourceInterDtoList?.[0], messages),
      sourceInterDtoList_1:
        sourceInterDtoList?.[1] &&
        getSourceData(sourceInterDtoList?.[1], messages),
      sourceInterDtoList_2:
        sourceInterDtoList?.[2] &&
        getSourceData(sourceInterDtoList?.[2], messages),
    })
  );
