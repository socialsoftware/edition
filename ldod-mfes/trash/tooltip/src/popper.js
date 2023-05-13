/** @format */

import * as mod from '@vendor/@popperjs/core_2.11.6/dist/umd/popper.min.js';
let createPopper;

if (globalThis?.Popper) createPopper = Popper.createPopper;
else createPopper = mod?.createPopper;
export { createPopper };
