/** @format */

export const modal = () => {
	return /*html*/ `
        <div
            class="modal fade"
            id="new-ldod-modal"
            tabindex="-1">
            <div class="modal-dialog">
               <div class="modal-content">
                    <div class="modal-header">
                        <slot name="header-slot"></slot>
                        <button
                            id="modal-btn-close"
                            type="button"
                            class="btn-close">
                        </button>
                    </div>
                    <div class="modal-body">
                        <slot name="body-slot"></slot>
                    </div>
                    <div class="modal-footer">
                        <slot name="footer-slot"></slot>
                    </div>
                </div>
            </div>
        </div>
        <div id="modal-backdrop"></div>
`;
};
