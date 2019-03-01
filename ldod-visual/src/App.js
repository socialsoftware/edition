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
  setHistoryEntryCounter
} from "./actions/index";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
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
  CRIT_CATEGORY
} from "./constants/history-transitions";
import NetworkGraph from "./components/NetworkGraph";
import FragmentLoader from "./components/FragmentLoader";
import SquareGrid from "./components/SquareGrid";
import MyWordCloud from "./components/MyWordCloud";
import PublicEditionContainer from "./containers/PublicEditionContainer";
import ReactHtmlParser, {processNodes, convertNodeToElement, htmlparser2} from 'react-html-parser';
import loadingGif from './assets/loading.gif';
import loadingFragmentsGif from './assets/fragmentload.gif';
import IdleTimer from 'react-idle-timer';

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
    datesExist: state.datesExist
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
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter))
  };
};

const styles = {
  transition: 'all 0.3s ease-out'
};

class ConnectedApp extends Component {
  constructor(props) {
    super(props);

    this.buttonToolBarToRender = <div/>

    this.idleTimer = null
    this.onAction = this._onAction.bind(this)
    this.onActive = this._onActive.bind(this)
    this.onIdle = this._onIdle.bind(this)

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
      mouseOverMenuButtons: false
    };

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
  }

  handleCloseConfig() {
    this.setState({showConfig: false});
  }

  handleShowConfig() {
    this.setState({showConfig: true});
    this.props.setCurrentFragmentMode(true);
    console.log("App.handleShowConfig: setting currentFragmentMode to true")

  }

  handleCloseGlobalView() {
    this.setState({showGlobalView: false});
  }

  handleShowGlobalView() {
    this.setState({showGlobalView: true});
    this.props.setCurrentFragmentMode(false);
    console.log("App.handleShowGlobalView(): setting currentFragmentMode to false (PickedFragmentMode)")

  }

  handleCloseHistoric() {
    this.setState({showHistoric: false});
  }

  handleShowHistoric() {
    this.props.setCurrentFragmentMode(true);
    this.setState({showHistoric: true});
  }

  handleCloseLanding() {
    this.setState({showLanding: false});
    console.log("calling handlecloselanding()")
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
    console.log('user did something', e)
    if (window.scrollY == 0 && !this.state.mouseOverMenuButtons) {
      this.setState({opacity: 1})
    } else if (window.scrollY > 0 && !this.state.mouseOverMenuButtons) {
      this.setState({opacity: 0.7})
    }

  }

  _onActive(e) {
    console.log('user is active', e)
    console.log('time remaining', this.idleTimer.getRemainingTime())

  }

  _onIdle(e) {
    console.log('user is idle', e)
    console.log('last active', this.idleTimer.getLastActiveTime())
    if (this.state.mouseOverMenuButtons) {
      this.setState({opacity: 1})
    } else {
      this.setState({opacity: 0});
    }

  }

  // listenScrollEvent = e => {
  //
  //   if (window.scrollY > 0 && !this.state.mouseOverMenuButtons) {
  //     this.setState({opacity: 0.7})
  //   } else {
  //     this.setState({opacity: 1})
  //   }
  // }

  componentDidMount() {
    //window.addEventListener('scroll', this.listenScrollEvent)
  }

  render() {
    console.log(new Date().getTime() + " App.js: Rendering")

    //BUTTON LOGIC
    if (this.props.outOfLandingPage) {
      console.log("App.js: out of landing page")
      console.log("App.js: recommendationIndex: " + this.props.recommendationIndex)
      console.log("App.js: fragmentIndex: " + this.props.fragmentIndex)
      if (this.props.recommendationIndex == 0) {
        console.log("App.js: changing previous button style to default");
        this.previousFragmentButtonStyle = "default";
      } else if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {
        console.log("App.js: changing next button style to default");
        this.nextFragmentButtonStyle = "default";
      } else {
        this.previousFragmentButtonStyle = "primary";
        this.nextFragmentButtonStyle = "primary";
        console.log("App.js: changing both button styles to primary")
      }
    }

    if (!this.state.editionsReceived) {
      this.landingActivityToRender = <PublicEditionContainer onChange={this.handleEditionsReceived} sendSelectedEdition={this.handleEditionSelected}/>

    } else if (this.state.editionsReceived && this.state.editionSelected) {

      if (!this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<div>
          <img src={loadingGif} alt="loading..." className="loadingGifCentered"/>
          <p align="center">A carregar todos os fragmentos da edição virtual escolhida...</p>
          <p align="center">Se demorar demasiado tempo, actualize a página e volte a tentar.</p>
        </div>);
      } else if (this.state.showLandingActivity & (this.props.potentialSemanticCriteria == CRIT_CATEGORY) & this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<MyWordCloud onChange={this.handleCloseModals}/>)
      } else if (this.state.showLandingActivity & this.props.allFragmentsLoaded) {
        this.landingActivityToRender = (<SquareGrid onChange={this.handleCloseModals}/>)
      } else if (!this.state.showLandingActivity & this.props.allFragmentsLoaded) {
        let categoryButtonStyle = "primary"
        let categoryButtonFunction = this.handleShowLandingActivityWordCloudCategory;
        if (this.props.categories.length === 0) {
          categoryButtonStyle = "secondary";
          categoryButtonFunction = function() {}
        }
        let datesButtonStyle = "primary"
        let datesButtonFunction = this.handleShowLandingActivitySquareDateOrder;
        let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";
        if (!this.props.datesExist) {
          datesButtonStyle = "secondary";
          datesButtonFunction = function() {}
          datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data (edição virtual sem datas)"
        }
        this.landingActivityToRender = (<div>
          <p>
            Esta é a sua primeira actividade. Escolha uma as seguintes opções.
          </p>

          <ButtonToolbar >

            <Button bsStyle="primary" bsSize="large" onClick={this.handleShowLandingActivitySquareEditionOrder} block="block">
              Explorar os fragmentos por ordem desta edição virtual
            </Button>

            <Button bsStyle={datesButtonStyle} bsSize="large" onClick={datesButtonFunction} block="block">
              {datesButtonMessage}
            </Button>

            <Button bsStyle={categoryButtonStyle} bsSize="large" onClick={categoryButtonFunction} block="block">
              Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia)
            </Button>

          </ButtonToolbar>
        </div>)
      }
    }

    let previousNavButton = <div/>;

    let nextNavButton = <div/>;

    if (this.props.allFragmentsLoaded & this.props.outOfLandingPage) {

      console.log("App.js: this.props.visualizationTechnique: " + this.props.visualizationTechnique);
      console.log("App.js: this.props.semanticCriteria: " + this.props.semanticCriteria);
      console.log("App.js: this.props.potentialVisualizationTechnique: " + this.props.potentialVisualizationTechnique);
      console.log("App.js: this.props.potentialSemanticCriteria: " + this.props.potentialSemanticCriteria);
      console.log("App.js: this.props.currentFragmentMode: " + this.props.currentFragmentMode);

      previousNavButton = <NavigationButton nextButton={false}/>;

      nextNavButton = <NavigationButton nextButton={true}/>;

      this.buttonToolBarToRender = (<div className="buttonToolbar" style={{
          ...styles,
          opacity: this.state.opacity
        }}>

        <ButtonToolbar onMouseOver={this.setMouseOverMenuButtons} onMouseLeave={this.setMouseOutMenuButtons}>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowGlobalView}>
            Actividade Actual
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowConfig}>
            Nova Actividade
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

    return (<div className="app">

      <div>
        <IdleTimer ref={ref => {
            this.idleTimer = ref
          }} element={document} onActive={this.onActive} onIdle={this.onIdle} onAction={this.onAction} debounce={250} timeout={1000 * 2}/> {/* your app here */}
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

        <div className="appfrag">
          {fragLoader}
        </div>

        <p/>

        <p align="center" style={{
            ...styles,
            opacity: this.state.opacity,
            color: 'white',
            fontSize: 12
          }}>Título da edição virtual seleccionada: {myTitle}</p>

        <p align="center" style={{
            ...styles,
            opacity: this.state.opacity,
            color: 'white',
            fontSize: 12
          }}>Acrónimo: {this.state.currentEdition.acronym}</p>

      </div>

      <Modal show={this.state.showLanding} dialogClassName="custom-modal">
        <Modal.Header>
          <Modal.Title>
            Bem-vindo
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>

          <div className="landing-activity-style">{this.landingActivityToRender}</div>
        </Modal.Body>

        <Modal.Footer></Modal.Footer>
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
