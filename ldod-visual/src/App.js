import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { RepositoryService } from './services/RepositoryService'

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
        acronym: 'LdoD-test',
        fragments: [],
        transcriptions: []
    }

  }

  componentDidMount() {
    const service = new RepositoryService();
    service.getFragments(this.state.acronym).then(response => {
      this.setState({
        fragments: response.data
      })
    });
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload.
        </p>

        {this.state.fragments.map(f => f.title)}
      </div>
    );
  }
}

export default App;
