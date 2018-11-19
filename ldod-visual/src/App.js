import React, {Component} from "react";
import "./App.css";
import FragmentContainer from "./containers/FragmentContainer";
import {setFragmentIndex, addHistoryEntry} from "./actions/index";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import ActivityMenu from "./components/ActivityMenu";
import HistoryMenu from "./components/HistoryMenu";
import {VIS_NETWORK, BY_HISTORIC, BY_NEXTBUTTON, BY_PREVIOUSBUTTON} from "./constants/history-transitions";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history};
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
      showHistoric: false
    };

    this.handleClickPrevious = this.handleClickPrevious.bind(this);
    this.handleClickNext = this.handleClickNext.bind(this);

    this.handleShowGlobalView = this.handleShowGlobalView.bind(this);
    this.handleCloseGlobalView = this.handleCloseGlobalView.bind(this);

    this.handleShowConfig = this.handleShowConfig.bind(this);
    this.handleCloseConfig = this.handleCloseConfig.bind(this);

    this.handleShowHistoric = this.handleShowHistoric.bind(this);
    this.handleCloseHistoric = this.handleCloseHistoric.bind(this);

    this.handleCloseModals = this.handleCloseModals.bind(this);
  }

  handleClickPrevious() {
    if (this.props.fragmentIndex === 1) { //no more previous fragments
      this.setState({previousFragmentButtonStyle: "default"});
    }
    if (this.props.fragmentIndex > 0) {
      this.setState({nextFragmentButtonStyle: "primary"});

      //add history entry
      let obj;
      obj = {
        originalFragment: this.props.fragments[this.props.fragmentIndex],
        nextFragment: this.props.fragments[this.props.fragmentIndex - 1],
        via: BY_PREVIOUSBUTTON,
        criteria: "NA",
        visualization: this.props.fragmentIndex
      };
      this.props.addHistoryEntry(obj);
      this.props.setFragmentIndex(this.props.fragmentIndex - 1);
    }
  }

  handleClickNext() {
    if (this.props.fragmentIndex === this.props.fragments.length - 2) {
      this.setState({
        nextFragmentButtonStyle: "default" //no more next fragments
      });
    }
    if (this.props.fragmentIndex < this.props.fragments.length - 1) {

      //add history entry
      let obj;
      obj = {
        originalFragment: this.props.fragments[this.props.fragmentIndex],
        nextFragment: this.props.fragments[this.props.fragmentIndex + 1],
        via: BY_NEXTBUTTON,
        criteria: "NA",
        visualization: "NA"
      };
      this.props.addHistoryEntry(obj);
      this.props.setFragmentIndex(this.props.fragmentIndex + 1);
      this.setState({previousFragmentButtonStyle: "primary"});
    }
  }

  handleCloseModals() {
    this.setState({showConfig: false, showGlobalView: false});
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

  render() {
    return (<div className="app">
      <div className="buttonToolbar">
        <ButtonToolbar>
          <Button bsStyle={this.state.previousFragmentButtonStyle} bsSize="large" onClick={this.handleClickPrevious}>
            Anterior
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowConfig}>
            Configuração de Actividade
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowGlobalView}>
            Vista Global
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.handleShowHistoric}>
            Histórico
          </Button>

          <Button bsStyle={this.state.nextFragmentButtonStyle} bsSize="large" onClick={this.handleClickNext}>
            Próximo
          </Button>
        </ButtonToolbar>
      </div>

      <div>
        <FragmentContainer/>
      </div>

      <Modal show={this.state.showGlobalView} onHide={this.handleCloseGlobalView} dialogClassName="custom-modal">
        <Modal.Header closeButton="closeButton">
          <Modal.Title>
            Vista Global
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>{this.props.currentVisualization}</Modal.Body>

        <Modal.Footer>
          <Button bsStyle="primary" onClick={this.handleCloseGlobalView}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={this.state.showConfig} onHide={this.handleCloseConfig} dialogClassName="custom-modal">
        <Modal.Header closeButton="closeButton">
          <Modal.Title>
            Configuração de Actividade
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
          {console.log(this.props.history)}
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
