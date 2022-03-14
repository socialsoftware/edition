import create from 'zustand';

export const documentsStore = create(() => ({
  sourceList: null,
  encodedFragments: null,
  sourceLength: null,
  fragmentLength: null,
  showModal: false,
  docPath: null,
}));

export const documentStateSelector = (sel) =>
  documentsStore((state) => state[sel]);

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

export const setSourceLength = (sourceLength) =>
  documentsStore.setState({ sourceLength });
export const setFragmentLength = (fragmentLength) =>
  documentsStore.setState({ fragmentLength });
