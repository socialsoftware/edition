import React, {Component} from "react";
import {RepositoryService} from "../services/RepositoryService";
import NetworkGraph from "../components/NetworkGraph";
import {setCurrentVisualization} from "../actions/index";
import {connect} from "react-redux";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization};
};

const mapDispatchToProps = dispatch => {
  return {
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization))
  };
};

class ConnectedNetworkGraphContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      networkGraphData: [],
      fragmentsDistanceLoaded: false
    };
  }

  componentDidMount() {
    const service = new RepositoryService();
    service.getIntersByDistance(this.props.pFragmentId, this.props.pHeteronymWeight, this.props.pTextWeight, this.props.pDateWeight, this.props.ptaxonomyWeight).then(response => {
      this.setState({networkGraphData: response.data});
      this.setState(prevState => ({
        fragmentsDistanceLoaded: !prevState.check
      }));

      //meter aqui a logica dos pesos, se ptextweight=1, semanticcriteria=text, etc.

      const globalViewToRender = (<NetworkGraphContainer pFragmentId={this.props.fragments[this.props.fragmentIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);

      this.props.setCurrentVisualization(globalViewToRender);
    });
  }

  render() {
    let graphToRender;

    if (this.state.fragmentsDistanceLoaded) {
      graphToRender = (<NetworkGraph graphData={this.state.networkGraphData} onChange={this.props.onChange}/>);
    } else {
      graphToRender = <div/>;
    }

    return <div>
      {graphToRender}
    </div>;
  }
}

const NetworkGraphContainer = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraphContainer);

export default NetworkGraphContainer;
