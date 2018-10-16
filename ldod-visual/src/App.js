import React, { Component } from 'react';
import './App.css';
import { RepositoryService } from './services/RepositoryService'
import FragmentContainer from './containers/FragmentContainer'
import ReaderButtonToolbar from './components/ReaderButtonToolbar'

class App extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="app">

        <div className="buttonToolbar">
          <ReaderButtonToolbar/>
        </div>

        <div>
          <FragmentContainer/>
        </div>

      </div>
    );
  }
}

export default App;
