import {
    SET_COMPARE_IDS_TYPE,
    SET_INTER_ID, SET_LOGIN_STATUS,
    SET_MODULE_CONFIG,
    SET_USER_INFO,
} from '../constants/actionTypes';

export const initialState = {
    moduleConfig: null,
    interId: null,
    compareIds: null,
    type: null,
    status: false,
    info: null,
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
        case SET_LOGIN_STATUS:
            console.log('Setting login status');
            return Object.assign({}, state, {
                status: action.status,
            });
        case SET_USER_INFO:
            return Object.assign({}, state, {
                info: action.info,
            });
        default:
            return state;
    }
}

export default rootReducer;
