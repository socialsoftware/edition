/** @format */

export default () => theme =>
	/*html*/ `
        <div
            class="toast fade alert-${theme}"
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
        >
            <div class="d-flex">
            ${
				theme === 'success'
					? /*html*/ `<span is="ldod-span-icon" icon="check" size="18px" fill="#0f5132"></span>`
					: /*html*/ `<span is="ldod-span-icon" icon="triangle-exclamation" size="18px" fill="#842029"></span>`
			}
            <div class="toast-body">
                <slot ></slot>
                </div>
                <button
                    type="button"
                    class="btn-close btn-close-alert btn-close-margin"
                ></button>
            </div>
        </div>
`;
