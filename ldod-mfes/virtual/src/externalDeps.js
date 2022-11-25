export const text =
  window.mfes?.includes('text') &&
  (await import('text').catch((e) => console.error(e))).textReferences;
