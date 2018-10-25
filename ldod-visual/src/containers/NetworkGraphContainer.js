import React, { Component } from 'react';
import { RepositoryService } from '../services/RepositoryService';
import NetworkGraph from '../components/NetworkGraph';


class NetworkGraphContainer extends Component {

  constructor(props) {
      super(props);

      this.state = {
          list: [],
          fragmentsDistanceLoaded: false
      }

    }


  componentDidMount() {

    const service = new RepositoryService();
    service.getIntersByDistance(this.props.pFragmentId, this.props.pHeteronymWeight, this.props.pTextWeight, this.props.pDateWeight, this.props.ptaxonomyWeight).then(response => {
        this.setState({list: response.data});
        this.setState(prevState => ({
          fragmentsDistanceLoaded: !prevState.check
        }));

    });

  }

  render() {

    //alert("hey?")

    let graphToRender;

    if (this.state.fragmentsDistanceLoaded) {
      //alert(this.props.fragments.length)

      //this.state.list.map(i =>alert(i.interId+" : "+i.distance));
      //alert(this.state.list.length)
      //alert(this.state.list);

      graphToRender = <NetworkGraph graphData={this.state.list} onChange={this.props.onChange}/>

    } else {
      graphToRender = <div></div>
    }


    return (
      <div> {graphToRender} </div>
    );
  }
}

export default NetworkGraphContainer;
