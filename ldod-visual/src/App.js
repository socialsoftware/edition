import React, {Component} from "react";
import "./App.css";
import Fragment from "./components/Fragment";
import {
  setFragmentIndex,
  addHistoryEntry,
  setRecommendationArray,
  setRecommendationIndex,
  setCurrentFragmentMode,
  setSemanticCriteria,
  setVisualizationTechnique,
  setPotentialVisualizationTechnique,
  setPotentialSemanticCriteria,
  setDisplayTextSkimming,
  setHistoryEntryCounter,
  setAllFragmentsLoaded
} from "./actions/index";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal, OverlayTrigger, Tooltip} from "react-bootstrap";
import ActivityMenu from "./components/ActivityMenu";
import HistoryMenu from "./components/HistoryMenu";
import NavigationButton from "./components/NavigationButton";
import {
  VIS_NETWORK,
  BY_HISTORIC,
  BY_NEXTBUTTON,
  BY_PREVIOUSBUTTON,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  VIS_SQUARE_GRID,
  CRIT_CATEGORY,
  CRIT_HETERONYM
} from "./constants/history-transitions";
import NetworkGraph from "./components/NetworkGraph";
import FragmentLoader from "./components/FragmentLoader";
import SquareGrid from "./components/SquareGrid";
import MyWordCloud from "./components/MyWordCloud";
import PublicEditionContainerTable from "./containers/PublicEditionContainerTable";
import ReactHtmlParser, {processNodes, convertNodeToElement, htmlparser2} from 'react-html-parser';
import loadingGif from './assets/loading.gif';
import loadingFragmentsGif from './assets/fragmentload.gif';
import IdleTimer from 'react-idle-timer';
import {
  Link,
  DirectLink,
  Element,
  Events,
  animateScroll as scroll,
  scrollSpy,
  scroller
} from 'react-scroll';
import Favicon from 'react-favicon';
import {Helmet} from "react-helmet";

import picNetgraph from './assets/card-pics-regular/netgraph.png';
import picSquare from './assets/card-pics-regular/square.png';
import picSquareGolden from './assets/card-pics-regular/square-golden.png';
import picSquareTime from './assets/card-pics-regular/square-time.png';
import picWordCloud from './assets/card-pics-regular/word-cloud.png';

import picNetgraphGray from './assets/card-pics-gray/netgraph-gray.png';
import picSquareGray from './assets/card-pics-gray/square-gray.png';
import picSquareGoldenGray from './assets/card-pics-gray/square-golden-gray.png';
import picSquareTimeGray from './assets/card-pics-gray/square-time-gray.png';
import picWordCloudGray from './assets/card-pics-gray/word-cloud-gray.png';

import HashMap from "hashmap";

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
    currentCategory: state.currentCategory
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationIndex: recommendationIndex => dispatch(setRecommendationIndex(recommendationIndex)),
    setCurrentFragmentMode: currentFragmentMode => dispatch(setCurrentFragmentMode(currentFragmentMode)),
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria)),
    setVisualizationTechnique: visualizationTechnique => dispatch(setVisualizationTechnique(visualizationTechnique)),
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria)),
    setPotentialVisualizationTechnique: potentialVisualizationTechnique => dispatch(setPotentialVisualizationTechnique(potentialVisualizationTechnique)),
    setPotentialSemanticCriteria: potentialSemanticCriteria => dispatch(setPotentialSemanticCriteria(potentialSemanticCriteria)),
    setDisplayTextSkimming: displayTextSkimming => dispatch(setDisplayTextSkimming(displayTextSkimming)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded))
  };
};

const styles = {
  transition: 'all 0.2s ease-out'
};

