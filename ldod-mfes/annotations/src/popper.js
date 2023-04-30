/** @format */

const createPopper =
	(await import('@vendor/@popperjs/core_2.11.6/dist/umd/popper.min.js'))?.createPopper ??
	window.Popper.createPopper;

export { createPopper };
