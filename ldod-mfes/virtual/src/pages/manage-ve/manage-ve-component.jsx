/** @format */

import { LdodManageVE } from './ldod-manage-ve';
import VeManageTable from './ve-manage-table';

/**
 * @param {LdodManageVE} root
 */
export default root => {
	return (
		<>
			<h3 class="text-center" data-virtual-key="manageVE">
				{root.getConstants('manageVE')}
				<span>{` (${root.virtualEditions.length})`}</span>
			</h3>
			<div class="flex-row">
				<div class="flex-column">
					<ldod-upload
						id="corpus"
						width="600px"
						data-virtual-button-key="uploadVeCorpus"
						title={root.getConstants('uploadVeCorpus')}
						data-url={`/virtual/admin/upload-virtual-corpus`}></ldod-upload>
					<ldod-upload
						id="fragments"
						width="600px"
						data-virtual-button-key="uploadVeFragments"
						multiple
						title={root.getConstants('uploadVeFragments')}
						data-url={`/virtual/admin/upload-virtual-fragments`}></ldod-upload>
				</div>
				<div class="flex-column">
					<ldod-export
						width="350px"
						file-type="zip"
						data-virtual-button-key="exportVe"
						title={root.getConstants('exportVe')}
						file-prefix="VirtualEditionsFragments"
						data-url={`/virtual/admin/export-virtual-editions`}
						method="GET"></ldod-export>
				</div>
			</div>
			<div>
				<VeManageTable root={root} />
			</div>
		</>
	);
};
