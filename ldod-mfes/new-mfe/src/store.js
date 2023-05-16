/** @format */
import { createStore } from 'zustand_4.3.8/vanilla';

export const store = createStore(() => ({
	counter: 0,
}));

export const { getState, setState, subscribe } = store;
