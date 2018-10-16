import { ADD_FRAGMENT } from "../constants/action-types";
import { SET_FRAGMENT_INDEX } from "../constants/action-types";

export const addFragment = fragment => ({ type: ADD_FRAGMENT, payload: fragment });

export const setFragmentIndex = fragmentIndex => ({ type: SET_FRAGMENT_INDEX, payload: fragmentIndex });
