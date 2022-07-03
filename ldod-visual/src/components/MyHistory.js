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
import "./MyHistory.css";
import loadingGif from '../assets/loading.gif';
import {Button} from "react-bootstrap";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    allFragmentsLoaded: state.allFragmentsLoaded,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    outOfLandingPage: state.outOfLandingPage,
    currentFragmentMode: state.currentFragmentMode,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    displayTextSkimming: state.displayTextSkimming,
    categories: state.categories,
    history: state.history,
    datesExist: state.datesExist,
    historyEntryCounter: state.historyEntryCounter,
    currentCategory: state.currentCategory,
    fragmentsSortedByDate: state.fragmentsSortedByDate
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

class ConnectedMyHistory extends Component {
  constructor(props) {
    super(props);

    this.properties = [];

    this.actualIndex = 0;

    var height = Math.round(window.innerHeight * 0.7) + 'px';

    this.state = {
      refresh: false,
      showInstructions: false
    };

    this.options = {
      locales: {
        // create a new locale (text strings should be replaced with localized strings)
        pt: {}
      },

      // use the new locale
      locale: 'pt',
      height: height
    };
    this.timeline = [];
    this.jsxToRender = [];

    this.loadingGif = (<div>
      <img src={loadingGif} alt="loading...myhistory" className="loadingGifCentered"/>
      <p align="center">A carregar atividade...</p>
    </div>);

    this.handleClick = this.handleClick.bind(this);

    this.clearLoadingGif = this.clearLoadingGif.bind(this);

    this.myHistoryArray = [];

    this.toggleInstructions = this.toggleInstructions.bind(this);

  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions
    });
  }

  handleClick(event) {
    console.log(event)
    const nodeId = event.item;
    if (nodeId) {
      //alert(nodeId);
      // this.props.setSemanticCriteria(this.props.potentialSemanticCriteria);
      var i;
      for (i = 0; i < this.myHistoryArray.length; i++) {
        if (this.myHistoryArray[i].interId === nodeId) {

          const globalViewToRender = (<MyHistory onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          let historyVis = this.props.visualizationTechnique;
          let historyCriteria = this.props.semanticCriteria;
          if (this.props.currentFragmentMode) {
            historyVis = this.props.potentialVisualizationTechnique;
            historyCriteria = this.props.potentialSemanticCriteria;
          }
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.myHistoryArray[i], //this.props.recommendationArray[this.props.fragmentIndex],
            nextFragment: this.myHistoryArray[i],
            vis: historyVis,
            criteria: historyCriteria,
            visualization: globalViewToRender,
            recommendationArray: this.myHistoryArray, //mudar para quando o cirterio for difernete,
            recommendationIndex: i,
            fragmentIndex: this.props.fragmentIndex,
            start: new Date().getTime(),
            category: ""
          };

          //aqui é para fazer set do fragindex caso seja uma nova atividade. caso contrario fica igual.
          if (this.props.currentFragmentMode) {
            var j;
            for (j = 0; j < this.props.fragments.length; j++) {
              if (this.props.fragments[j].interId === nodeId) {
                console.log("squareGrid: because of currentFragmentMode, setFragmentIndex is now: " + j)
                this.props.setFragmentIndex(j);
                obj.fragmentIndex = j;
              }
            }
          }
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          this.props.setVisualizationTechnique(this.props.potentialVisualizationTechnique);
          this.props.setSemanticCriteria(this.props.potentialSemanticCriteria);
          this.props.setRecommendationArray(this.myHistoryArray);
          this.props.setRecommendationIndex(i);
          this.props.onChange();
        }
      }
    }
  }

  _handleKeyDownActivity = (event) => {

    var I_KEY = 73;

    switch (event.keyCode) {
      case I_KEY:
        this.toggleInstructions();
        break;
      default:
        break;

    }

  }

  componentDidMount() {
    document.addEventListener("keydown", this._handleKeyDownActivity);

    var historyItems = [];

    this.timeline = [];

    this.props.fragmentsSortedByDate.map(f => {

      if (f.meta.date !== null) {
        let date = f.meta.date.split('-');
        let year = parseInt(date[0]);
        let month = parseInt(date[1]);
        let day = parseInt(date[2]);

        let entryStyle = "" //default

        //purple
        if (!this.props.currentFragmentMode && f.interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
          entryStyle = "color: white; background-color: #8A2BE2; border-color: #4B0082;"
        }
        //red
        if (!this.props.currentFragmentMode && f.interId === this.props.fragments[this.props.fragmentIndex].interId) {
          entryStyle = "color: white; background-color: #FF7F50; border-color: #DC143C;"
        }
        //red para tornar vermelho o que será vermelho em nova atividade em vez de considerar o da antiga
        if (this.props.currentFragmentMode && f.interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
          entryStyle = "color: white; background-color: #FF7F50; border-color: #DC143C;"
        }

        let myDate = new Date(year, month - 1, day);
        console.log("MyHistory: Added date " + myDate + " |title: " + f.meta.title)

        let item = {
          id: f.interId,
          content: "<p>" + f.meta.title + "</p><p>" + " (" + f.meta.date + ")" + "</p>",
          start: myDate,
          style: entryStyle
        };
        console.log("item id: " + item.id);
        historyItems.push(item);
        this.myHistoryArray.push(f);
      }
    });

    var container = document.getElementById('visualization');
    console.log("history counter: " + this.props.historyEntryCounter);
    console.log("historico: " + this.props.history);
    console.log("history items: " + historyItems);
    this.timeline = new Timeline(container, historyItems, this.options);

    this.timeline.on('click', this.handleClick);

    var moveToOptions = {
      animation: { // animation object, can also be Boolean
        duration: 1000, // animation duration in milliseconds (Number)
        easingFunction: "easeInCubic" // Animation easing function, available are:
      } // linear, easeInQuad, easeOutQuad, easeInOutQuad,
    } // easeInCubic, easeOutCubic, easeInOutCubic,
    // easeInQuart, easeOutQuart, easeInOutQuart,
    // easeInQuint, easeOutQuint, easeInOutQuint

    //this.timeline.focus(this.props.recommendationArray[this.props.recommendationIndex].interId);

    //this.timeline.moveTo(this.props.recommendationArray[this.props.recommendationIndex].meta.date);

    let z;
    for (z = 0; z < this.myHistoryArray.length; z++) {
      if (this.myHistoryArray[z].interId == this.props.recommendationArray[this.props.recommendationIndex].interId) {
        this.actualIndex = z;
        break;
      }
    }

    let imedPrevIndex = this.actualIndex;
    let imedNextIndex = this.actualIndex;

    let j;
    for (j = (this.actualIndex); j < this.myHistoryArray.length; j++) {
      if (this.myHistoryArray[j].meta.date !== this.myHistoryArray[this.actualIndex].meta.date) {
        imedNextIndex = j;
        break
      }
    }

    let n;
    for (n = (this.actualIndex); n !== -1; n--) {
      if (this.myHistoryArray[n].meta.date !== this.myHistoryArray[this.actualIndex].meta.date && n >= 0) {
        imedPrevIndex = n;
        break
      }
    }

    let date1 = this.myHistoryArray[imedPrevIndex].meta.date.split('-');
    let year1 = parseInt(date1[0]);
    let month1 = parseInt(date1[1]);
    let day1 = parseInt(date1[2]);

    let myDate1 = new Date(year1, Math.max(1, (month1 - 4)), 15);

    let date2 = this.myHistoryArray[imedNextIndex].meta.date.split('-');
    let year2 = parseInt(date2[0]);
    let month2 = parseInt(date2[1]);
    let day2 = parseInt(date2[2]);

    let myDate2 = new Date(year2, Math.min(12, (month2 + 2)), 15);

    console.log("myhistory debug: imedPrevIndex date " + this.myHistoryArray[imedPrevIndex].meta.date + " | " + imedPrevIndex + " " + this.myHistoryArray[imedPrevIndex].meta.title)
    console.log("myhistory debug: this.actualIndex date " + this.myHistoryArray[this.actualIndex].meta.date + " | " + this.actualIndex + " " + this.myHistoryArray[this.actualIndex].meta.title)
    console.log("myhistory debug: imedNextIndex date " + this.myHistoryArray[imedNextIndex].meta.date + " | " + imedNextIndex + " " + this.myHistoryArray[imedNextIndex].meta.title)

    this.timeline.setWindow(myDate1, myDate2, this.clearLoadingGif);

    // setInterval(() => {
    //   this.clearLoadingGif();
    // }, 500);

    this.timeline.on("itemover", function(params) {
      document.getElementById('visualization').style.cursor = 'pointer'
    });
    this.timeline.on("itemout", function(params) {
      document.getElementById('visualization').style.cursor = 'default'
    });

  }

  clearLoadingGif() {
    this.loadingGif = (<div/>);
    this.setState({});
  }

  render() {

    let redSquareText = (<span style={{
        background: "#FF7F50",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#FF7F50",
        color: 'white'
      }}>
      <b>retângulo laranja</b>
    </span >);

    let purpleSquareText = (<span style={{
        background: "#8A2BE2",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#8A2BE2",
        color: 'white'
      }}>
      <b>retângulo roxo</b>
    </span >);

    let instructions = (<div className="instructionsButton">
      <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
        Mostrar instruções [i]
      </Button>
    </div>)

    if (this.state.showInstructions) {
      instructions = (<div>
        <div className="instructionsText">

          <p>
            Nesta cronologia, poderá situar o fragmento atual e a sua data em comparação ao resto dos fragmentos da edição que tenham também data disponível.
          </p>

          <ul>
            <li>
              Cada retângulo representa um fragmento com o título apresentado.
            </li>
            <li>
              Pode controlar o zoom da cronologia com a roda do rato.
            </li>
            <li>
              Pode arrastar a cronologia com o botão esquerdo do rato (caso haja muitos fragmentos com data semelhante, é possível que empilhem para cima da altura da janela da cronologia).
            </li>
            <li>
              Um {redSquareText}
              representará o fragmento sob o qual realizou ou está a realizar uma nova atividade.
            </li>
            <li>
              Um {purpleSquareText}
              representará o fragmento que está a ler durante a atividade atual caso navegue para um fragmento diferente do fragmento inicial que escolheu ao selecionar uma nova atividade (o {redSquareText}).
            </li>
            <li>
              Clique num dos retângulos dos fragmentos para ler o mesmo.
            </li>

          </ul>

        </div>

        <div className="instructionsButton">
          <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
            Esconder instruções [i]
          </Button>
        </div>

        <br/>

      </div>)

    }

    if (this.props.allFragmentsLoaded) {
      this.jsxToRender = <div id="visualization"></div>
    }

    return (<div className="myHistory">
      {instructions}
      {this.loadingGif}
      <div id="visualization"></div>
    </div>);
  }
}

const MyHistory = connect(mapStateToProps, mapDispatchToProps)(ConnectedMyHistory);

export default MyHistory;
