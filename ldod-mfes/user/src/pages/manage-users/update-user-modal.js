/** @format */

export default root => {
	return /*html*/ `
		<ldod-bs-modal dialog-class="modal-xl" id="user-update-user-modal">
			<h4 data-users-key="editUserHeader" slot="header-slot">
				${root.getConstant('editUserHeader')}
			</h4>
			<div slot="body-slot"></div>
		</ldod-bs-modal>`;
};
