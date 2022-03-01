import create from 'zustand';

export const documentsStore = create((set) => ({
  sourceList: null,
  fragmentsList: null,
  setSourceList: (data) => set({ sourceList: data }),
  setFragmentsList: (data) => set({ fragmentsList: data }),
}));
