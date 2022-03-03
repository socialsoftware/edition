import create from 'zustand';

export const fragmentStore = create(() => ({
  fragment: {},
  virtualFragment: {},
  virtualEditions : ["LdoD-JPC-anot", "LdoD-Jogo-Class", "LdoD-Mallet", "LdoD-Twitter"],
}));

export const setFragment = (data) => fragmentStore.setState({ fragment: data });

export const setVirtualFragment = (data) =>
  fragmentStore.setState({ virtualFragment: data });

  export const getVirtualEditions = () => fragmentStore.getState().virtualEditions;