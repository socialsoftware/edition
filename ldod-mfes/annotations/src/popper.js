let createPopper;

if (import.meta.env.DEV) {
  createPopper = (await import('@popperjs/core_2.11.6/dist/esm/popper.js'))
    .createPopper;
} else {
  await import('vendor/@popperjs/core_2.11.6/dist/umd/popper.min.js');
  createPopper = Popper.createPopper;
}

export { createPopper };
