import {ADD_FRAGMENT} from "../constants/action-types";
import {SET_FRAGMENT_INDEX} from "../constants/action-types";
import {SET_CURRENT_VISUALIZATION} from "../constants/action-types";
import {ADD_HISTORY_ENTRY} from "../constants/action-types";
import {SET_ALL_FRAGMENTS_LOADED} from "../constants/action-types";
import {SET_OUT_OF_LANDING_PAGE} from "../constants/action-types";
import {SET_HISTORY_ENTRY_COUNTER} from "../constants/action-types";
import {SET_RECOMMENDATION_ARRAY} from "../constants/action-types";
import {SET_RECOMMENDATION_INDEX} from "../constants/action-types";
import {SET_FRAGMENTS_HASHMAP} from "../constants/action-types";
import {SET_CURRENT_FRAGMENT_MODE} from "../constants/action-types";

const initialState = {
  fragments: [],
  fragmentIndex: 0,
  currentVisualization: "Configure primeiro uma actividade para poder ter uma vista global!",
  history: [],
  allFragmentsLoaded: false,
  outOfLandingPage: false,
  historyEntryCounter: 0,
  recommendationArray: [],
  recommendationIndex: 0,
  fragmentsHashMap: [],
  currentFragmentMode: true
};

const rootReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_FRAGMENT:
      return {
        ...state,
        fragments: [
          ...state.fragments,
          action.payload
        ]
      };
    case SET_FRAGMENT_INDEX:
      return {
        ...state,
        fragmentIndex: action.payload
      };
    case SET_CURRENT_VISUALIZATION:
      return {
        ...state,
        currentVisualization: action.payload
      };
    case ADD_HISTORY_ENTRY:
      return {
        ...state,
        history: [
          ...state.history,
          action.payload
        ]
      };
    case SET_ALL_FRAGMENTS_LOADED:
      return {
        ...state,
        allFragmentsLoaded: action.payload
      };
    case SET_OUT_OF_LANDING_PAGE:
      return {
        ...state,
        outOfLandingPage: action.payload
      };
    case SET_HISTORY_ENTRY_COUNTER:
      return {
        ...state,
        historyEntryCounter: action.payload
      };
    case SET_RECOMMENDATION_ARRAY:
      return {
        ...state,
        recommendationArray: action.payload
      };
    case SET_RECOMMENDATION_INDEX:
      return {
        ...state,
        recommendationIndex: action.payload
      };
    case SET_FRAGMENTS_HASHMAP:
      return {
        ...state,
        fragmentsHashMap: action.payload
      };
    case SET_CURRENT_FRAGMENT_MODE:
      return {
        ...state,
        currentFragmentMode: action.payload
      };
    default:
      return state;
  }
};

export default rootReducer;
