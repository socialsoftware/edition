import { ADD_FRAGMENT } from "../constants/action-types";
import { SET_FRAGMENT_INDEX } from "../constants/action-types";

const initialState = {
  fragments: [],
  fragmentIndex: 0,
};

const rootReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_FRAGMENT:
      return { ...state, fragments: [...state.fragments, action.payload] };
    case SET_FRAGMENT_INDEX:
      return { ...state, fragmentIndex: action.payload };
    default:
      return state;
  }
};

export default rootReducer;
