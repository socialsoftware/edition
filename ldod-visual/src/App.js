import React, { Component } from 'react';
import './App.css';
import { RepositoryService } from './services/RepositoryService'
import FragmentContainer from './containers/FragmentContainer'
import { Button, ButtonToolbar } from 'react-bootstrap'

class App extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="app">

        <div className="fragmentContainer">
            <FragmentContainer/>
        </div>

        <div className="buttonToolbar">
          <ButtonToolbar>
            <Button bsStyle="primary" bsSize="large">
              Anterior
            </Button>
            <Button bsStyle="primary" bsSize="large">
              Global View
            </Button>
            <Button bsStyle="primary" bsSize="large">
              Configuração
            </Button>
            <Button bsStyle="primary" bsSize="large">
              Próximo
            </Button>
          </ButtonToolbar>
        </div>

      </div>
    );
  }
}

export default App;
