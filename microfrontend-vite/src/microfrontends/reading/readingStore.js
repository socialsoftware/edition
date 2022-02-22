import create from 'zustand';
import { persist } from 'zustand/middleware';

export const readingStore = create(
  persist(
    () => ({
      experts: null,
      recommendation: {
        read: [],
        heteronymWeight: 0,
        dateWeight: 0,
        textWeight: 1,
        taxonomyWeight: 0,
        currentInterpretation: null,
        prevRecomendation: null,
      },
    }),
    {
      name: 'ldod-reading-storage',
    }
  )
);

export const state = () => readingStore.getState();
export const getExperts = () => state().experts;
export const setExperts = (experts) => readingStore.setState({ experts });
export const getRecommendation = () => state().recommendation;
export const setRecommendation = (recommendation) =>
  readingStore.setState({ recommendation });
