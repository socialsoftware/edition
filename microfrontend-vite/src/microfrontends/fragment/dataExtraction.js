import { setAuthorialsInter, setVirtualsInter } from './fragmentStore';

export const FragmentNavData = ({
  fragment: { externalId, fragmentXmlId, title, sourceInterDtoList },
  ldoD: { archiveEdition, sortedExpert },
  virtualEditionsDto,
}) => ({
  externalId,
  fragmentXmlId,
  title,
  sources: sourceInterDtoList.map((source) => ({
    urlId: source.urlId,
    shortName: source.shortName,
    externalId: source.externalId,
  })),
  expertEditions: sortedExpert.map(({ acronym, editor, inter }) => ({
    acronym,
    editor,
    inter: inter.map(({ urlId, externalID, number }) => ({
      externalId: externalID,
      urlId,
      number,
    })),
  })),
  virtualEditions: [archiveEdition, ...virtualEditionsDto].map(
    ({ acronym, sortedInter4Frag }) => ({
      acronym,
      inter: sortedInter4Frag.map(({ urlId, number, externalId }) => ({
        externalId,
        urlId,
        number,
      })),
    })
  ),
});

export const FragmentInterData = ({
  transcript,
  writerLineByLine,
  setTranscriptionSideBySide,
  fragment: {
    title,
    sourceInterDtoList: [authorialMetaData],
  },
  inters,
  nextPb,
  prevPb,
  nextSurface,
  prevSurface,
  surface,
  variations,
}) => {
  const inter = inters?.[0]
  const isLineByLine = writerLineByLine && true;
  const isSideBySide = setTranscriptionSideBySide && true;
  const isNormal = !isLineByLine && !isSideBySide;
  const isVirtual = inter.type === 'VIRTUAL';
  const isAuthorial = inter.type === 'AUTHORIAL';

  isVirtual
    ? setVirtualsInter(inters?.map((inter) => inter?.externalId) ?? [])
    : setAuthorialsInter(inters?.map((inter) => inter?.externalId) ?? []);

  return {
    externalIds: inters?.map((inter) => inter.externalId),
    fontFamily: !isNormal || !isAuthorial ? 'georgia' : 'courier',
    type: inter.type,
    transcriptType: isLineByLine ? 'LINE' : isSideBySide ? 'SIDE' : 'NORMAL',
    editionTitle: inter.editionTitle,
    usesList: inter.usedList.map(({ shortName }) => shortName),
    usesReference: `${inter.usesReference}${
      inter.usesSecondReference && `(${inter.usesSecondReference})`
    }`,
    fontSize:
      isNormal ? isVirtual || isAuthorial ? 'medium' : '' : '',
    transcript: isLineByLine
      ? writerLineByLine
      : isSideBySide
      ? setTranscriptionSideBySide
      : transcript,
    title,
    metaData: !isVirtual && [authorialMetaData, ...inters],
    inters,
    surface: {
      prevPb,
      nextPb,
      graphic: surface?.graphic,
      prevGraphic: prevSurface?.graphic,
      nextGraphic: nextSurface?.graphic,
    },
    taxonomies: inters?.length === 1 && inter.categoryUserDtoList,
    taxonomiesComp: inters.map(inter => inter?.categoryUserDtoList),
    variations,
  };
};
