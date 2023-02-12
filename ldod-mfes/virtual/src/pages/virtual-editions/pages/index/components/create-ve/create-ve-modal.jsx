/** @format */

import '@shared/modal-bs.js';
import CreateVEForm from './create-ve-form';

export default ({ node }) => {
	return (
		<>
			<ldod-bs-modal id="virtual-ve-create-modal" dialog-class="modal-lg">
				<h4 data-virtualkey="modalTitle" slot="header-slot" style={{ margin: '0' }}>
					{node.getConstants('modalTitle')}
				</h4>
				<div slot="body-slot">
					<CreateVEForm node={node} />
				</div>
			</ldod-bs-modal>
		</>
	);
};
