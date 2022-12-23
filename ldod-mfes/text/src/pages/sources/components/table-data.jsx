import textReferences from '@src/references';

let imageViewer;
const loadPswp = async () => {
  imageViewer = (await import('@src/common/image-viewer')).imageViewer;
};

const isManuscript = (type) => type === 'manuscript';

export const getTableData = (node) => {
  return node.interSources?.map((source, rowIndex) => {
    return {
      externalId: source.externalId,
      data: () => ({
        docs: source.name,
        transcription: source.sourceIntersList?.map(({ xmlId, urlId }) => (
          <a is="nav-to" to={textReferences.fragmentInter(xmlId, urlId)}>
            {source.title}
          </a>
        )),
        date: source.date,
        type: (
          <>
            {source.sourceType === 'printed' && (
              <span data-key={`${source.sourceType}`}>
                {node.getConstants(source.sourceType)}
              </span>
            )}
            {source.handNoteList?.map(({ medium }) => {
              return (
                <>
                  <span data-key="manuscript">
                    {node.getConstants('manuscript')}
                  </span>
                  ({medium?.toUpperCase()})
                </>
              );
            })}
            {source.typeNoteList?.map(({ medium }) => {
              return (
                <>
                  <span data-key="typescript">
                    {node.getConstants('typescript')}
                  </span>
                  ({medium.toUpperCase()})
                </>
              );
            })}
          </>
        ),
        ldodLabel: isManuscript(source.sourceType) && (
          <span
            data-key="hadLdoDLabel"
            data-args={source.hadLdoDLabel ? 'true' : 'false'}>
            {node.getConstants('hadLdoDLabel', source.hadLdoDLabel)}
          </span>
        ),
        dimensions:
          isManuscript(source.sourceType) && source.dimensionList?.length
            ? source.dimensionList?.map((dim) => {
                return (
                  <>
                    {dim}
                    <br />
                  </>
                );
              })
            : '',
        fac: source.surfaceList?.length ? (
          <div
            id={`fact-gallery-${rowIndex}`}
            class="pswp-gallery"
            onPointerEnter={loadPswp}>
            {source.surfaceList?.map((surface, index) => {
              return (
                <>
                  <a
                    data-pswp-src={`https://ldod.uc.pt/facs/${surface}`}
                    onClick={() =>
                      imageViewer?.(`div#fact-gallery-${rowIndex}`)
                    }
                    data-pswp-width="1500"
                    data-pswp-height="1250">
                    ({++index}) {source.name}
                  </a>
                  <br />
                </>
              );
            })}
          </div>
        ) : (
          ''
        ),
      }),
      search: JSON.stringify(source),
    };
  });
};
