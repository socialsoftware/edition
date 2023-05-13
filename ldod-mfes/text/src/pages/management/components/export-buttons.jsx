/** @format */

import('@ui/buttons.js').then(({ exportButton }) => exportButton());
const PATH = '/text/admin';

export default ({ exportAll, exportSelected, exportRandom }) => {
	return (
		<div class="export-row">
			<ldod-export
				width="350px"
				data-button-key="exportAll"
				title={exportAll}
				file-prefix="exported-file"
				data-url={`${PATH}/export-all`}
				method="GET"></ldod-export>
			<ldod-export
				id="exportSelected"
				width="350px"
				data-button-key="exportSelected"
				title={exportSelected}
				file-prefix="exported-file"
				data-url={`${PATH}/export-fragments`}
				method="POST"
				body={[]}></ldod-export>
			<ldod-export
				width="350px"
				data-button-key="exportRandom"
				title={exportRandom}
				file-prefix="exported-file"
				data-url={`${PATH}/export-random`}
				method="GET"></ldod-export>
		</div>
	);
};
