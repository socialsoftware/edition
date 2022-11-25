import { isMFEAvailable } from './utils';

const onError = (e) => console.error(e);

const loader = async (mfeName) =>
  isMFEAvailable(mfeName) &&
  (await import(mfeName).catch(onError))[`${mfeName}References`];

export const reading = await loader('reading');
export const text = await loader('text');
export const search = await loader('search');
export const virtual = await loader('virtual');
export const about = await loader('about');
export const social = await loader('social');
export const user = await loader('user');
