import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import {
  addFragment,
  setAllFragmentsLoaded,
  setfragmentsHashMap,
  setRecommendationArray,
  setRecommendationLoaded,
  setSemanticCriteriaData,
  setPotentialSemanticCriteriaData
} from "../actions/index";
import {connect} from "react-redux";
import HashMap from "hashmap";
import {
  VIS_SQUARE_GRID,
  VIS_NETWORK_GRAPH,
  VIS_WORD_CLOUD,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_TEXT_SIMILARITY,
  CRIT_HETERONYM,
  CRIT_TAXONOMY,
  CRIT_WORD_RELEVANCE
} from "../constants/history-transitions";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    fragmentsHashMap: state.fragmentsHashMap,
    fragmentIndex: state.fragmentIndex,
    currentFragmentMode: state.currentFragmentMode,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationLoaded: state.recommendationLoaded,
    semanticCriteriaDataLoaded: state.semanticCriteriaDataLoaded,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter
  };
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setfragmentsHashMap: fragmentsHashMap => dispatch(setfragmentsHashMap(fragmentsHashMap)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationLoaded: recommendationLoaded => dispatch(setRecommendationLoaded(recommendationLoaded)),
    setSemanticCriteriaData: semanticCriteriaData => dispatch(setSemanticCriteriaData(semanticCriteriaData)),
    setPotentialSemanticCriteriaData: potentialSemanticCriteriaData => dispatch(setPotentialSemanticCriteriaData(potentialSemanticCriteriaData))
  };
};

class ConnectedFragmentLoader extends React.Component {
  constructor(props) {
    super(props);

    this.map = new HashMap();

    this.sortedFragmentsByDate = [];

    this.state = {
      acronym: "LdoD-Arquivo",
      //acronym: "LdoD-test",
      localHistoryCount: 0,
      idsDistanceArray: []
    };
  }

  componentDidUpdate(prevProps, prevState) {

    if (!(this.props.recommendationLoaded)) {
      console.log("Loading recommendation array");

      if (this.props.visualizationTechnique == VIS_NETWORK_GRAPH) {

        let pHeteronymWeight = "0.0";
        let pTextWeight = "0.0";
        let pDateWeight = "0.0";
        let ptaxonomyWeight = "0.0";

        if (this.props.semanticCriteria == CRIT_HETERONYM) {
          pHeteronymWeight = "1.0";
        } else if (this.props.semanticCriteria == CRIT_TEXT_SIMILARITY) {
          pTextWeight = "1.0";
        } else if (this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER) {
          pDateWeight = "1.0";
        } else if (this.props.semanticCriteria == CRIT_TAXONOMY) {
          ptaxonomyWeight = "1.0";
        }

        const service = new RepositoryService();
        let idsDistanceArray = [];
        let myNewRecommendationArray = [];
        service.getIntersByDistance(this.props.fragments[this.props.fragmentIndex].interId, pHeteronymWeight, pTextWeight, pDateWeight, ptaxonomyWeight).then(response => {

          idsDistanceArray = response.data;
          this.props.setSemanticCriteriaData(idsDistanceArray);

          var i;
          for (i = 0; i < idsDistanceArray.length; i++) {
            let frag = this.props.fragmentsHashMap.get(idsDistanceArray[i].interId);
            myNewRecommendationArray.push(frag);
          }
          this.props.setRecommendationArray(myNewRecommendationArray);
          console.log("FragmentLoader: new recommendation array calculated.")
          this.props.setRecommendationLoaded(true);

        });
      }
    }

  }

  componentDidMount() {
    const service = new RepositoryService();
    console.log("FragmentLoader.js: componentDidMount -> requesting fragments")
    service.getFragments(this.state.acronym).then(response => {
      response.data.fragments.map(f => this.props.addFragment(f));
      this.props.fragments.map(f => this.map.set(f.interId, f));
      this.props.setfragmentsHashMap(this.map);

      console.log("FragmentLoader.js: fragments loaded at componentdidmount")

      this.props.setAllFragmentsLoaded(true);
      //alert(this.props.fragments.length)
    });
  }

  render() {
    return <div></div>;
  }

}

const FragmentLoader = connect(mapStateToProps, mapDispatchToProps)(ConnectedFragmentLoader);

export default FragmentLoader;
