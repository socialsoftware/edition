import {
  getCheckboxesState,
  setAuthorialsInter,
  setVirtualsInter,
} from './fragmentStore';

const fragmentType = {
  AUTHORIAL: 'AUTHORIAL',
  EDITORIAL: 'EDITORIAL',
  VIRTUAL: 'VIRTUAL',
};
export const isVirtual = ({ type }) => type === fragmentType.VIRTUAL;
export const isAuthorial = ({ type }) => type === fragmentType.AUTHORIAL;
export const isEditorial = ({ type }) => type === fragmentType.EDITORIAL;


const fragmentSourceType = {
  MANUSCRIPT: 'MANUSCRIPT',
  PRINTED: 'PRINTED',
  PUBLICATION: 'PUBLICATION',
};

export const isManuscript = ({ sourceType, type }) =>
  isAuthorial({ type }) && sourceType === fragmentSourceType.MANUSCRIPT;
export const isPublication = ({ sourceType, type }) =>
  isAuthorial({ type }) && sourceType === fragmentSourceType.PRINTED;

const getSourceType = (fragment) =>
  isManuscript(fragment)
    ? fragmentSourceType.MANUSCRIPT
    : isPublication(fragment)
    ? fragmentSourceType.PUBLICATION
    : undefined;

const transcriptType = {
  SINGLE: 'SINGLE',
  SIDE: 'SIDE',
  LINE: 'LINE',
};

const getTranscriptionType = ({
  writerLineByLine,
  transcript,
  setTranscriptionSideBySide,
}) =>
  writerLineByLine
    ? transcriptType.LINE
    : setTranscriptionSideBySide
    ? transcriptType.SIDE
    : transcript && transcriptType.SINGLE;

export const isSingle = (fragment) =>
  getTranscriptionType(fragment) === transcriptType.SINGLE;
export const isSideBySide = (fragment) =>
  (getTranscriptionType(fragment) || fragment.transcriptType) ===
  transcriptType.SIDE;
export const isLineByLine = (fragment) =>
  getTranscriptionType(fragment) === transcriptType.LINE;

export const FragmentNavData = ({
  fragment: { externalId, fragmentXmlId, title, sourceInterDtoList },
  ldoD: { archiveEdition, sortedExpert },
  virtualEditionsDto,
}) => ({
  externalId,
  fragmentXmlId,
  title,
  sources: sourceInterDtoList.map(({ urlId, shortName, externalId }) => ({
    urlId,
    shortName,
    externalId,
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
      inter: sortedInter4Frag.map(({ externalId, urlId, number }) => ({
        externalId,
        urlId,
        number,
      })),
    })
  ),
});

function FragmentAuthorialSingle({
  fragment,
  transcript,
  nextPb,
  prevPb,
  surface,
  nextSurface,
  prevSurface,
  inters,
}) {
  const inter = inters[0];
  const type = inter.type;

  return {
    type,
    transcriptType: transcriptType.SINGLE,
    sourceType: getSourceType(inter),
    title: inter.title,
    transcript,
    fontFamily: 'courier',
    fontSize: '',
    metaData: isAuthorial(inter)
      ? fragment.sourceInterDtoList?.find(
          ({ externalId }) => externalId === inter.externalId
        )
      : inter,
    surface: {
      prevPb,
      nextPb,
      graphic: surface?.graphic,
      prevGraphic: prevSurface?.graphic,
      nextGraphic: nextSurface?.graphic,
    },
  };
}

function FragmentEditorialSingle({ fragment, transcript, inters }) {
  const inter = inters[0];
  const type = inter.type;

  return {
    type,
    transcriptType: transcriptType.SINGLE,
    sourceType: null,
    title: inter.title,
    transcript,
    fontFamily: 'georgia',
    fontSize: 'medium',
    metaData: isAuthorial(inter)
      ? fragment.sourceInterDtoList?.find(
          ({ externalId }) => externalId === inter.externalId
        )
      : inter,
  };
}

function FragmentVirtualSingle({ transcript, inters }) {
  const inter = inters[0] ?? {};
  const type = inter?.type;

  return {
    type,
    transcriptType: transcriptType.SINGLE,
    sourceType: null,
    title: inter?.title,
    transcript,
    inters,
    fontFamily: 'monospace',
    fontSize: '',
    metaData: null,
    taxonomies: inters.length <= 1 && inter.categoryUserDtoList,
    taxonomiesComp:
      inters.length > 1 && inters.map((inter) => inter?.categoryUserDtoList),
    editionTitle: inter.editionTitle,
    usesList: inter.usedList.map(({ shortName }) => shortName),
    usesReference: `${inter.usesReference}${
      inter.usesSecondReference && `(${inter.usesSecondReference})`
    }`,
  };
}

function FragmentInterSide(data) {
  return {
    transcriptType: transcriptType.SIDE,
    variations: data.variations,
    inters: data.inters?.map((inter, index) => ({
      type: inter.type,
      setTranscriptionSideBySide: data.setTranscriptionSideBySide[index],
      title: inter.title,
      metaData: isAuthorial(inter)
        ? data.fragment.sourceInterDtoList?.find(
            ({ externalId }) => externalId === inter.externalId
          )
        : inter,
      fontFamily: getCheckboxesState().align ? 'monospace' : 'gerogia',
    })),
  };
}

function FragmentInterLine(data) {
  return {
    transcriptType: transcriptType.LINE,
    variations: data.variations,
    writerLineByLine: data.writerLineByLine,
    title: data.fragment?.title,
    fontFamily: getCheckboxesState().line ? 'monospace' : 'gerogia',
  };
}

export function extractData(data, state) {
  const inters = data.inters;
  const inter = inters?.[0];
  
  if (isVirtual(inter ?? {})) {
    setVirtualsInter(inters?.map((inter) => inter?.externalId) ?? [])
    return FragmentVirtualSingle(data);
  }

  setAuthorialsInter(inters?.map((inter) => inter?.externalId) ?? []);

  if (isSingle(data)) {
    if (isAuthorial(inter)) return FragmentAuthorialSingle(data);
    if (isEditorial(inter)) return FragmentEditorialSingle(data);
  }
  if (isSideBySide(data)) return FragmentInterSide(data);

  if (isLineByLine(data)) return FragmentInterLine(data);

}
