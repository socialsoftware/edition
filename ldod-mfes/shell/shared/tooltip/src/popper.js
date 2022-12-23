export let createPopper;

if (import.meta.env.DEV) {
  createPopper = (await import('@popperjs/core_2.11.6')).createPopper;
} else {
  await import('vendor/@popperjs/core_2.11.6/dist/umd/popper.min.js');
  createPopper = Popper.createPopper;
}
