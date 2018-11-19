import {ADD_FRAGMENT} from "../constants/action-types";
import {SET_FRAGMENT_INDEX} from "../constants/action-types";
import {SET_CURRENT_VISUALIZATION} from "../constants/action-types";
import {ADD_HISTORY_ENTRY} from "../constants/action-types";

const initialState = {
  fragments: [],
  fragmentIndex: 0,
  currentVisualization: "Configure primeiro uma actividade para poder ter uma vista global!",
  history: []
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
    default:
      return state;
  }
};

export default rootReducer;
