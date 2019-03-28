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
  setAllFragmentsLoaded,
  setOutOfLandingPage
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

import availablePreviousArrow from './assets/available-previous.png'
import availableNextArrow from './assets/available-next.png'
import unavailablePreviousArrow from './assets/unavailable-previous.png'
import unavailableNextArrow from './assets/unavailable-next.png'

import availableNextArrowGolden from './assets/goldnav/available-next-golden.png'
import availablePreviousArrowGolden from './assets/goldnav/available-previous-golden.png'
import unavailablePreviousArrowGolden from './assets/goldnav/unavailable-previous-golden.png'
import unavailableNextArrowGolden from './assets/goldnav/unavailable-next-golden.png'

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
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage))
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
      nextGoldenButtonClass: "goldenButtonNext",
      showInstructions: false,
      showReadingMenuIntructions: false,
      inReadingMenu: true
    };

    this.opacityHide = 0;
    this.opacityShow = 1;
    this.opacityOnText = 0.6;
    this.opacityBarelyVisible = 0; //antes estava 0.3. ou 0.1

    this.categoryButtonFunction = function() {};
    this.datesButtonFunction = function() {};

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

    this.toggleInstructions = this.toggleInstructions.bind(this);

    this.goBackToTop = this.goBackToTop.bind(this);

    this.toggleShowReadingMenuInstructions = this.toggleShowReadingMenuInstructions.bind(this);

    this.navArrowWidth = "100px";

    this.availablePreviousArrowButton = <img src={availablePreviousArrow} onClick={this.nextButtonAction} style={{
        width: this.navArrowWidth,
        cursor: "pointer"
      }}/>
    this.unavailablePreviousArrowButton = <img src={unavailablePreviousArrow} style={{
        width: this.navArrowWidth
      }}/>

    this.availableNextArrowButton = <img src={availableNextArrow} onClick={this.nextButtonAction} style={{
        width: this.navArrowWidth,
        cursor: "pointer"
      }}/>
    this.unavailableNextArrowButton = <img src={unavailableNextArrow} style={{
        width: this.navArrowWidth
      }}/>

    this.previousFragmentButtonStyle = this.availablePreviousArrowButton
    this.nextFragmentButtonStyle = this.availableNextArrowButton

    this.previousFragmentButtonStyle = this.availablePreviousArrowButton
    this.nextFragmentButtonStyle = this.availableNextArrowButton

  }

  toggleShowReadingMenuInstructions() {
    this.setState({
      showReadingMenuIntructions: !this.state.showReadingMenuIntructions
    });
  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions
    });
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
    this.setState({
      showConfig: false,
      showGlobalView: false,
      showLanding: false,
      showHistoric: false,
      inReadingMenu: true,
      hiddenFromIdle: false,
      opacity: this.opacityShow
    });
    scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
  }

  handleCloseConfig() {
    this.setState({showConfig: false, opacity: this.opacityShow, hiddenFromIdle: false, inReadingMenu: true});
  }

  handleShowConfig() {
    this.setState({showConfig: true, inReadingMenu: false});
    this.props.setCurrentFragmentMode(true);
    console.log("App.handleShowConfig: setting currentFragmentMode to true")
  }

  handleCloseGlobalView() {
    this.setState({showGlobalView: false, hiddenFromIdle: false, inReadingMenu: true, opacity: this.opacityShow});

  }

  handleShowGlobalView() {
    this.setState({showGlobalView: true, inReadingMenu: false});
    this.props.setCurrentFragmentMode(false);
    console.log("App.handleShowGlobalView(): setting currentFragmentMode to false (PickedFragmentMode)")
  }

  handleCloseHistoric() {
    this.setState({showHistoric: false, opacity: this.opacityShow, hiddenFromIdle: false, inReadingMenu: true});
  }

  handleShowHistoric() {
    this.props.setCurrentFragmentMode(true);
    this.setState({showHistoric: true, inReadingMenu: false});
  }

  handleCloseLanding() {
    this.setState({showLanding: false, hiddenFromIdle: false, inReadingMenu: true, opacity: this.opacityShow});
    console.log("calling handlecloselanding()");

  }

  handleShowLandingActivitySquareEditionOrder() {
    console.log("app.js: handleShowLandingActivitySquareEditionOrder()");
    this.props.setCurrentFragmentMode(true);
    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_EDITION_ORDER);
    this.setState({showLandingActivity: true});
  }

  handleShowLandingActivitySquareDateOrder() {
    console.log("app.js: handleShowLandingActivitySquareDateOrder()");
    if (this.props.datesExist) {
      this.props.setCurrentFragmentMode(true);
      this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
      this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
      this.setState({showLandingActivity: true});
    }
  }

  handleShowLandingActivityWordCloudCategory() {
    console.log("app.js: handleShowLandingActivityWordCloudCategory()");
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

      this.setState({opacity: this.opacityShow, hiddenFromIdle: false})
    } else if (window.scrollY > 36 & !this.state.mouseOverMenuButtons & !this.state.hiddenFromIdle) {

      this.setState({opacity: this.opacityOnText, hiddenFromIdle: false})
    } else if (this.state.mouseOverMenuButtons) {

      this.setState({opacity: this.opacityShow, hiddenFromIdle: false})
    }

  }

  _onIdle(e) {
    // console.log('user is idle', e)
    // console.log('last active', this.idleTimer.getLastActiveTime())
    if (this.state.mouseOverMenuButtons) {
      this.setState({opacity: this.opacityShow, hiddenFromIdle: false})
    } else {
      this.setState({opacity: this.opacityBarelyVisible, hiddenFromIdle: true});
    }

  }

  goBackToTop() {
    scroll.scrollToTop({duration: 500, delay: 0, smooth: 'easeInOutQuart'});
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
    this.props.setOutOfLandingPage(false);
    this.setState({editionSelected: false});
    this.landingActivityToRender = <PublicEditionContainerTable onChange={this.handleEditionsReceived} sendSelectedEdition={this.handleEditionSelected}/>
  }

  handleFirstActivitySelectRetreat() {

    this.setState({showLandingActivity: false});
  }

  _handleKeyDown = (event) => {

    var ESCAPE_KEY = 27;
    var RIGHT_ARROW = 39;
    var LEFT_ARROW = 37;
    var A_KEY = 65;
    var N_KEY = 78;
    var H_KEY = 72;
    var E_KEY = 69;
    var D_KEY = 68;
    var T_KEY = 84;
    var C_KEY = 67;
    var X_KEY = 88;
    var O_KEY = 79;
    var R_KEY = 82;
    var I_KEY = 73;

    if (this.props.outOfLandingPage && this.state.inReadingMenu) {

      switch (event.keyCode) {
        case RIGHT_ARROW:
          this.nextButtonAction();
          break;
        case LEFT_ARROW:
          this.previousButtonAction();
          break;
        case A_KEY:
          this.handleShowGlobalView();
          break;
        case N_KEY:
          this.handleShowConfig();
          break;
        case H_KEY:
          this.handleShowHistoric();
          break;
        case R_KEY:
          this.handleToggleTextSkimming();
          break;
        case I_KEY:
          this.toggleShowReadingMenuInstructions();
          break;
        case T_KEY:
          this.goBackToTop();
        default:
          break;
      }
    }
    // else if (!this.props.outOfLandingPage && this.props.allFragmentsLoaded && this.state.editionSelected) {
    //   console.log("app.js | event.KeyCode: " + event.keyCode)
    //   switch (event.keyCode) {
    //     case E_KEY:
    //       console.log("app.js | event.KeyCode: E")
    //       this.handleShowLandingActivitySquareEditionOrder();
    //       break;
    //     case D_KEY:
    //       console.log("app.js | event.KeyCode: D")
    //       this.datesButtonFunction();
    //       break;
    //     case T_KEY:
    //       console.log("app.js | event.KeyCode: T")
    //       this.categoryButtonFunction();
    //       break;
    //     default:
    //       break;
    //   }
    // }

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
        this.previousFragmentButtonStyle = this.unavailablePreviousArrowButton;
        this.nextFragmentButtonStyle = this.availableNextArrowButton;
      } else if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {
        // console.log("App.js: changing next button style to default");
        this.previousFragmentButtonStyle = this.availablePreviousArrowButton;
        this.nextFragmentButtonStyle = this.unavailableNextArrowButton;
      } else {
        this.previousFragmentButtonStyle = this.availablePreviousArrowButton;
        this.nextFragmentButtonStyle = this.availableNextArrowButton;
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
        this.categoryButtonFunction = this.handleShowLandingActivityWordCloudCategory;
        let categoryImage = picWordCloud;
        if (this.props.categories.length === 0) {
          categoryButtonStyle = "default";
          categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia) (edição sem taxonomia)"
          this.categoryButtonFunction = function() {}
          categoryImage = picWordCloudGray;
          categoryButtonBotMessage = activityUnselectable;
        }
        let datesButtonStyle = "primary"
        let datesButtonBotMessage = activitySelectable;
        this.datesButtonFunction = this.handleShowLandingActivitySquareDateOrder;
        let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";
        let datesImage = picSquareTime;
        if (!this.props.datesExist) {
          datesButtonStyle = "default";
          datesButtonBotMessage = activityUnselectable;
          this.datesButtonFunction = function() {}
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
                <img src={datesImage} onClick={this.datesButtonFunction} className="cardsActivityImage" alt="Avatar" style={{
                    width: "100%"
                  }}/>
                <p align="center">
                  <b>{datesButtonMessage}</b>
                </p>
                <div className="welcomeButtonActivity">
                  <Button bsStyle={datesButtonStyle} bsSize="small" onClick={this.datesButtonFunction}>
                    {datesButtonBotMessage}
                  </Button>
                </div>
              </div>
            </div>

            <div className="cardActivity">
              <div className="containerActivity">
                <img src={categoryImage} onClick={this.categoryButtonFunction} className="cardsActivityImage" alt="Avatar" style={{
                    width: "100%"
                  }}/>
                <p align="center">
                  <b>{categoryButtonMessage}</b>
                </p>
                <div className="welcomeButtonActivity">
                  <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={this.categoryButtonFunction}>
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
    let changeEdButton = <div/>;
    let readingMenuIntructions = <div/>;
    let progressBar = <div/>;
    let goBackToTopButton = <div/>;
    let textSkimmingButtonJsx = <div/>;
    if (this.props.allFragmentsLoaded && this.props.outOfLandingPage) {

      let toggleTextSkimmingButtonMessage;
      if (this.state.toggleTextSkimming && this.props.outOfLandingPage) {
        toggleTextSkimmingButtonMessage = "Esconder palavras mais relevantes [R]";
      } else if (this.props.outOfLandingPage) {
        toggleTextSkimmingButtonMessage = "Destacar palavras mais relevantes [R]";
      }

      textSkimmingButtonJsx = (<Button bsStyle="default" bsSize="small" onClick={this.handleToggleTextSkimming} onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons}>
        {toggleTextSkimmingButtonMessage}
      </Button>)

      editionTitleToDisplay = ("Título da edição virtual seleccionada: " + ReactHtmlParser(this.state.currentEdition.title));
      editionAcronymToDisplay = ("Acrónimo: " + this.state.currentEdition.acronym);
      changeEdButton = (<Button bsStyle="default" bsSize="small" onClick={this.forcePageReload}>
        Escolher outra edição virtual
      </Button>)
      readingMenuIntructions = (<Button bsStyle="default" bsSize="small" onClick={this.toggleShowReadingMenuInstructions}>
        Instrucções [i]
      </Button>)
      progressBar = ((<div className="progress-bar">
        <div className="filler" style={{
            width: `${ ((this.props.recommendationIndex + 1) / this.props.recommendationArray.length) * 100}%`
          }}/>
      </div>))
      goBackToTopButton = <Button bsStyle="default" bsSize="small" onClick={this.goBackToTop}>
        Voltar ao topo [T]
      </Button>
    }

    if (this.props.allFragmentsLoaded & this.props.outOfLandingPage) {

      // console.log("App.js: this.props.visualizationTechnique: " + this.props.visualizationTechnique);
      // console.log("App.js: this.props.semanticCriteria: " + this.props.semanticCriteria);
      // console.log("App.js: this.props.potentialVisualizationTechnique: " + this.props.potentialVisualizationTechnique);
      // console.log("App.js: this.props.potentialSemanticCriteria: " + this.props.potentialSemanticCriteria);
      // console.log("App.js: this.props.currentFragmentMode: " + this.props.currentFragmentMode);

      //http://xahlee.info/comp/unicode_arrows.html
      //← ⟵ ⇦ ⬅ 🡐 🡄 🠘 ← 🠐 🠨 🠬 🠰 🡠 🢀
      //→ ⟶ ⇨ ⮕ 🡒 🡆 🠚 → 🠒 🠪 🠮 🠲 🡢 🢂

      //previousNavButton = <NavigationButton nextButton={false}/>;
      previousNavButton = this.previousFragmentButtonStyle;

      //nextNavButton = <NavigationButton nextButton={true}/>;
      nextNavButton = this.nextFragmentButtonStyle;

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

          this.navArrowWidthGolden = "5px";

          this.availablePreviousArrowButtonGolden = <img src={availablePreviousArrowGolden} onClick={goldenPreviousButtonFun} style={{
              width: this.navArrowWidthGolden,
              cursor: "pointer"
            }}/>
          this.unavailablePreviousArrowButtonGolden = <img src={unavailablePreviousArrowGolden} style={{
              width: this.navArrowWidthGolden
            }}/>

          this.availableNextArrowButtonGolden = <img src={availableNextArrowGolden} onClick={goldenPreviousButtonFun} style={{
              width: this.navArrowWidthGolden,
              cursor: "pointer"
            }}/>
          this.unavailableNextArrowButtonGolden = <img src={unavailableNextArrowGolden} style={{
              width: this.navArrowWidthGolden
            }}/>

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

          <Button bsStyle="default" bsSize="small" onClick={this.handleShowGlobalView}>
            Actividade actual [A]
          </Button>

          <Button bsStyle="default" bsSize="small" onClick={this.handleShowConfig}>
            Nova actividade [N]
          </Button>

          <Button bsStyle="default" bsSize="small" onClick={this.handleShowHistoric}>
            Histórico de leitura [H]
          </Button>

          {textSkimmingButtonJsx}

        </ButtonToolbar>

      </div>)
    } else {

      this.buttonToolBarToRender = <div/>;
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

        <div className="metaInfo" onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons} tyle={{
            ...styles,
            opacity: this.state.opacity
          }}>

          <p align="center" style={{
              ...styles,
              opacity: this.state.opacity,
              color: 'black',
              fontSize: 15
            }}>{editionTitleToDisplay}</p>

          <p align="center" style={{
              ...styles,
              opacity: this.state.opacity,
              color: 'black',
              fontSize: 15
            }}>{editionAcronymToDisplay}</p>
          <br/>
          <p align="center" style={{
              ...styles,
              opacity: this.state.opacity,
              color: 'black',
              fontSize: 15,
              fontFamily: 'Arial'
            }}>{readingMenuIntructions}
            {goBackToTopButton}{changeEdButton}</p>

        </div>

        {progressBar}

      </div>

      <Modal show={this.state.showLanding} dialogClassName="custom-modal">

        <Modal.Body>
          <div className="landing-activity-style">{this.landingActivityToRender}</div>
        </Modal.Body>

        <Modal.Footer>
          {retreatButton}
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showGlobalView} onHide={this.handleCloseGlobalView} dialogClassName="custom-modal">

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

        <Modal.Body>
          <HistoryMenu onChange={this.handleCloseModals}/>
        </Modal.Body>

        <Modal.Footer>
          <Button bsStyle="primary" onClick={this.handleCloseHistoric}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showReadingMenuIntructions} onHide={this.toggleShowReadingMenuInstructions} dialogClassName="custom-modal-instructions">
        <Modal.Header closeButton="closeButton">
          <Modal.Title align="center">
            Instruções do menu de leitura
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <div className="readingMoreInfo">
            <p align="center">Este é o menu de leitura. Aqui poderá:</p>
            <lu>
              <li>
                Ir para o menu da actividade actual e interagir com a mesma clicando no botão ou na tecla "A" do seu teclado.
              </li>
              <li>
                Ir para o menu de nova actividade clicando no botão ou na tecla "N" do seu teclado.
              </li>
              <li>
                Ir para o menu de histórico de leitura clicando no botão ou na tecla "H" do seu teclado.
              </li>
              <li>
                Realçar as palavras mais relevantes do fragmento clicando no botão ou na tecla "R" do seu teclado. O critério do realce é dado pelo TF-IDF ou{" "}
                <i>term frequency–inverse document frequency.</i>
              </li>
              <li>
                Utilizar os botões azuis com as setas da esquerda ou da direita para ir para o fragmento anterior ou seguinte no contexto da actividade actual. Também pode usar as teclas esquerda e direita do seu teclado para o mesmo efeito.
              </li>
              <li>
                Ao fazer uma actividade que envolva a selecção de uma certa categoria ou heterónimo, irão surgir novos botões amarelos semelhantes aos botões azuis das setas. Poderá utilizar estes botões amarelos para navegar exclusivamente entre os fragmentos dessa categoria ou heterónimo seleccionados.
              </li>
            </lu>
          </div>
        </Modal.Body>

        <Modal.Footer>
          <Button bsStyle="default" bsSize="small" onClick={this.toggleShowReadingMenuInstructions}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>);
  }
}

const App = connect(mapStateToProps, mapDispatchToProps)(ConnectedApp);

export default App;
