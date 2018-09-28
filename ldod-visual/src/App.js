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
    service.getTranscriptions(this.state.acronym).then(response => {
      const transcriptions =  response.data.transcriptions;
      service.getFragments(this.state.acronym).then(response => {
        this.setState({
          transcriptions: transcriptions,
          fragments: response.data.fragments
        })
      });
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
          To get started, edit <code>src/App.js</code> blablablabalbalba.
        </p>
        {this.state.transcriptions.map(f => <span> {f.title}<br /> </span>)}
        {this.state.fragments.map(f => <span> {f.meta.dates}<br /> </span>)}
      </div>
    );
  }
}

export default App;
