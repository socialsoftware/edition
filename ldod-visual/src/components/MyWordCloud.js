import WordCloud from 'react-d3-cloud';
import React, {Component, createRef} from "react";
import {connect} from "react-redux";
import "./MyWordCloud.css";
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
  setCurrentCategory,
  setPotentialCategory
} from "../actions/index";
import {VIS_SQUARE_GRID, BY_SQUAREGRID_EDITIONORDER, CRIT_EDITION_ORDER, CRIT_CHRONOLOGICAL_ORDER} from "../constants/history-transitions";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    currentFragmentMode: state.currentFragmentMode,
    outOfLandingPage: state.outOfLandingPage,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    allFragmentsLoaded: state.allFragmentsLoaded,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    fragmentsSortedByDate: state.fragmentsSortedByDate,
    categories: state.categories,
    currentCategory: state.currentCategory,
    potentialCategory: state.potentialCategory
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
    setCurrentCategory: currentCategory => dispatch(setCurrentCategory(currentCategory)),
    setPotentialCategory: potentialCategory => dispatch(setPotentialCategory(potentialCategory))
  };
};

class ConnectedMyWordCloud extends Component {
  constructor(props) {
    super(props);

    this.state = {
      renderSquareMap: false,
      containerHeight: 0
    };

    this.handleClickedWord = this.handleClickedWord.bind(this);

  }

  handleClickedWord(word) {

    if (word.hasText) {

      console.log(word);
      alert(word.text);
      this.props.onChange();

    }

  }

  componentDidMount() {
    //const height = document.getElementById('container').clientHeight;
    //this.setState({containerHeight: height});
  }

  render() {

    let message = "Esta edição virtual não tem categorias para fazer esta actividade.";
    let data = [];

    console.log("MyWordCloud.js:")
    console.log(this.props.categories);
    console.log(this.props.categories.length);
    if (this.props.categories.length > 0) {

      message = "Instruções da word cloud."

      const sizeFactor = 100;
      let obj;

      let i;
      for (i = 0; i < this.props.categories.length; i++) {
        obj = {
          text: this.props.categories[i].category,
          value: this.props.categories[i].categoryCount
        };
        data.push(obj);
      }

    }

    const fontSizeMapper = word => ((word.value / this.props.categories.length)) * 90; //Math.log2(word.value) * 5;
    const rotate = 1; //word => word.value % 360;
    const height = 700;
    const width = 900;
    const padding = 6;
    const font = 'Impact';

    return (<div>
      <p>
        {message}
      </p>

      <div style={{
          display: 'flex',
          justifyContent: 'center'
        }}>
        <WordCloud data={data} fontSizeMapper={fontSizeMapper} rotate={rotate} onWordClick={this.handleClickedWord} height={height} width={width} padding={padding} font={font}/>
      </div>

    </div>);
  }
}

const MyWordCloud = connect(mapStateToProps, mapDispatchToProps)(ConnectedMyWordCloud);

export default MyWordCloud;
