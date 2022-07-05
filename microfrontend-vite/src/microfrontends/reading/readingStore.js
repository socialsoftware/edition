import { devtools } from 'zustand/middleware';
import create from 'zustand';

export const readingStore = create(
  devtools(
    () => ({
      experts: null,
      fragment: null,
      recommendation: {
        read: [],
        heteronymWeight: 0,
        dateWeight: 0,
        textWeight: 1,
        taxonomyWeight: 0,
        currentInterpretation: null,
        prevRecomendation: null,
      },
      citations: null,
      length: null,
    }),
    { name: 'ReadingStore' }
  )
);

export const readingStateSelector = (sel) =>
  readingStore((state) => state[sel]);

export const state = () => readingStore.getState();
export const getExperts = () => state().experts;
export const setExperts = (experts) => readingStore.setState({ experts });
export const getRecommendation = () => state().recommendation;
export const getFragment = () => state().fragment;
export const setFragment = (fragment) =>
  readingStore.setState({ fragment: fragment });
export const setRecommendation = (recommendation) =>
  readingStore.setState({ recommendation });
export const resetRecommendations = () =>
  setRecommendation({ ...getRecommendation(), read: [] });
export const setRecommendationAttribute = (attribute, value) =>
  setRecommendation({ ...getRecommendation(), [attribute]: parseFloat(value) });
export const setCitations = (data) =>
  readingStore.setState({ citations: data });
export const setLength = (length) => readingStore.setState({ length });
