import React, { Component } from 'react';
import './App.css';
import { RepositoryService } from './services/RepositoryService'
import FragmentContainer from './containers/FragmentContainer'
import { setFragmentIndex } from "./actions/index";
import { connect } from "react-redux";
import { Button, ButtonToolbar, Modal } from 'react-bootstrap'
import ActivityMenu from './components/ActivityMenu'


const mapStateToProps = state => {
    return {
        fragments: state.fragments,
        fragmentIndex: state.fragmentIndex
    };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex))
  };
};

class ConnectedApp extends Component {

  constructor(props) {

    super(props);

    this.state = {
      previousFragmentButtonStyle: "default",
      nextFragmentButtonStyle: "primary",
      showConfig: false,
      showGlobalView: false
    }

    this.handleClickPrevious = this.handleClickPrevious.bind(this);
    this.handleClickNext = this.handleClickNext.bind(this);

    this.handleShowGlobalView  = this.handleShowGlobalView.bind(this);
    this.handleCloseGlobalView  = this.handleCloseGlobalView.bind(this);

    this.handleShowConfig  = this.handleShowConfig.bind(this);
    this.handleCloseConfig  = this.handleCloseConfig.bind(this);


  }

  handleClickPrevious() {
    if (this.props.fragmentIndex === 1){
      this.setState({previousFragmentButtonStyle: "default"})
    }
    if (this.props.fragmentIndex>0){
      this.setState({nextFragmentButtonStyle: "primary"})
      this.props.setFragmentIndex(this.props.fragmentIndex-1)
    }
  }

  handleClickNext() {
    if (this.props.fragmentIndex === this.props.fragments.length-2){
      this.setState({nextFragmentButtonStyle: "default"})
    }
    if (this.props.fragmentIndex<this.props.fragments.length-1){
      this.props.setFragmentIndex(this.props.fragmentIndex+1)
      this.setState({previousFragmentButtonStyle: "primary"})
    }
  }

  handleCloseConfig() {
    this.setState({ showConfig: false });
  }

  handleShowConfig() {
    this.setState({ showConfig: true });
  }

  handleCloseGlobalView() {
    this.setState({ showGlobalView: false });
  }

  handleShowGlobalView() {
    this.setState({ showGlobalView: true });
  }



  render() {
    return (
      <div className="app">

        <div className="buttonToolbar">
          <ButtonToolbar>

            <Button bsStyle={this.state.previousFragmentButtonStyle} bsSize="large" onClick={this.handleClickPrevious}>
              Anterior
            </Button>

            <Button bsStyle="primary" bsSize="large" onClick={this.handleShowGlobalView}>
              Global View
            </Button>

            <Button bsStyle="primary" bsSize="large" onClick={this.handleShowConfig}>
              Configuração
            </Button>

            <Button bsStyle={this.state.nextFragmentButtonStyle} bsSize="large" bsSize="large" onClick={this.handleClickNext}>
              Próximo
            </Button>

          </ButtonToolbar>
        </div>

        <div>
          <FragmentContainer/>
        </div>

        <Modal show={this.state.showGlobalView} onHide={this.handleCloseGlobalView} dialogClassName="custom-modal">
          <Modal.Header closeButton>
            <Modal.Title> Global View </Modal.Title>
          </Modal.Header>

          <Modal.Body>
            <p>
              Global View.
            </p>

          </Modal.Body>

          <Modal.Footer>
            <Button bsStyle="primary" onClick={this.handleCloseGlobalView}>Fechar</Button>
          </Modal.Footer>

        </Modal>


        <Modal show={this.state.showConfig} onHide={this.handleCloseConfig} dialogClassName="custom-modal">

          <Modal.Header closeButton>
            <Modal.Title>Configuração</Modal.Title>
          </Modal.Header>

          <Modal.Body>
              <ActivityMenu/>

          </Modal.Body>

          <Modal.Footer>
            <Button bsStyle="primary" onClick={this.handleCloseConfig}>Fechar</Button>
          </Modal.Footer>

        </Modal>

      </div>
    );
  }
}

const App = connect(mapStateToProps,mapDispatchToProps)(ConnectedApp);

export default App;
