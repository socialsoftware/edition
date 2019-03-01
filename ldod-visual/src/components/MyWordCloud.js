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
import {VIS_SQUARE_GRID, BY_SQUAREGRID_EDITIONORDER, CRIT_EDITION_ORDER, CRIT_CHRONOLOGICAL_ORDER, CRIT_CATEGORY} from "../constants/history-transitions";
import HashMap from "hashmap";
import SquareGrid from "../components/SquareGrid";

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

      this.props.setPotentialCategory(word.text);

      //console.log(word);
      //alert(word.text);
      console.log("MyWordCloud.js | clicked word properties: " + word);
      this.setState({renderSquareMap: true});
      //this.props.onChange();

    }

  }

  componentDidMount() {
    //const height = document.getElementById('container').clientHeight;
    //this.setState({containerHeight: height});
  }

  render() {

    let message = "Esta edição virtual não tem categorias para fazer esta actividade.";
    let data = [];
    let componentToRender;

    console.log("MyWordCloud.js:")
    console.log(this.props.categories);
    console.log(this.props.categories.length);
    if (this.props.categories.length > 0) {

      message = "Esta é uma nuvem de palavras das categorias desta edição virtual. Para explorar fragmentos que pertençam a uma destas categorias, clique com o botão esquerdo do rato numa delas para ser remetido para outro mapa."

      let minFontSize = 5;
      let maxFontSize = 30;

      if (this.props.categories.length < 10) {
        minFontSize = maxFontSize
      }

      let obj;

      let i;
      for (i = 0; i < this.props.categories.length; i++) {
        obj = {
          text: this.props.categories[i].category,
          value: this.props.categories[i].categoryCount
        };
        data.push(obj);
      }

      data.sort(function(a, b) {
        return b.value - a.value
      });
      console.log(data)

      console.log("this.props.categories.length: " + this.props.categories.length);

      const fontSizeMapper = function(word) {
        let fontSize = (word.value / this.props.categories.length) * maxFontSize;
        if (fontSize > maxFontSize) {
          console.log("WORDCLOUD: TRUNCATING TO maxFontSize: " + word.text)
          return maxFontSize
        } else if (fontSize < minFontSize) {
          console.log("WORDCLOUD: TRUNCATING TO minFontSize: " + word.text)
          return minFontSize
        }
        console.log("WORDCLOUD: returning fontsize " + fontSize + " for word : " + word.text)
        return fontSize;
      }.bind(this); //Math.log2(word.value) * 5;

      const rotate = 0; //word => word.value % 360;
      const height = 700;
      const width = 700;
      const padding = 2;
      const font = 'Impact';

      if (this.state.renderSquareMap && !(this.outOfLandingPage)) {
        componentToRender = (<SquareGrid onChange={this.props.onChange}/>)
      } else {
        componentToRender = (<div>
          <p>
            {message}
          </p>

          <div style={{
              display: 'flex',
              justifyContent: 'center',
              height: {
                height
              },
              width: {
                width
              },
              marginBottom: "35px"
              //background: 'black'
            }}>
            <WordCloud data={data} fontSizeMapper={fontSizeMapper} rotate={rotate} onWordClick={this.handleClickedWord} font={font}/>
          </div>

        </div>)
      }

    }

    return (<div className="cloud-activity">{componentToRender}</div>);
  }
}

const MyWordCloud = connect(mapStateToProps, mapDispatchToProps)(ConnectedMyWordCloud);

export default MyWordCloud;
