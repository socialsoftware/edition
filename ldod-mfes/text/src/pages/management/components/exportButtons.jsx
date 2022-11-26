import('shared/buttons.js').then(({ exportButton }) => exportButton());
const PATH = '/text/admin';

export default ({ exportAll, exportSelected, exportRandom, node }) => {
  return (
    <div class="export-row">
      <ldod-export
        width="350px"
        data-buttonkey="exportAll"
        title={exportAll}
        file-prefix="exported-file"
        data-url={`${PATH}/export-all`}
        method="GET"></ldod-export>
      <ldod-export
        id="exportSelected"
        width="350px"
        data-buttonkey="exportSelected"
        title={exportSelected}
        file-prefix="exported-file"
        data-url={`${PATH}/export-fragments`}
        method="POST"
        body={[]}></ldod-export>
      <ldod-export
        width="350px"
        data-buttonkey="exportRandom"
        title={exportRandom}
        file-prefix="exported-file"
        data-url={`${PATH}/export-random`}
        method="GET"></ldod-export>
    </div>
  );
};
