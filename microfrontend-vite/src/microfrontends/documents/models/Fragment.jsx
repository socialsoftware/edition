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
