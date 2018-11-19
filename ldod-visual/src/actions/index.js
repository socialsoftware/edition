import {ADD_FRAGMENT} from "../constants/action-types";
import {SET_FRAGMENT_INDEX} from "../constants/action-types";
import {SET_CURRENT_VISUALIZATION} from "../constants/action-types";
import {ADD_HISTORY_ENTRY} from "../constants/action-types";

export const addFragment = fragment => ({type: ADD_FRAGMENT, payload: fragment});

export const setFragmentIndex = fragmentIndex => ({type: SET_FRAGMENT_INDEX, payload: fragmentIndex});

export const setCurrentVisualization = currentVisualization => ({type: SET_CURRENT_VISUALIZATION, payload: currentVisualization});

export const addHistoryEntry = historyEntry => ({type: ADD_HISTORY_ENTRY, payload: historyEntry});
