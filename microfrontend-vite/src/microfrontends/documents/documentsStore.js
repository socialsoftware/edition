import create from 'zustand';

export const documentsStore = create((set) => ({
  sourceList: null,
  setSourceList: (data) => set({ sourceList: data }),
}));

