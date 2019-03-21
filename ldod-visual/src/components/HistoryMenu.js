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
import picNetGraph from '../assets/picnetgraph.png';
import picSquareGrid from '../assets/picsquaregrid.png';
import loadingGif from '../assets/loading.gif';

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

    this.state = {
      loadingGif: (<div>
        <img src={loadingGif} alt="loading...myhistory" className="loadingGifCentered"/>
        <p align="center">A carregar histórico...</p>
      </div>)
    };

    var height = Math.round(window.innerHeight * 0.7) + 'px';

    this.clearLoadingGif = this.clearLoadingGif.bind(this);

    this.options = {
      locales: {
        // create a new locale (text strings should be replaced with localized strings)
        pt: {}
      },

      // use the new locale
      locale: 'pt',
      height: height,
      onInitialDrawComplete: this.clearLoadingGif
    };
    this.timeline = [];

    this.handleClick = this.handleClick.bind(this);

  }

  clearLoadingGif() {
    this.setState({loadingGif: (<div/>)});
  }

  handleClick(event) {

    //const properties = this.timeline.getEventProperties(event);

    console.log(event)

    const properties = event;

    if (properties.item !== null && this.props.allFragmentsLoaded === true) {

      console.log("properties id: " + parseInt(properties.item));
      console.log("history array: " + this.props.history);

      var i;
      for (i = this.props.fragments.length - 1; i !== 0; i--) {
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

          //parte que foi feita para não saltar para o fragmento clicado no historico via netgraph pois era preciso ir alterar o historico no fragmentloader depois de carregar um novo recommendation index que era preciso fazer set do mesmo na ultima entrada do historico.

          //if (this.props.history[parseInt(properties.item)].vis !== VIS_NETWORK_GRAPH) {
          console.log("HistoryMenu.js: this.props.history[parseInt(properties.item)].vis !== VIS_NETWORK_GRAPH")
          this.props.setRecommendationArray(this.props.history[parseInt(properties.item)].recommendationArray);
          this.props.setRecommendationIndex(this.props.history[parseInt(properties.item)].recommendationIndex);
          //} else {
          //  console.log("HistoryMenu.js: this.props.setSemanticCriteriaDataLoaded(false);")
          //  this.props.setSemanticCriteriaDataLoaded(false);
          //}
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
      var historyPic = document.createElement('div');
      historyPic.appendChild(document.createTextNode(this.props.history[i].nextFragment.meta.title));
      historyPic.appendChild(document.createElement('br'));
      var img = document.createElement('img');

      img.style.width = '48px';
      img.style.height = '48px';

      if (this.props.history[i].vis == VIS_NETWORK_GRAPH) {
        img.src = picNetGraph;
      } else if (this.props.history[i].vis == VIS_SQUARE_GRID) {
        img.src = picSquareGrid;
      }
      historyPic.appendChild(img);
      let item = {
        id: this.props.history[i].id,
        content: historyPic,
        start: this.props.history[i].start
      };
      console.log("item id: " + item.id);
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
        Nesta cronologia, poderá consultar o seu caminho na leitura da edição virtual do Livro do Desassossego que seleccionou. Basta clicar num dos títulos do fragmentos para retornar ao mesmo bem como a actividade pela qual chegou a esse fragmento.
      </p>
      {this.state.loadingGif}
      {jsxToRender}
    </div>);
  }
}

const HistoryMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedHistoryMenu);

export default HistoryMenu;
