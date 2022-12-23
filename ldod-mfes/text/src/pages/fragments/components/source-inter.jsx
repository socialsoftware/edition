let imageViewer;
const loadPswp = async () => {
  imageViewer = (await import('@src/common/image-viewer')).imageViewer;
};

const getPage = (startPage, endPage) =>
  startPage
    ? endPage !== startPage
      ? `${startPage} - ${endPage}`
      : startPage
    : '';

const getDate = (date, precision) =>
  date ? (
    precision ? (
      <>
        {date} (<i>{precision}</i>)
      </>
    ) : (
      date
    )
  ) : (
    ''
  );

const getAnnexNote = (notes) => notes.map((note) => note).join(',');

export const getSourceInter = (inter, node, rowIndex) => {
  if (Object.entries(inter).length === 0) return '-';
  const date = getDate(inter.date, inter.precision);
  const page = getPage(inter.startPage, inter.endPage);
  const annexNote = getAnnexNote(inter.annexNote ?? []);
  const sourceInter = (
    <>
      {inter.identification && (
        <>
          <strong data-key="identification">
            {node.getConstants('identification')}
          </strong>
          {inter.identification}
          <br />
        </>
      )}
      {inter.title && (
        <>
          <strong data-key="colTitle">{node.getConstants('colTitle')}</strong>
          {inter.title}
          <br />
        </>
      )}
      <strong data-key="heteronym">{node.getConstants('heteronym')}</strong>
      {inter.heteronym ?? (
        <span data-key="notAssigned">{node.getConstants('notAssigned')}</span>
      )}
      <br />
      {inter.form && (
        <>
          <strong data-key="form">{node.getConstants('form')}</strong>
          <span data-key={inter.form.toLowerCase()}>
            {node.getConstants(inter.form.toLowerCase())}
          </span>
          {inter.dimensionList.length > 0 ? (
            <small>
              {' ('.concat(
                inter.dimensionList.map((dim) => dim).join(', '),
                ')'
              )}
            </small>
          ) : (
            ''
          )}
          <br />
        </>
      )}
      {inter.material && (
        <>
          <strong data-key="material">{node.getConstants('material')}</strong>
          <span data-key={inter.material.toLowerCase()}>
            {node.getConstants(inter.material.toLowerCase())}
          </span>
          <br />
        </>
      )}
      {inter.columns && (
        <>
          <strong data-key="columns">{node.getConstants('columns')}</strong>
          {inter.columns}
          <br />
        </>
      )}
      <>
        <strong data-key="ldodLabel">{node.getConstants('ldodLabel')}</strong>
        <span
          data-key="hadLdoDLabel"
          data-args={inter.hadLdoDLabel ? 'true' : 'false'}>
          {node.getConstants('hadLdoDLabel', inter.hadLdoDLabel)}
        </span>
        <br />
      </>
      {inter.handNoteMediumList?.map(({ medium, note }) => {
        return (
          <>
            <strong data-key="handNote">{node.getConstants('handNote')}</strong>{' '}
            {medium && <em>({medium})</em>}
            <strong> :</strong> {note}
            <br />
          </>
        );
      })}
      {inter.typeNoteList?.map(({ medium, note }) => {
        return (
          <>
            <strong data-key="typeNote">{node.getConstants('typeNote')}</strong>{' '}
            {medium && <em>({medium})</em>}
            <strong> :</strong> {note}
            <br />
          </>
        );
      })}
      {inter.journal && (
        <>
          <strong data-key="journal">{node.getConstants('journal')}</strong>
          {inter.journal}
          <br />
        </>
      )}
      {inter.number && (
        <>
          <strong data-key="number">{node.getConstants('number')}</strong>
          {inter.number}
          <br />
        </>
      )}
      {page && (
        <>
          <strong data-key="page">{node.getConstants('page')}</strong>
          {page}
          <br />
        </>
      )}
      {inter.pubPlace && (
        <>
          <strong data-key="pubPlace">{node.getConstants('pubPlace')}</strong>
          {inter.pubPlace}
          <br />
        </>
      )}
      {date && (
        <>
          <strong data-key="date">{node.getConstants('date')}</strong>
          {date}
          <br />
        </>
      )}
      {inter.notes && (
        <>
          <strong data-key="notes">{node.getConstants('notes')}</strong>
          {inter.notes}
          <br />
        </>
      )}
      {annexNote && (
        <>
          <strong data-key="note">{node.getConstants('note')}</strong>
          {annexNote}
          <br />
        </>
      )}
      {inter.shortName && inter.surfaceDetailsList?.length && (
        <>
          <strong data-key="facs">{node.getConstants('facs')}</strong>
          <span
            id={`fact-gallery-${rowIndex}`}
            class="pswp-gallery"
            onPointerEnter={loadPswp}>
            {inter.surfaceDetailsList?.map(({ graphic }, index) => {
              return (
                <>
                  {index && <span>, </span>}
                  <a
                    data-pswp-src={`https://ldod.uc.pt/facs/${graphic}`}
                    onClick={() => imageViewer(`span#fact-gallery-${rowIndex}`)}
                    data-pswp-width="1500"
                    data-pswp-height="1250">
                    {inter.shortName}.{++index}
                  </a>
                </>
              );
            })}
          </span>
        </>
      )}
    </>
  );

  return sourceInter;
};
