import create from 'zustand';

export const editionStore = create(() => ({
  edition: null,
  acronym: null,
  dataFiltered: null,
}));


export const getEdition = () => editionStore.getState().edition;
export const setEdition = (edition) => editionStore.setState({ edition });

export const getAcronym = () => editionStore.getState().acronym;
export const setAcronym = (acronym) => editionStore.setState({ acronym });

export const setDataFiltered = (dataFiltered) => editionStore.setState({ dataFiltered });

export const editionStoreSelector = (sel) => editionStore(state => state[sel]);
