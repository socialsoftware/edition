import create from 'zustand';

export const readingStore = create((set, get) => ({
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
  citations: null,
  filteredCitations: null,
}));

export const state = () => readingStore.getState();
export const getExperts = () => state().experts;
export const setExperts = (experts) => readingStore.setState({ experts });
export const getRecommendation = () => state().recommendation;
export const setRecommendation = (recommendation) =>
  readingStore.setState({ recommendation });
export const resetRecommendations = () =>
  setRecommendation({ ...getRecommendation(), read: [] });
export const setRecommendationAttribute = (attribute, value) =>
  setRecommendation({ ...getRecommendation(), [attribute]: parseFloat(value) });
export const setCitations = (data) =>
  readingStore.setState({ citations: data, filteredCitations: data });
export const setFilteredCitations = (data) =>
  readingStore.setState({ filteredCitations: data });
