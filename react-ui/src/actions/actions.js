import { SET_COMPARE_IDS_TYPE, SET_INTER_ID, SET_MODULE_CONFIG } from '../constants/actionTypes';

export const setModuleConfig = config => ({ type: SET_MODULE_CONFIG, config });

export const setCompareIdsType = (ids, compareType) => ({ type: SET_COMPARE_IDS_TYPE, ids, compareType });

export const setInterId = id => ({ type: SET_INTER_ID, id });
