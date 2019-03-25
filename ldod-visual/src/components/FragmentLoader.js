import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import Fragment from "./Fragment";
import {
  addFragment,
  setAllFragmentsLoaded,
  setfragmentsHashMap,
  setRecommendationArray,
  setRecommendationLoaded,
  setSemanticCriteriaData,
  setPotentialSemanticCriteriaData,
  setFragmentsSortedByDate,
  setCategories,
  setHistory,
  setDatesExist
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
    setPotentialSemanticCriteriaData: potentialSemanticCriteriaData => dispatch(setPotentialSemanticCriteriaData(potentialSemanticCriteriaData)),
    setFragmentsSortedByDate: fragmentsSortedByDate => dispatch(setFragmentsSortedByDate(fragmentsSortedByDate)),
    setCategories: categories => dispatch(setCategories(categories)),
    setHistory: history => dispatch(setHistory(history)),
    setDatesExist: datesExist => dispatch(setDatesExist(datesExist))
  };
};

class ConnectedFragmentLoader extends React.Component {
  constructor(props) {
    super(props);

    this.map = new HashMap();

    this.sortedFragmentsByDate = [];

    this.state = {
      toggleFragmentUpdate: false
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

        const service = new RepositoryService(this.props.currentEdition.acronym);
        let idsDistanceArray = [];
        let myNewRecommendationArray = [];
        service.getIntersByDistance(this.props.fragments[this.props.fragmentIndex].interId, pHeteronymWeight, pTextWeight, pDateWeight, ptaxonomyWeight).then(response => {

          let receivedArray = response.data;

          receivedArray.map(f => {
            f.distance = (1 - f.distance)
          });

          idsDistanceArray = receivedArray;

          this.props.setSemanticCriteriaData(idsDistanceArray);

          var i;
          for (i = 0; i < idsDistanceArray.length; i++) {
            let frag = this.props.fragmentsHashMap.get(idsDistanceArray[i].interId);
            myNewRecommendationArray.push(frag);
          }
          this.props.setRecommendationArray(myNewRecommendationArray);
          console.log("FragmentLoader: new recommendation array calculated. Adding to history")

          let myTempObj = this.props.history[this.props.historyEntryCounter - 1];
          let myTempHist = this.props.history;
          //my temp set aqui: pegar no historico, mudar a ultima casa, fazer set ao historico completo com a casa actualizada com o novo recommendationArray.

          myTempObj.recommendationArray = myNewRecommendationArray;
          myTempObj.recommendationIndex = 0;

          myTempHist[this.props.historyEntryCounter - 1] = myTempObj;

          this.props.setHistory(myTempHist);

          // console.log("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVV " + this.props.history[this.props.historyEntryCounter - 1].recommendationIndex)
          // console.log("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVV " + this.props.history[this.props.historyEntryCounter - 1].recommendationArray)

          this.props.setRecommendationLoaded(true);

          this.setState({
            toggleFragmentUpdate: !this.state.toggleFragmentUpdate
          });

        });
      }
    }

  }

  componentDidMount() {
    if (!this.props.allFragmentsLoaded) {
      const service = new RepositoryService(this.props.currentEdition.acronym);
      this.props.setAllFragmentsLoaded(false);
      this.props.setFragmentsSortedByDate([]);
      this.props.setDatesExist(false);
      console.log("FragmentLoader.js: componentDidMount -> requesting fragments for edition acronym: " + this.props.currentEdition.acronym + " with edition title: " + this.props.currentEdition.title)
      service.getFragments().then(response => {
        console.log("FragmentLoader.js: receiving fragments");
        console.log(response);

        //response.data.fragments.map(f => f.text = f.text.concat(" base Base toda Toda"));

        this.props.addFragment(response.data.fragments);
        this.props.fragments.map(f => this.map.set(f.interId, f));
        this.props.setfragmentsHashMap(this.map);

        let unorderedFragments = [];
        let noDateFragments = [];
        let myFragmentsSortedByDate = [];

        this.props.fragments.map(f => {

          if (f.meta.date !== null) {
            return (unorderedFragments.push(f));
          } else {
            return (noDateFragments.push(f));
          }
        });

        if (unorderedFragments.length > 0) {

          this.props.setDatesExist(true);

          unorderedFragments.sort((frag1, frag2) => {
            let date1 = frag1.meta.date.split('-');
            let date2 = frag2.meta.date.split('-');
            let year1 = parseInt(date1[0]);
            let year2 = parseInt(date2[0]);
            let month1 = parseInt(date1[1]);
            let month2 = parseInt(date2[1]);
            let day1 = parseInt(date1[2]);
            let day2 = parseInt(date2[2]);
            if (year1 !== year2) {
              return year1 - year2;
            } else if (month1 !== month2) {
              return month1 - month2;
            } else {
              return day1 - day2;
            }
          });

          // console.log(unorderedFragments.length);

          myFragmentsSortedByDate = unorderedFragments.concat(noDateFragments);

          // console.log(myFragmentsSortedByDate.length);

          // myFragmentsSortedByDate.map(f => console.log(f.meta.date));

          this.props.setFragmentsSortedByDate(myFragmentsSortedByDate);

        }

        if (response.data.categories) {

          let myCategories = response.data.categories;
          let tempCounter = 0;

          let i;
          let j;
          for (i = 0; i < myCategories.length; i++) {

            for (j = 0; j < response.data.fragments.length; j++) {

              if (response.data.fragments[j].meta.categories.includes(myCategories[i])) {

                tempCounter++;
              }
            }

            let obj = {
              category: myCategories[i],
              categoryCount: tempCounter
            };
            // console.log("counting category: number " + 1 + " out of " + myCategories.length + " (" + myCategories[i] + ")");
            myCategories[i] = obj;
            tempCounter = 0;

          }

          this.props.setCategories(myCategories);
        }

        this.props.setAllFragmentsLoaded(true);

        this.setState({
          toggleFragmentUpdate: !this.state.toggleFragmentUpdate
        });

      });
    }
  }

  render() {
    // const service = new RepositoryService("bla");
    // service.getPublicEditions().then(response => {
    //   console.log("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ |" + response.data.map(e => console.log("|Title: " + e.title + " |Acronym:" + e.acronym + " |hasCategories: " + e.taxonomy.hasCategories)));
    // });

    return <Fragment currentEdition={this.props.currentEdition} toggleTextSkimming={this.props.toggleTextSkimming}/>;
  }

}

const FragmentLoader = connect(mapStateToProps, mapDispatchToProps)(ConnectedFragmentLoader);

export default FragmentLoader;
