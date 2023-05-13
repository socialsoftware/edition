/** @format */

import('@ui/buttons.js').then(({ uploadButton }) => uploadButton());
const PATH = '/text/admin';

export default ({ uploadSingle, uploadMultiple, uploadCorpus }) => {
	//onUpload
	return (
		<div class="export-row">
			<ldod-upload
				width="600px"
				data-button-key="uploadCorpus"
				title={uploadCorpus}
				data-url={`${PATH}/upload-corpus`}></ldod-upload>
			<ldod-upload
				width="600px"
				data-button-key="uploadSingle"
				title={uploadSingle}
				data-url={`${PATH}/upload-fragment`}></ldod-upload>
			<ldod-upload
				width="600px"
				multiple
				data-button-key="uploadMultiple"
				title={uploadMultiple}
				data-url={`${PATH}/upload-fragments`}></ldod-upload>
		</div>
	);
};
