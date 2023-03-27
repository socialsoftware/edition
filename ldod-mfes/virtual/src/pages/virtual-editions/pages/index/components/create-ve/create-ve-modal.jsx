/** @format */

import '@shared/modal-bs.js';
import './create-ve-form/create-ve-form.js';

export default ({ node }) => {
	return (
		<>
			<ldod-bs-modal id="virtual-ve-create-modal" dialog-class="modal-lg">
				<h4 data-virtualkey="modalTitle" slot="header-slot" style={{ margin: '0' }}>
					{node.getConstants('modalTitle')}
				</h4>
				<div slot="body-slot">
					<create-ve-form node={node}></create-ve-form>
				</div>
			</ldod-bs-modal>
		</>
	);
};
