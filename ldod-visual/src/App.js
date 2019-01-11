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
  setVisualizationTechnique
} from "./actions/index";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import ActivityMenu from "./components/ActivityMenu";
import HistoryMenu from "./components/HistoryMenu";
import {
  VIS_NETWORK,
  BY_HISTORIC,
  BY_NEXTBUTTON,
  BY_PREVIOUSBUTTON,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  VIS_SQUARE_GRID
} from "./constants/history-transitions";
import NetworkGraph from "./components/NetworkGraph";
import FragmentLoader from "./components/FragmentLoader";
import SquareGrid from "./components/SquareGrid";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    allFragmentsLoaded: state.allFragmentsLoaded,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    outOfLandingPage: state.outOfLandingPage,
    currentFragmentMode: state.currentFragmentMode
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
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria))
  };
};

class ConnectedApp extends Component {
  constructor(props) {
    super(props);

    this.previousFragmentButtonStyle = "primary";
    this.nextFragmentButtonStyle = "primary";

    this.state = {
      previousFragmentButtonStyle: "primary",
      nextFragmentButtonStyle: "primary",
      showConfig: false,
      showGlobalView: false,
      showHistoric: false,
      showLanding: true,
      showLandingActivity: false
    };

    this.landingActivityToRender = <div/>;

    this.handleShowGlobalView = this.handleShowGlobalView.bind(this);
    this.handleCloseGlobalView = this.handleCloseGlobalView.bind(this);

    this.handleShowConfig = this.handleShowConfig.bind(this);
    this.handleCloseConfig = this.handleCloseConfig.bind(this);

    this.handleShowHistoric = this.handleShowHistoric.bind(this);
    this.handleCloseHistoric = this.handleCloseHistoric.bind(this);

    this.handleCloseModals = this.handleCloseModals.bind(this);
    this.handleCloseLanding = this.handleCloseLanding.bind(this);

    //recommendationArray
    this.handleClickPrevious = this.handleClickPrevious.bind(this);
    this.handleClickNext = this.handleClickNext.bind(this);

    //show landing activities
    this.handleShowLandingActivitySquareEditionOrder = this.handleShowLandingActivitySquareEditionOrder.bind(this);
    this.handleShowLandingActivitySquareDateOrder = this.handleShowLandingActivitySquareDateOrder.bind(this);
  }

  handleClickPrevious() {
    if (this.props.recommendationIndex === 1) {
      this.previousFragmentButtonStyle = "default";
    }
    if (this.props.recommendationIndex > 0) {
      this.nextFragmentButtonStyle = "primary";
      this.props.setRecommendationIndex(this.props.recommendationIndex - 1)
    }
  }

  handleClickNext() {
    if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {
      this.nextFragmentButtonStyle = "default";
    }
    if (this.props.recommendationIndex < this.props.recommendationArray.length - 1) {
      this.props.setRecommendationIndex(this.props.recommendationIndex + 1)
      this.previousFragmentButtonStyle = "primary";
    }
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
    this.setState({showHistoric: true});
  }

  handleCloseLanding() {
    this.setState({showLanding: false});
    console.log("calling handlecloselanding()")
  }

  handleShowLandingActivitySquareEditionOrder() {
    this.props.setSemanticCriteria(CRIT_EDITION_ORDER);
    this.props.setVisualizationTechnique(VIS_SQUARE_GRID);
    this.setState({showLandingActivity: true});
  }

  handleShowLandingActivitySquareDateOrder() {
    this.props.setSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
    this.props.setVisualizationTechnique(VIS_SQUARE_GRID);
    this.setState({showLandingActivity: true});
  }

  setCurrentFragmentMode() {}

  setPickedFragmentMode() {}

  render() {
    console.log("rendering app.js")

    //BUTTON LOGIC
    if (this.props.outOfLandingPage) {
      console.log("out of landing page")
      console.log("recommendationIndex: " + this.props.recommendationIndex)
      console.log("fragmentIndex: " + this.props.fragmentIndex)
      if (this.props.recommendationIndex == 0) {
        console.log("changing previous button style to default");
        this.previousFragmentButtonStyle = "default";
      } else if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {
        console.log("changing next button style to default");
        this.nextFragmentButtonStyle = "default";
      } else {
        this.previousFragmentButtonStyle = "primary";
        this.nextFragmentButtonStyle = "primary";
        console.log("changing both button styles to primary")
      }
    }

    if (this.state.showLandingActivity & this.props.allFragmentsLoaded) {
      this.landingActivityToRender = (<SquareGrid onChange={this.handleCloseModals}/>)
    } else {
      this.landingActivityToRender = (<div className="buttonToolbarStart">
        <ButtonToolbar>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowLandingActivitySquareEditionOrder} block="block">
            Explorar os fragmentos por ordem desta edição virtual
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowLandingActivitySquareDateOrder} block="block">
            Explorar os fragmentos desta edição ordenados por data
          </Button>

        </ButtonToolbar>
      </div>)
    }

    let buttonToolBarToRender;

    if (this.props.allFragmentsLoaded) {

      //<SquareGrid onChange={this.handleCloseModals}/>;

      buttonToolBarToRender = (<div className="buttonToolbar">
        <ButtonToolbar>

          <Button bsStyle={this.previousFragmentButtonStyle} bsSize="large" onClick={this.handleClickPrevious}>
            Anterior
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowGlobalView}>
            Actividade Actual
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowConfig}>
            Nova Actividade
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowHistoric}>
            Histórico de leitura
          </Button>

          <Button bsStyle={this.nextFragmentButtonStyle} bsSize="large" onClick={this.handleClickNext}>
            Próximo
          </Button>

        </ButtonToolbar>
      </div>)

    } else {

      buttonToolBarToRender = <div/>;
    }
    return (<div className="app">

      <FragmentLoader/> {buttonToolBarToRender}

      <div>
        <Fragment/>
      </div>

      <Modal show={this.state.showLanding} dialogClassName="custom-modal-landing">
        <Modal.Header>
          <Modal.Title>
            Bem-vindo
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>
            Esta é a sua primeira actividade. Escolha uma as seguintes opções.
          </p>
          {this.landingActivityToRender}
        </Modal.Body>

      </Modal>

      <Modal show={this.state.showGlobalView} onHide={this.handleCloseGlobalView} dialogClassName="custom-modal-landing">
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
