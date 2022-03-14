import create from 'zustand';

export const editionStore = create(() => ({
  data: null,
  length: null,
}));

export const getData = () => editionStore.getState().data;
export const setData = (data) =>
  editionStore.setState({ data });

export const setLength = (length) => editionStore.setState({ length });
export const editionStoreSelector = (sel) =>
  editionStore((state) => state[sel]);
