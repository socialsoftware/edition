export default ({ exportAll, exportSelected, exportRandom, node }) => {
  const HOST = import.meta.env.VITE_HOST;

  return (
    <div class="export-row">
      <ldod-export
        width="350px"
        data-buttonkey="exportAll"
        title={exportAll}
        file-prefix="exported-file"
        data-url={`${HOST}/text/admin/export-all`}
        method="GET"></ldod-export>
      <ldod-export
        id="exportSelected"
        width="350px"
        data-buttonkey="exportSelected"
        title={exportSelected}
        file-prefix="exported-file"
        data-url={`${HOST}/text/admin/export-fragments`}
        method="POST"
        body={[]}></ldod-export>
      <ldod-export
        width="350px"
        data-buttonkey="exportRandom"
        title={exportRandom}
        file-prefix="exported-file"
        data-url={`${HOST}/text/admin/export-random`}
        method="GET"></ldod-export>
    </div>
  );
};
