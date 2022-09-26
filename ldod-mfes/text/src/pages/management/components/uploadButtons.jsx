export default ({ uploadSingle, uploadMultiple, uploadCorpus }) => {
  //onUpload
  const HOST = import.meta.env.VITE_HOST;
  return (
    <div class="export-row">
      <ldod-upload
        width="600px"
        data-buttonkey="uploadCorpus"
        title={uploadCorpus}
        data-url={`${HOST}/text/admin/upload-corpus`}></ldod-upload>
      <ldod-upload
        width="600px"
        data-buttonkey="uploadSingle"
        title={uploadSingle}
        data-url={`${HOST}/text/admin/upload-fragment`}></ldod-upload>
      <ldod-upload
        width="600px"
        multiple
        data-buttonkey="uploadMultiple"
        title={uploadMultiple}
        data-url={`${HOST}/text/admin/upload-fragments`}></ldod-upload>
    </div>
  );
};