class ConnectedApp extends Component {
  constructor(props) {
    super(props);

    this.buttonToolBarToRender = <div/>

    this.goldenSquareArray = [];
    let obj = {
      goldenIndex: " ",
      currentCategory: " ",
      goldenId: " "
    }
    this.goldenSquareArray.push(obj);

    this.nextGoldenSquareIndex = null;
    this.previousGoldenSquareIndex = null;

    this.idleTimer = null
    this.onAction = this._onAction.bind(this)
    this.onActive = this._onActive.bind(this)
    this.onIdle = this._onIdle.bind(this)

    this.handlePreviousGoldenButton = this.handlePreviousGoldenButton.bind(this)
    this.handleNextGoldenButton = this.handleNextGoldenButton.bind(this)

    this.forcePageReload = this.forcePageReload.bind(this)

    this.handleEditionSelectRetreat = this.handleEditionSelectRetreat.bind(this)
    this.handleFirstActivitySelectRetreat = this.handleFirstActivitySelectRetreat.bind(this)

    this.previousFragmentButtonStyle = "primary";
    this.nextFragmentButtonStyle = "primary";

    this.state = {
      previousFragmentButtonStyle: "primary",
      nextFragmentButtonStyle: "primary",
      showConfig: false,
      showGlobalView: false,
      showHistoric: false,
      showLanding: true,
      showLandingActivity: false,
      toggleTextSkimming: false,
      toggleUpdateFragmentsReceived: false,
      showEditionSelection: true,
      editionsReceived: false,
      editionSelected: false,
      currentEdition: "",
      fadeIn: true,
      fadeInterval: 20,
      fadeDelay: 200,
      opacity: 1,
      mouseOverMenuButtons: true,
      hiddenFromIdle: false,
      previousGoldenButtonClass: "goldenButtonPrevious",
      nextGoldenButtonClass: "goldenButtonNext"
    };

    this.opacityHide = 0;
    this.opacityShow = 1;
    this.opacityOnText = 0.6;
    this.opacityBarelyVisible = 0.3;

    this.landingActivityToRender = <p>A carregar edições virtuais...</p>;

    this.handleShowGlobalView = this.handleShowGlobalView.bind(this);
    this.handleCloseGlobalView = this.handleCloseGlobalView.bind(this);

    this.handleShowConfig = this.handleShowConfig.bind(this);
    this.handleCloseConfig = this.handleCloseConfig.bind(this);

    this.handleShowHistoric = this.handleShowHistoric.bind(this);
    this.handleCloseHistoric = this.handleCloseHistoric.bind(this);

    this.handleCloseModals = this.handleCloseModals.bind(this);
    this.handleCloseLanding = this.handleCloseLanding.bind(this);

    this.setMouseOverMenuButtons = this.setMouseOverMenuButtons.bind(this);
    this.setMouseOutMenuButtons = this.setMouseOutMenuButtons.bind(this);

    //show landing activities
    this.handleShowLandingActivitySquareEditionOrder = this.handleShowLandingActivitySquareEditionOrder.bind(this);
    this.handleShowLandingActivitySquareDateOrder = this.handleShowLandingActivitySquareDateOrder.bind(this);

    this.handleToggleTextSkimming = this.handleToggleTextSkimming.bind(this);

    this.handleShowLandingActivityWordCloudCategory = this.handleShowLandingActivityWordCloudCategory.bind(this);

    this.handleToggleFragmentsReceived = this.handleToggleFragmentsReceived.bind(this);

    this.handleEditionsReceived = this.handleEditionsReceived.bind(this);

    this.handleEditionSelected = this.handleEditionSelected.bind(this);

    this.addNewHistoryEntry = this.addNewHistoryEntry.bind(this);

    this.nextButtonAction = this.nextButtonAction.bind(this);
    this.previousButtonAction = this.previousButtonAction.bind(this);

  }

  setMouseOverMenuButtons() {
    this.setState({mouseOverMenuButtons: true});
    console.log("setMouseOverMenuButtons");
  }

  setMouseOutMenuButtons() {
    this.setState({mouseOverMenuButtons: false});
    console.log("setMouseOutMenuButtons")
  }

  handleCloseModals() {
    this.setState({showConfig: false, showGlobalView: false, showLanding: false, showHistoric: false});
    this.setState({opacity: this.opacityShow})
    this.setState({hiddenFromIdle: false});
    scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
  }

  handleCloseConfig() {
    this.setState({showConfig: false});
    this.setState({opacity: this.opacityShow})
    this.setState({hiddenFromIdle: false});

  }

  handleShowConfig() {
    this.setState({showConfig: true});
    this.props.setCurrentFragmentMode(true);
    console.log("App.handleShowConfig: setting currentFragmentMode to true")

  }

  handleCloseGlobalView() {
    this.setState({showGlobalView: false});
    this.setState({opacity: this.opacityShow})
    this.setState({hiddenFromIdle: false});

  }

  handleShowGlobalView() {
    this.setState({showGlobalView: true});
    this.props.setCurrentFragmentMode(false);
    console.log("App.handleShowGlobalView(): setting currentFragmentMode to false (PickedFragmentMode)")

  }

  handleCloseHistoric() {
    this.setState({showHistoric: false});
    this.setState({opacity: this.opacityShow})
    this.setState({hiddenFromIdle: false});

  }

  handleShowHistoric() {
    this.props.setCurrentFragmentMode(true);
    this.setState({showHistoric: true});
  }

