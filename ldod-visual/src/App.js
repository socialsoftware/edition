import React, {Component} from "react";
import "./App.css";
import Fragment from "./components/Fragment";
import {setFragmentIndex, addHistoryEntry} from "./actions/index";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import ActivityMenu from "./components/ActivityMenu";
import HistoryMenu from "./components/HistoryMenu";
import {VIS_NETWORK, BY_HISTORIC, BY_NEXTBUTTON, BY_PREVIOUSBUTTON} from "./constants/history-transitions";
import NetworkGraph from "./components/NetworkGraph";
import FragmentLoader from "./components/FragmentLoader";
import SquareGrid from "./components/SquareGrid";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history, allFragmentsLoaded: state.allFragmentsLoaded};
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry))
  };
};

class ConnectedApp extends Component {
  constructor(props) {
    super(props);

    this.state = {
      previousFragmentButtonStyle: "default",
      nextFragmentButtonStyle: "primary",
      showConfig: false,
      showGlobalView: false,
      showHistoric: false,
      showLanding: true
    };

    this.handleShowGlobalView = this.handleShowGlobalView.bind(this);
    this.handleCloseGlobalView = this.handleCloseGlobalView.bind(this);

    this.handleShowConfig = this.handleShowConfig.bind(this);
    this.handleCloseConfig = this.handleCloseConfig.bind(this);

    this.handleShowHistoric = this.handleShowHistoric.bind(this);
    this.handleCloseHistoric = this.handleCloseHistoric.bind(this);

    this.handleCloseModals = this.handleCloseModals.bind(this);
    this.handleCloseLanding = this.handleCloseLanding.bind(this);
  }

  handleCloseModals() {
    this.setState({showConfig: false, showGlobalView: false, showLanding: false});
  }

  handleCloseConfig() {
    this.setState({showConfig: false});
  }

  handleShowConfig() {
    this.setState({showConfig: true});
  }

  handleCloseGlobalView() {
    this.setState({showGlobalView: false});
  }

  handleShowGlobalView() {
    this.setState({showGlobalView: true});
  }

  handleCloseHistoric() {
    this.setState({showHistoric: false});
  }

  handleShowHistoric() {
    this.setState({showHistoric: true});
  }

  handleCloseLanding() {
    this.setState({showLanding: false});
  }

  render() {
    let landingVisToRender;

    if (this.props.allFragmentsLoaded) {
      //alert(this.props.fragments.length)
      landingVisToRender = <SquareGrid onChange={this.handleCloseModals}/>;
    } else {
      landingVisToRender = <div/>;
    }
    return (<div className="app">

      <FragmentLoader/>

      <div className="buttonToolbar">
        <ButtonToolbar>

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
      </div>

      <div>
        <Fragment/>
      </div>

      <Modal show={this.state.showLanding} dialogClassName="custom-modal">
        <Modal.Header>
          <Modal.Title>
            Bem-vindo
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          Esta é a sua primeira actividade. {landingVisToRender}
        </Modal.Body>

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
          <HistoryMenu/>
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
