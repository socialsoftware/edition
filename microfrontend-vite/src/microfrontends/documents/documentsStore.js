import create from 'zustand';

export const documentsStore = create(() => ({
  sourceList: null,
  filteredSourceList: null,
  encodedFragments: null,
  filteredEncodedFragments: null,
  showModal: false,
  docPath: null,
}));

export const setSourceList = (data) =>
  documentsStore.setState({ sourceList: data });
export const setFilteredSourceList = (data) =>
  documentsStore.setState({ filteredSourceList: data });

export const toggleShow = () =>
  documentsStore.setState((state) => ({ showModal: !state.showModal }));
export const setDocPath = (path) => documentsStore.setState({ docPath: path });

export const setEncodedFragments = (data) =>
  documentsStore.setState({ encodedFragments: data });
export const setFilteredEncodedFragments = (data) =>
  documentsStore.setState({ filteredEncodedFragments: data });
