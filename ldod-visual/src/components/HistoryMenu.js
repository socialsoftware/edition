import {Network, Timeline, DataSet} from "vis";
import React, {Component} from "react";
import {connect} from "react-redux";
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
  CRIT_WORD_RELEVANCE,
  CRIT_CATEGORY
} from "../constants/history-transitions";
import {
  setFragmentIndex,
  setCurrentVisualization,
  addHistoryEntry,
  setOutOfLandingPage,
  setHistoryEntryCounter,
  setRecommendationArray,
  setRecommendationIndex,
  setVisualizationTechnique,
  setSemanticCriteria,
  setSemanticCriteriaDataLoaded,
  setCurrentCategory
} from "../actions/index";
import "./HistoryMenu.css";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    allFragmentsLoaded: state.allFragmentsLoaded,
    historyEntryCounter: state.historyEntryCounter,
    recommendationindex: state.recommendationIndex,
    recommendationArray: state.recommendationArray
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationIndex: recommendationIndex => dispatch(setRecommendationIndex(recommendationIndex)),
    setVisualizationTechnique: visualizationTechnique => dispatch(setVisualizationTechnique(visualizationTechnique)),
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria)),
    setSemanticCriteriaDataLoaded: semanticCriteriaDataLoaded => dispatch(setSemanticCriteriaDataLoaded(semanticCriteriaDataLoaded)),
    setCurrentCategory: currentCategory => dispatch(setCurrentCategory(currentCategory))
  };
};

class ConnectedHistoryMenu extends Component {
  constructor(props) {
    super(props);

    this.properties = [];

    this.options = {};
    this.timeline = [];

    this.handleClick = this.handleClick.bind(this);

  }

  handleClick(event) {

    //const properties = this.timeline.getEventProperties(event);

    console.log(event)

    const properties = event;

    if (properties.item !== null && this.props.allFragmentsLoaded === true) {

      console.log("properties id: " + parseInt(properties.item));
      console.log("history array: " + this.props.history);

      var i;
      for (i = 0; i < this.props.fragments.length; i++) {
        if (this.props.fragments[i].interId === this.props.history[parseInt(properties.item)].nextFragment.interId) {
          const globalViewToRender = this.props.history[parseInt(properties.item)].visualization //(<SquareGrid onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let historyCategory = "";
          if (this.props.history[parseInt(properties.item)].criteria == CRIT_TAXONOMY) {
            historyCategory = this.props.history[parseInt(properties.item)].category
            this.props.setCurrentCategory(historyCategory);
          }
          let obj;
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.props.history[parseInt(properties.item)].originalFragment,
            nextFragment: this.props.history[parseInt(properties.item)].nextFragment,
            vis: this.props.history[parseInt(properties.item)].vis,
            criteria: this.props.history[parseInt(properties.item)].criteria,
            visualization: globalViewToRender,
            recommendationArray: this.props.history[parseInt(properties.item)].recommendationArray,
            recommendationIndex: this.props.history[parseInt(properties.item)].recommendationIndex,
            start: new Date().getTime(),
            category: historyCategory
          };
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          this.props.onChange();
          this.props.setFragmentIndex(this.props.history[parseInt(properties.item)].fragmentIndex); //mudar a logica para isto ser o fragmento central.

          if (this.props.history[parseInt(properties.item)].vis !== VIS_NETWORK_GRAPH) {
            console.log("HistoryMenu.js: this.props.history[parseInt(properties.item)].vis !== VIS_NETWORK_GRAPH")
            this.props.setRecommendationArray(this.props.history[parseInt(properties.item)].recommendationArray);
            this.props.setRecommendationIndex(this.props.history[parseInt(properties.item)].recommendationIndex);
          } else {
            console.log("HistoryMenu.js: this.props.setSemanticCriteriaDataLoaded(false);")
            this.props.setSemanticCriteriaDataLoaded(false);
          }
          this.props.setVisualizationTechnique(this.props.history[parseInt(properties.item)].vis);
          this.props.setSemanticCriteria(this.props.history[parseInt(properties.item)].criteria)

          //  }

        }

      }
    }
  }

  componentDidMount() {
    var historyItems = [];

    var i;
    for (i = 0; i < this.props.historyEntryCounter; i++) {
      let item = {
        id: this.props.history[i].id,
        content: this.props.history[i].nextFragment.meta.title,
        start: this.props.history[i].start
      };
      historyItems.push(item);
    }

    var container = document.getElementById('visualization');
    console.log("history counter: " + this.props.historyEntryCounter);
    console.log("historico: " + this.props.history);
    console.log("history items: " + historyItems);
    this.timeline = new Timeline(container, historyItems, this.options);
    this.timeline.on('click', this.handleClick);

    //this.printMessage();
  }

  render() {

    let jsxToRender;

    if (this.props.allFragmentsLoaded) {
      jsxToRender = <div id="visualization"></div>
    }

    return (<div className="historyMenu">
      <p>
        Timeline.
      </p>
      {jsxToRender}
    </div>);
  }
}

const HistoryMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedHistoryMenu);

export default HistoryMenu;
