import { SET_MODULE_CONFIG } from '../constants/actionTypes';

const initialState = {
    moduleConfig: {},
};

function rootReducer(state = initialState, action) {
    switch (action.type) {
        case SET_MODULE_CONFIG:
            return Object.assign({}, state, {
                moduleConfig: action.config,
            });
        default:
            return state;
    }
}

export default rootReducer;
