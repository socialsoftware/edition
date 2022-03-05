export const FragmentNavData = ({
  fragment: {
    externalId,
    fragmentXmlId,
    title,
    expertEditionInterDtoMap,
    sourceInterDtoList: [sourceInter],
  },
  ldoD: { archiveEdition, sortedExpert },
  virtualEditionsDto,
}) => ({
  externalId,
  fragmentXmlId,
  title,
  sourceUrlId: sourceInter.urlId,
  sourceShortName: sourceInter.shortName,
  sourceExternalId: sourceInter.externalId,
  expertEditions: sortedExpert.map(({ acronym, editor }) => ({
    urlId: expertEditionInterDtoMap[acronym].urlId,
    number: expertEditionInterDtoMap[acronym].number,
    externalID: expertEditionInterDtoMap[acronym].externalID,
    acronym,
    editor,
  })),
  virtualEditions: [archiveEdition, ...virtualEditionsDto].map(
    ({ acronym, externalId,sortedInter4Frag: [frag] }) => ({
      acronym,
      externalId,
      urlId: frag?.urlId,
      number: frag?.number,
    })
  ),
});

export const FragmentSourceData = ({
  transcript,
  fragment: {
    title,
    sourceInterDtoList: [sourceInter],
  },
}) => ({ transcript, title, sourceInter });

export const FragmentExpertData = ({
  transcript,
  fragment: {
    title,
  },
  inters: [sourceInter]
}) => ({ transcript, title, sourceInter });
