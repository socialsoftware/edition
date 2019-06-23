import { SET_COMPARE_IDS_TYPE, SET_INTER_ID, SET_MODULE_CONFIG } from '../constants/actionTypes';

const initialState = {
    moduleConfig: {},
    interId: null,
    compareIds: null,
    type: null,
};

function rootReducer(state = initialState, action) {
    switch (action.type) {
        case SET_MODULE_CONFIG:
            return Object.assign({}, state, {
                moduleConfig: action.config,
            });
        case SET_COMPARE_IDS_TYPE:
            return Object.assign({}, state, {
                compareIds: action.ids,
                type: action.compareType,
            });
        case SET_INTER_ID:
            return Object.assign({}, state, {
                interId: action.id,
                compareIds: null,
            });
        default:
            return state;
    }
}

export default rootReducer;
