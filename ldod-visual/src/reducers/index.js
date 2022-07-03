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
import {SET_RECOMMENDATION_LOADED} from "../constants/action-types";
import {SET_VISUALIZATION_TECHNIQUE} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA_DATA} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA_DATA_LOADED} from "../constants/action-types";
import {SET_POTENTIAL_VISUALIZATION_TECHNIQUE} from "../constants/action-types";
import {SET_POTENTIAL_SEMANTIC_CRITERIA} from "../constants/action-types";
import {SET_POTENTIAL_SEMANTIC_CRITERIA_DATA} from "../constants/action-types";
import {SET_FRAGMENTS_SORTED_BY_DATE} from "../constants/action-types";
import {SET_DISPLAY_TEXT_SKIMMING} from "../constants/action-types";
import {SET_CATEGORIES} from "../constants/action-types";
import {SET_CURRENT_CATEGORY} from "../constants/action-types";
import {SET_POTENTIAL_CATEGORY} from "../constants/action-types";
import {SET_HISTORY} from "../constants/action-types";
import {SET_DATES_EXIST} from "../constants/action-types";
import {SET_GOLDEN_ARRAY} from "../constants/action-types";

const initialState = {
  fragments: [],
  fragmentIndex: 0,
  currentVisualization: "Configure primeiro uma atividade para poder ter uma vista global!",
  history: [],
  allFragmentsLoaded: false,
  outOfLandingPage: false,
  historyEntryCounter: 0,
  recommendationArray: [],
  recommendationIndex: 0,
  fragmentsHashMap: [],
  currentFragmentMode: true,
  recommendationLoaded: true,
  visualizationTechnique: 0,
  semanticCriteria: 0,
  semanticCriteriaData: [],
  semanticCriteriaDataLoaded: true,
  potentialVisualizationTechnique: 0,
  potentialSemanticCriteria: 0,
  potentialSemanticCriteriaData: [],
  fragmentsSortedByDate: [],
  displayTextSkimming: false,
  categories: [],
  currentCategory: [],
  potentialCategory: [],
  datesExist: false,
  goldenArray: []
};

const rootReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_FRAGMENT:
      return {
        ...state,
        fragments: action.payload
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
    case SET_RECOMMENDATION_LOADED:
      return {
        ...state,
        recommendationLoaded: action.payload
      };
    case SET_VISUALIZATION_TECHNIQUE:
      return {
        ...state,
        visualizationTechnique: action.payload
      };
    case SET_SEMANTIC_CRITERIA:
      return {
        ...state,
        semanticCriteria: action.payload
      };
    case SET_SEMANTIC_CRITERIA_DATA:
      return {
        ...state,
        semanticCriteriaData: action.payload
      };
    case SET_SEMANTIC_CRITERIA_DATA_LOADED:
      return {
        ...state,
        semanticCriteriaDataLoaded: action.payload
      };
    case SET_POTENTIAL_VISUALIZATION_TECHNIQUE:
      return {
        ...state,
        potentialVisualizationTechnique: action.payload
      };
    case SET_POTENTIAL_SEMANTIC_CRITERIA:
      return {
        ...state,
        potentialSemanticCriteria: action.payload
      };
    case SET_POTENTIAL_SEMANTIC_CRITERIA_DATA:
      return {
        ...state,
        potentialSemanticCriteriaData: action.payload
      };
    case SET_FRAGMENTS_SORTED_BY_DATE:
      return {
        ...state,
        fragmentsSortedByDate: action.payload
      };
    case SET_DISPLAY_TEXT_SKIMMING:
      return {
        ...state,
        setDisplayTextSkimming: action.payload
      };
    case SET_CATEGORIES:
      return {
        ...state,
        categories: action.payload
      };
    case SET_CURRENT_CATEGORY:
      return {
        ...state,
        currentCategory: action.payload
      };
    case SET_POTENTIAL_CATEGORY:
      return {
        ...state,
        potentialCategory: action.payload
      };
    case SET_HISTORY:
      return {
        ...state,
        history: action.payload
      };
    case SET_DATES_EXIST:
      return {
        ...state,
        datesExist: action.payload
      };
    case SET_GOLDEN_ARRAY:
      return {
        ...state,
        goldenArray: action.payload
      };
    default:
      return state;
  }
};

export default rootReducer;
