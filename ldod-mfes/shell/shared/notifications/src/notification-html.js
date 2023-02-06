/** @format */

export default () => theme =>
	/*html*/ `
        <div class="toast-container">
        <div
            class="toast fade text-bg-${theme}"
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
        >
            <div class="d-flex">
                <div class="toast-body">
                <slot name="toast-body"></slot>
                </div>
                <button
                    type="button"
                    class="btn-close btn-close-white btn-close-margin"
                ></button>
            </div>
        </div>
        </div>
`;
