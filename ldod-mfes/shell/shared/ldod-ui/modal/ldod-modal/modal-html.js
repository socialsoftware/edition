export default (dialogClass, noFooter) => {
	return /*html*/ `
   <div
      class="modal"
      id="ldod-modal"
      tabindex="-1"
      role="dialog"
      aria-hidden="true"
      aria-labelledby="ldod.modal-label"
    >
      <div class=${`"modal-dialog ${dialogClass}"`}>
        <div class="modal-content">
          <div id="modal-close-button">
            <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close">
          </button>
          </div>
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">
              <slot name="header-slot"></slot>
            </h5>
          </div>
          <div class="modal-body">
            <slot name="body-slot"></slot>
          </div>
          ${getFooter(noFooter)}
        </div>
      </div>
    </div>
    `;
};

function getFooter(noFooter) {
	if (noFooter) return '';
	return /*html*/ `
  <div class="modal-footer">
    <slot name="footer-slot"></slot>
  </div>`;
}
