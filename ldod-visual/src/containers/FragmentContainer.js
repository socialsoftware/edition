import React, { Component } from 'react';
import { RepositoryService } from '../services/RepositoryService'
import Fragment from '../components/Fragment';

class FragmentContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
        acronym: 'LdoD-test',
        fragments: [],
        transcriptions: [],
        titleArray: [],
        textArray: []
    }

    //invocacao da api dos fragmentos

  }

  componentDidMount() {
    const service = new RepositoryService();
    service.getTranscriptions(this.state.acronym).then(response => {
      const transcriptions =  response.data.transcriptions;
      service.getFragments(this.state.acronym).then(response => {
        this.setState({
          transcriptions: transcriptions,
          fragments: response.data.fragments,
          titlesArray: response.data.fragments.map(f => f.meta.title),
          textArray: response.data.fragments.map(f => f.text)
        })
      });
    });
  }

  render() {
      let fragmentToRender;
      let bla;

      if (this.state.fragments.length>0) {
        fragmentToRender = <Fragment titleArray={this.state.titlesArray} textArray={this.state.textArray}/>
      } else {
        fragmentToRender = <div></div>
      }

      return (
        <div>{fragmentToRender}</div>
    );
  }
}

export default FragmentContainer;
