const onError = (e) => console.error(e);

const loader = async (mfeName) =>
  window.mfes?.includes(mfeName) &&
  (await import(mfeName).catch(onError))[`${mfeName}References`];

export const reading = await loader('reading');
export const virtual = await loader('virtual');