  handleCloseLanding() {
    this.setState({showLanding: false});
    console.log("calling handlecloselanding()");
    this.setState({opacity: this.opacityShow})
    this.setState({hiddenFromIdle: false});
  }

  handleShowLandingActivitySquareEditionOrder() {
    this.props.setCurrentFragmentMode(true);
    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_EDITION_ORDER);
    this.setState({showLandingActivity: true});
  }

  handleShowLandingActivitySquareDateOrder() {
    this.props.setCurrentFragmentMode(true);
    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
    this.setState({showLandingActivity: true});
  }

  handleShowLandingActivityWordCloudCategory() {
    this.props.setCurrentFragmentMode(true);
    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_CATEGORY);
    this.setState({showLandingActivity: true});
  }

  setCurrentFragmentMode() {}

  setPickedFragmentMode() {}

  handleToggleTextSkimming() {
    this.setState({
      toggleTextSkimming: !this.state.toggleTextSkimming
    });
  }

  handleToggleFragmentsReceived() {
    this.setState({
      toggleUpdateFragmentsReceived: !this.state.toggleUpdateFragmentsReceived
    });
    console.log("App.js: handleToggleFragmentsReceived");
  }

  handleEditionsReceived() {
    this.setState({editionsReceived: true});
    console.log("handleEditionsReceived()");
  }

  handleEditionSelected(value) {
    console.log(value);
    this.setState({editionSelected: true, currentEdition: value});
  }

  addNewHistoryEntry() {
    console.log("Adding new history entry from next/previous button");

  }

  _onAction(e) {
    //console.log('_onAction', e)
    // console.log('mouseOverMenuButtons: ' + this.state.mouseOverMenuButtons);

    if (window.scrollY < 36 & !this.state.mouseOverMenuButtons & !this.state.hiddenFromIdle) {

      this.setState({opacity: this.opacityShow})
      this.setState({hiddenFromIdle: false});
    } else if (window.scrollY > 36 & !this.state.mouseOverMenuButtons & !this.state.hiddenFromIdle) {

      this.setState({opacity: this.opacityOnText})
      this.setState({hiddenFromIdle: false});
    } else if (this.state.mouseOverMenuButtons) {

      this.setState({opacity: this.opacityShow})
      this.setState({hiddenFromIdle: false});
    }

  }

  _onIdle(e) {
    // console.log('user is idle', e)
    // console.log('last active', this.idleTimer.getLastActiveTime())
    if (this.state.mouseOverMenuButtons) {
      this.setState({opacity: this.opacityShow})
      this.setState({hiddenFromIdle: false});
    } else {
      this.setState({opacity: this.opacityBarelyVisible});
      this.setState({hiddenFromIdle: true});
    }

  }

  previousButtonAction() {

    if (this.props.recommendationIndex > 0) {
      scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
      //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
      let obj;
      let historyVis = this.props.visualizationTechnique;
      let historyCriteria = this.props.semanticCriteria;
      obj = {
        id: this.props.historyEntryCounter,
        originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
        nextFragment: this.props.recommendationArray[this.props.recommendationIndex - 1],
        vis: historyVis,
        criteria: historyCriteria,
        visualization: this.props.currentVisualization,
        recommendationArray: this.props.recommendationArray,
        recommendationIndex: this.props.recommendationIndex - 1,
        fragmentIndex: this.props.fragmentIndex,
        start: new Date().getTime(),
        category: this.props.currentCategory
      };

      this.props.addHistoryEntry(obj);
      this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
      //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
      this.props.setRecommendationIndex(this.props.recommendationIndex - 1)

    }

  }

  nextButtonAction() {
    if (this.props.recommendationIndex < this.props.recommendationArray.length - 1) {
      scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});

      //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
      let obj;
      let historyVis = this.props.visualizationTechnique;
      let historyCriteria = this.props.semanticCriteria;
      obj = {
        id: this.props.historyEntryCounter,
        originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
        nextFragment: this.props.recommendationArray[this.props.recommendationIndex + 1],
        vis: historyVis,
        criteria: historyCriteria,
        visualization: this.props.currentVisualization,
        recommendationArray: this.props.recommendationArray,
        recommendationIndex: this.props.recommendationIndex + 1,
        fragmentIndex: this.props.fragmentIndex,
        start: new Date().getTime(),
        category: this.props.currentCategory
      };

      this.props.addHistoryEntry(obj);
      this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
      //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
      this.props.setRecommendationIndex(this.props.recommendationIndex + 1)

    }
  }

  _onActive(e) {
    // console.log('user is active', e)
    // console.log('time remaining', this.idleTimer.getRemainingTime())

  }

  // listenScrollEvent = e => {
  //
  //   if (window.scrollY > 0 && !this.state.mouseOverMenuButtons) {
  //     this.setState({opacity: 0.7})
  //   } else {
  //     this.setState({opacity: 1})
  //   }
  // }

  handleEditionSelectRetreat() {
    this.props.setAllFragmentsLoaded(false);
    this.setState({editionSelected: false});
    this.landingActivityToRender = <PublicEditionContainerTable onChange={this.handleEditionsReceived} sendSelectedEdition={this.handleEditionSelected}/>
  }

  handleFirstActivitySelectRetreat() {

    this.setState({showLandingActivity: false});
  }

  _handleKeyDown = (event) => {

    var ESCAPE_KEY = 27;

    switch (event.keyCode) {
      case 39: //right key
        this.nextButtonAction();
        break;
      case 37: //left key
        this.previousButtonAction();
        break;
      default:
        break;
    }
  }

  forcePageReload() {
    window.location.reload();
  }

  handlePreviousGoldenButton() {

    scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
    //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    let obj;
    let historyVis = this.props.visualizationTechnique;
    let historyCriteria = this.props.semanticCriteria;
    obj = {
      id: this.props.historyEntryCounter,
      originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
      nextFragment: this.props.recommendationArray[this.previousGoldenSquareIndex],
      vis: historyVis,
      criteria: historyCriteria,
      visualization: this.props.currentVisualization,
      recommendationArray: this.props.recommendationArray,
      recommendationIndex: this.previousGoldenSquareIndex,
      fragmentIndex: this.props.fragmentIndex,
      start: new Date().getTime(),
      category: this.props.currentCategory
    };

    this.props.addHistoryEntry(obj);
    this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
    //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    this.props.setRecommendationIndex(this.previousGoldenSquareIndex);

    // if (this.state.previousGoldenButtonClass !== "goldenButtonInactive") {
    //
    //   let goldenIndex = -1;
    //   let willBePrevious = false;
    //
    //   let i = this.props.recommendationIndex;
    //   for (i; i !== 0; i--) {
    //     console.log("bla1");
    //     if (this.props.recommendationArray[i].meta.categories.includes(this.props.currentCategory) && goldenIndex == -1) {
    //       goldenIndex = i;
    //       console.log("bla2");
    //     } else if (this.props.recommendationArray[i].meta.categories.includes(this.props.currentCategory) && goldenIndex !== -1) {
    //       willBePrevious = true;
    //       i = 0;
    //       console.log("bla3");
    //     }
    //   }
    //
    //   if (goldenIndex !== -1) {
    //     scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
    //     HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    //     let obj;
    //     let historyVis = this.props.visualizationTechnique;
    //     let historyCriteria = this.props.semanticCriteria;
    //     obj = {
    //       id: this.props.historyEntryCounter,
    //       originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
    //       nextFragment: this.props.recommendationArray[goldenIndex],
    //       vis: historyVis,
    //       criteria: historyCriteria,
    //       visualization: this.props.currentVisualization,
    //       recommendationArray: this.props.recommendationArray,
    //       recommendationIndex: goldenIndex,
    //       fragmentIndex: this.props.fragmentIndex,
    //       start: new Date().getTime(),
    //       category: this.props.currentCategory
    //     };
    //
    //     this.props.addHistoryEntry(obj);
    //     this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
    //     HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    //     this.props.setRecommendationIndex(goldenIndex)
    //
    //   }
    //
    //   if (!willBePrevious) {
    //     this.setState({previousGoldenButtonClass: "goldenButtonInactive"})
    //   }
    // }
  }

  handleNextGoldenButton() {
    scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
    //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    let obj;
    let historyVis = this.props.visualizationTechnique;
    let historyCriteria = this.props.semanticCriteria;
    obj = {
      id: this.props.historyEntryCounter,
      originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
      nextFragment: this.props.recommendationArray[this.nextGoldenSquareIndex],
      vis: historyVis,
      criteria: historyCriteria,
      visualization: this.props.currentVisualization,
      recommendationArray: this.props.recommendationArray,
      recommendationIndex: this.nextGoldenSquareIndex,
      fragmentIndex: this.props.fragmentIndex,
      start: new Date().getTime(),
      category: this.props.currentCategory
    };

    this.props.addHistoryEntry(obj);
    this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
    //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
    this.props.setRecommendationIndex(this.nextGoldenSquareIndex);
  }

  componentDidMount() {
    //window.addEventListener('scroll', this.listenScrollEvent)
    document.addEventListener("keydown", this._handleKeyDown);
    try {
      setInterval(async () => {
        this.onAction()
      }, 250);
    } catch (e) {
      console.log(e);
    }
  }

  render() {
    //console.log(new Date().getTime() + " App.js: Rendering")
    let retreatButton;
    const activitySelectable = "Escolher actividade";
    const activityUnselectable = "Actividade indisponível";

    //BUTTON LOGIC
    if (this.props.outOfLandingPage) {
      // console.log("App.js: out of landing page")
      // console.log("App.js: recommendationIndex: " + this.props.recommendationIndex)
      // console.log("App.js: fragmentIndex: " + this.props.fragmentIndex)
      if (this.props.recommendationIndex == 0) {
        // console.log("App.js: changing previous button style to default");
        this.previousFragmentButtonStyle = "default";
        this.nextFragmentButtonStyle = "primary";
      } else if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {
        // console.log("App.js: changing next button style to default");
        this.previousFragmentButtonStyle = "primary";
        this.nextFragmentButtonStyle = "default";
      } else {
        this.previousFragmentButtonStyle = "primary";
        this.nextFragmentButtonStyle = "primary";
        // console.log("App.js: changing both button styles to primary")
      }
    }

    if (!this.state.editionsReceived) {
      this.landingActivityToRender = <PublicEditionContainerTable onChange={this.handleEditionsReceived} sendSelectedEdition={this.handleEditionSelected}/>

    } else if (this.state.editionsReceived && this.state.editionSelected) {

      retreatButton = (<Button bsStyle="primary" onClick={this.handleEditionSelectRetreat}>
        ← Seleccionar outra edição virtual
      </Button>);

      if (!this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<div>
          <img src={loadingGif} alt="loading...app.js" className="loadingGifCentered"/>
          <p align="center">A carregar todos os fragmentos da edição virtual escolhida...</p>
          <p align="center">Se demorar demasiado tempo, actualize a página e volte a tentar.</p>
        </div>);
      } else if (this.state.showLandingActivity & (this.props.potentialSemanticCriteria == CRIT_CATEGORY) & this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<MyWordCloud onChange={this.handleCloseModals}/>)
        retreatButton = (<Button bsStyle="primary" onClick={this.handleFirstActivitySelectRetreat}>
          ← Seleccionar outra actividade
        </Button>);
      } else if (this.state.showLandingActivity & this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<SquareGrid onChange={this.handleCloseModals}/>)
        retreatButton = (<Button bsStyle="primary" onClick={this.handleFirstActivitySelectRetreat}>
          ← Seleccionar outra actividade
        </Button>);
      } else if (!this.state.showLandingActivity & this.props.allFragmentsLoaded) {
        let categoryButtonStyle = "primary"
        let categoryButtonBotMessage = activitySelectable;
        let categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia)"
        let categoryButtonFunction = this.handleShowLandingActivityWordCloudCategory;
        let categoryImage = picWordCloud;
        if (this.props.categories.length === 0) {
          categoryButtonStyle = "default";
          categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia) (edição sem taxonomia)"
          categoryButtonFunction = function() {}
          categoryImage = picWordCloudGray;
          categoryButtonBotMessage = activityUnselectable;
        }
        let datesButtonStyle = "primary"
        let datesButtonBotMessage = activitySelectable;
        let datesButtonFunction = this.handleShowLandingActivitySquareDateOrder;
        let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";
        let datesImage = picSquareTime;
        if (!this.props.datesExist) {
          datesButtonStyle = "default";
          datesButtonBotMessage = activityUnselectable;
          datesButtonFunction = function() {}
          datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data (edição virtual sem datas)"
          datesImage = picSquareTimeGray;
        }
        const options = {
          decodeEntities: true
        }

        let myTitle = ReactHtmlParser(this.state.currentEdition.title, options);
        this.landingActivityToRender = (<div>
          <br/>
          <br/>
          <p align="center">
            Esta é a sua primeira actividade em torno da edição virtual que seleccionou - "{myTitle}". Escolha uma das seguintes opções.
          </p>

          <div className="cardsContainerActivity">

            <div className="cardActivity">
              <div className="containerActivity">
                <img src={picSquare} onClick={this.handleShowLandingActivitySquareEditionOrder} className="cardsActivityImage" alt="Avatar" style={{
                    width: "100%"
                  }}/>
                <p align="center">
                  <b>Explorar os fragmentos por ordem desta edição virtual</b>
                </p>
                <div className="welcomeButtonActivity">
                  <Button bsStyle="primary" bsSize="small" onClick={this.handleShowLandingActivitySquareEditionOrder}>
                    Escolher actividade
                  </Button>
                </div>
              </div>
            </div>

            <div className="cardActivity">
              <div className="containerActivity">
                <img src={datesImage} onClick={datesButtonFunction} className="cardsActivityImage" alt="Avatar" style={{
                    width: "100%"
                  }}/>
                <p align="center">
                  <b>{datesButtonMessage}</b>
                </p>
                <div className="welcomeButtonActivity">
                  <Button bsStyle={datesButtonStyle} bsSize="small" onClick={datesButtonFunction}>
                    {datesButtonBotMessage}
                  </Button>
                </div>
              </div>
            </div>

            <div className="cardActivity">
              <div className="containerActivity">
                <img src={categoryImage} onClick={categoryButtonFunction} className="cardsActivityImage" alt="Avatar" style={{
                    width: "100%"
                  }}/>
                <p align="center">
                  <b>{categoryButtonMessage}</b>
                </p>
                <div className="welcomeButtonActivity">
                  <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={categoryButtonFunction}>
                    {categoryButtonBotMessage}
                  </Button>
                </div>
              </div>
            </div>
          </div>

        </div>)
      }
    }
    let previousNavButton = <div/>;
    let previousNavButtonGold = <div/>;
    let nextNavButton = <div/>;
    let nextNavButtonGold = <div/>;
    let editionTitleToDisplay = <div/>;
    let editionAcronymToDisplay = <div/>;
    if (this.props.allFragmentsLoaded & this.props.outOfLandingPage) {

      editionTitleToDisplay = ("Título da edição virtual seleccionada: " + ReactHtmlParser(this.state.currentEdition.title));
      editionAcronymToDisplay = ("Acrónimo: " + this.state.currentEdition.acronym);

      // console.log("App.js: this.props.visualizationTechnique: " + this.props.visualizationTechnique);
      // console.log("App.js: this.props.semanticCriteria: " + this.props.semanticCriteria);
      // console.log("App.js: this.props.potentialVisualizationTechnique: " + this.props.potentialVisualizationTechnique);
      // console.log("App.js: this.props.potentialSemanticCriteria: " + this.props.potentialSemanticCriteria);
      // console.log("App.js: this.props.currentFragmentMode: " + this.props.currentFragmentMode);

      //http://xahlee.info/comp/unicode_arrows.html
      //← ⟵ ⇦ ⬅ 🡐 🡄 🠘 ← 🠐 🠨 🠬 🠰 🡠 🢀
      //→ ⟶ ⇨ ⮕ 🡒 🡆 🠚 → 🠒 🠪 🠮 🠲 🡢 🢂

      //previousNavButton = <NavigationButton nextButton={false}/>;
      previousNavButton = (<Button bsStyle={this.previousFragmentButtonStyle} bsSize="large" onClick={this.previousButtonAction}>
        ←
      </Button>);

      //nextNavButton = <NavigationButton nextButton={true}/>;
      nextNavButton = (<Button bsStyle={this.nextFragmentButtonStyle} bsSize="large" onClick={this.nextButtonAction}>
        →
      </Button>);

      if (this.props.visualizationTechnique == VIS_SQUARE_GRID) {

        if (this.props.semanticCriteria == CRIT_CATEGORY || this.props.semanticCriteria == CRIT_HETERONYM) {

          // guardar vector com obj = {id: , recommendationIndexGolden: , currentCategory: }
          if (!this.goldenSquareArray[0] && this.goldenSquareArray[0].currentCategory !== this.props.currentCategory) {
            this.goldenSquareArray = [];
            let i;
            for (i = 0; i < this.props.recommendationArray.length; i++) {

              let goldenSelectionCritCondition;
              if (this.props.semanticCriteria == CRIT_HETERONYM) {
                goldenSelectionCritCondition = (this.props.recommendationArray[i].meta.heteronym == this.props.currentCategory);
              } else if (this.props.semanticCriteria == CRIT_CATEGORY) {
                goldenSelectionCritCondition = (this.props.recommendationArray[i].meta.categories.includes(this.props.currentCategory));
              }

              if (goldenSelectionCritCondition) {

                let tempObj = {
                  goldenIndex: i,
                  currentCategory: this.props.currentCategory,
                  goldenId: this.props.recommendationArray[i].interId
                }

                this.goldenSquareArray.push(tempObj);
              }
            }
          }

          let goldenPreviousLabel = "Fragmento anterior do heterónimo seleccionado (" + this.props.currentCategory + ")"
          let goldenNextLabel = "Próximo fragmento do heterónimo seleccionado (" + this.props.currentCategory + ")"

          if (this.props.semanticCriteria == CRIT_CATEGORY) {
            goldenPreviousLabel = "Fragmento anterior da categoria seleccionada (" + this.props.currentCategory + ")"
            goldenNextLabel = "Próximo fragmento da categoria seleccionada (" + this.props.currentCategory + ")"
          }

          let goldenPreviousButtonClass = "goldenButtonInactive"
          let goldenNextButtonClass = "goldenButtonInactive"

          let goldenPreviousButtonFun = function() {}; //this.handlePreviousGoldenButton
          let goldenNextButtonFun = function() {}; //this.handleNextGoldenButton

          //nextGoldenButtonStyle & availabilty verification

          let j;
          for (j = (this.props.recommendationIndex); j < this.props.recommendationArray.length; j++) {
            if (this.props.recommendationArray[j].interId !== this.props.recommendationArray[this.props.recommendationIndex].interId && j <= this.props.recommendationArray.length) {

              let goldenSelectionCritCondition;
              if (this.props.semanticCriteria == CRIT_HETERONYM) {
                goldenSelectionCritCondition = (this.props.recommendationArray[j].meta.heteronym == this.props.currentCategory);
              } else if (this.props.semanticCriteria == CRIT_CATEGORY) {
                goldenSelectionCritCondition = (this.props.recommendationArray[j].meta.categories.includes(this.props.currentCategory));
              }
              if (goldenSelectionCritCondition) {
                goldenNextButtonClass = "goldenButton";
                goldenNextButtonFun = this.handleNextGoldenButton;
                this.nextGoldenSquareIndex = j;
                break;
              }
            }
          }

          //previousGoldenButtonStyle & availabilty verification

          let n;
          for (n = (this.props.recommendationIndex); n !== -1; n--) {
            if (this.props.recommendationArray[n].interId !== this.props.recommendationArray[this.props.recommendationIndex].interId && n >= 0) {

              let goldenSelectionCritCondition;
              if (this.props.semanticCriteria == CRIT_HETERONYM) {
                goldenSelectionCritCondition = (this.props.recommendationArray[n].meta.heteronym == this.props.currentCategory);
              } else if (this.props.semanticCriteria == CRIT_CATEGORY) {
                goldenSelectionCritCondition = (this.props.recommendationArray[n].meta.categories.includes(this.props.currentCategory));
              }
              if (goldenSelectionCritCondition) {
                goldenPreviousButtonClass = "goldenButton";
                goldenPreviousButtonFun = this.handlePreviousGoldenButton;
                this.previousGoldenSquareIndex = n;
                break;
              }
            }
          }

          // end of golden buttons availability verification

          previousNavButtonGold = (<div onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons} className="navPreviousGolden" style={{
              ...styles,
              opacity: this.state.opacity
            }}>
            <OverlayTrigger key='bottom' placement='bottom' overlay={<Tooltip id = {
                `tooltip-$'bottom'`
              } > {
                goldenPreviousLabel
              } < /Tooltip>}>
              <button className={goldenPreviousButtonClass} onClick={goldenPreviousButtonFun}>
                ←
              </button>
            </OverlayTrigger>
          </div>)

          nextNavButtonGold = (<div onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons} className="navNextGolden" style={{
              ...styles,
              opacity: this.state.opacity
            }}>
            <OverlayTrigger key='bottom' placement='bottom' overlay={<Tooltip id = {
                `tooltip-$'bottom'`
              } > {
                goldenNextLabel
              } < /Tooltip>}>
              <button className={goldenNextButtonClass} onClick={goldenNextButtonFun}>
                →
              </button>
            </OverlayTrigger>
          </div>)

        }
      }
      this.buttonToolBarToRender = (<div className="buttonToolbar" style={{
          ...styles,
          opacity: this.state.opacity
        }}>

        <ButtonToolbar onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons}>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowGlobalView}>
            Actividade actual
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowConfig}>
            Nova actividade
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowHistoric}>
            Histórico de leitura
          </Button>

        </ButtonToolbar>

      </div>)
    } else {

      this.buttonToolBarToRender = <div/>;
    }

    let toggleTextSkimmingButtonMessage;
    if (this.state.toggleTextSkimming && this.props.outOfLandingPage) {
      toggleTextSkimmingButtonMessage = "Esconder as palavras mais relevantes deste fragmento";
    } else if (this.props.outOfLandingPage) {
      toggleTextSkimmingButtonMessage = "Destacar as palavras mais relevantes deste fragmento";
    }

    let fragLoader;
    const options = {
      decodeEntities: true
    }

    let myTitle;
    if (this.state.editionSelected) {
      fragLoader = <FragmentLoader currentEdition={this.state.currentEdition} toggleTextSkimming={this.state.toggleTextSkimming} onChange={this.handleToggleFragmentsReceived
}/>

      myTitle = ReactHtmlParser(this.state.currentEdition.title, options);

    }

    return (<div className="app" onWheel={this.onAction}>

      <div>
        <IdleTimer ref={ref => {
            this.idleTimer = ref
          }} element={document} onActive={this.onActive} onIdle={this.onIdle} onAction={this.onAction} debounce={10} timeout={1000 * 3}/> {/* your app here */}
        <Favicon url="https://raw.githubusercontent.com/socialsoftware/edition/ldod-visual/ldod-visual/public/favicon.ico"/>
        <Helmet>
          <link rel="canonical" href="https://ldod.uc.pt/ldod-visual"/>
          <meta property="og:image:width" content="279"/>
          <meta property="og:image:height" content="279"/>
          <meta property="og:description" content="Explore uma das edi&ccedil;&otilde;es virtuais p&uacute;blicas do &quot;Livro do Desassossego&quot; dispon&iacute;veis no Arquivo LdoD"/>
          <meta property="og:title" content="LdoD Visual"/>
          <meta property="og:url" content="https://ldod.uc.pt/ldod-visual"/>
          <meta property="og:image" content="https://raw.githubusercontent.com/socialsoftware/edition/ldod-visual/ldod-visual/public/og-image.jpg"/>
          <meta property="og:site_name" content="https://ldod.uc.pt/ldod-visual"/>
        </Helmet>
      </div>

      {this.buttonToolBarToRender}

      <div className="toggleTextSkimming" style={{
          ...styles,
          opacity: this.state.opacity
        }}>

        <Button bsStyle="primary" bsSize="large" onClick={this.handleToggleTextSkimming} onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons}>
          {toggleTextSkimmingButtonMessage}
        </Button>

      </div>

      <div >

        <div onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons} className="navPrevious" style={{
            ...styles,
            opacity: this.state.opacity
          }}>
          {previousNavButton}
        </div>

        <div onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons} className="navNext" style={{
            ...styles,
            opacity: this.state.opacity
          }}>
          {nextNavButton}
        </div>

        {previousNavButtonGold}

        {nextNavButtonGold}

        <div className="appfrag" onMouseOver={this.setMouseOutMenuButtons} /*onMouseLeave={this.setMouseOverMenuButtons}*/

        >
          {fragLoader}
        </div>

        <p/>

        <p align="center" style={{
            // ...styles,
            // opacity: this.state.opacity,
            color: 'white',
            fontSize: 13
          }}>{editionTitleToDisplay}</p>

        <p align="center" style={{
            // ...styles,
            // opacity: this.state.opacity,
            color: 'white',
            fontSize: 13
          }}>{editionAcronymToDisplay}</p>

      </div>

      <Modal show={this.state.showLanding} dialogClassName="custom-modal">
        <Modal.Header>
          <Modal.Title></Modal.Title>
        </Modal.Header>

        <Modal.Body>

          <div className="landing-activity-style">{this.landingActivityToRender}</div>
        </Modal.Body>

        <Modal.Footer>
          {retreatButton}
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showGlobalView} onHide={this.handleCloseGlobalView} dialogClassName="custom-modal">
        <Modal.Header closeButton="closeButton">
          <Modal.Title>
            Actividade Actual
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          {this.props.currentVisualization}
        </Modal.Body>

        <Modal.Footer>
          <Button bsStyle="primary" onClick={this.handleCloseGlobalView}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showConfig} onHide={this.handleCloseConfig} dialogClassName="custom-modal">
        <Modal.Header closeButton="closeButton">
          <Modal.Title>
            Nova Actividade
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <ActivityMenu onChange={this.handleCloseModals}/>
        </Modal.Body>

        <Modal.Footer>
          <Button bsStyle="primary" onClick={this.forcePageReload}>
            Escolher outra edição virtual
          </Button>
          <Button bsStyle="primary" onClick={this.handleCloseConfig}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showHistoric} onHide={this.handleCloseHistoric} dialogClassName="custom-modal">
        <Modal.Header closeButton="closeButton">
          <Modal.Title>
            Histórico de leitura
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <HistoryMenu onChange={this.handleCloseModals}/>
        </Modal.Body>

        <Modal.Footer>
          <Button bsStyle="primary" onClick={this.handleCloseHistoric}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>);
  }
}

const App = connect(mapStateToProps, mapDispatchToProps)(ConnectedApp);

export default App;
