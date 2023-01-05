import * as mod from '@vendor/@popperjs/core_2.11.6/dist/umd/popper.min.js';
let createPopper;

if (Popper) createPopper = Popper.createPopper;
else createPopper = mod?.createPopper;
export { createPopper };
