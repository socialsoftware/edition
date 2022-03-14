import create from 'zustand';

export const searchStore = create(() => ({
  searchResult: null,
}));

export const searchStateSelector = (sel) => searchStore(state => state[sel]);
export const setSearchResult = (searchResult) => searchStore.setState({searchResult});