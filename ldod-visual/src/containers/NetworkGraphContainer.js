import React, {Component} from "react";
import {RepositoryService} from "../services/RepositoryService";
import NetworkGraph from "../components/NetworkGraph";
import {setCurrentVisualization} from "../actions/index";
import {connect} from "react-redux";
import {
  VIS_SQUARE_GRID,
  VIS_NETWORK_GRAPH,
  VIS_WORD_CLOUD,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_TEXT_SIMILARITY,
  CRIT_HETERONYM,
  CRIT_TAXONOMY,
  CRIT_WORD_RELEVANCE
} from "../constants/history-transitions";
import loadingGif from '../assets/loading.gif';

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    currentFragmentMode: state.currentFragmentMode,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    semanticCriteriaData: state.semanticCriteriaData
  };
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
    if (this.props.currentFragmentMode) {
      const service = new RepositoryService();
      let targetId = this.props.fragments[this.props.fragmentIndex].interId;

      if (this.props.currentFragmentMode) {
        //console.log("NETWORKGRAPHCONTAINER: CALCULATING NETWORK GRAPH DISTANCES FOR CURRENT FRAGMENT AND NOT!! THE PICKED FRAGMENT %%%%%%%%%%%%%%%%%%%%%")
        targetId = this.props.recommendationArray[this.props.recommendationIndex].interId;
      }

      let pHeteronymWeight = "0.0";
      let pTextWeight = "0.0";
      let pDateWeight = "0.0";
      let ptaxonomyWeight = "0.0";

      if (this.props.potentialSemanticCriteria == CRIT_HETERONYM) {
        pHeteronymWeight = "1.0";
        //console.log("NETWoRKGRAPHCONTAINER: pHeteronymWeight = 1.0");
      } else if (this.props.potentialSemanticCriteria == CRIT_TEXT_SIMILARITY) {
        pTextWeight = "1.0";
        //console.log("NETWoRKGRAPHCONTAINER: pTextWeight = 1.0");
      } else if (this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER) {
        pDateWeight = "1.0";
        //console.log("NETWoRKGRAPHCONTAINER: pDateWeight = 1.0");
      } else if (this.props.potentialSemanticCriteria == CRIT_TAXONOMY) {
        ptaxonomyWeight = "1.0";
        //console.log("NETWoRKGRAPHCONTAINER: ptaxonomyWeight = 1.0");
      }

      let idsDistanceArray = [];
      let myNewRecommendationArray = [];
      service.getIntersByDistance(targetId, pHeteronymWeight, pTextWeight, pDateWeight, ptaxonomyWeight).then(response => {
        console.log("NetworkGraphContainer.js: Distances received.");

        let receivedArray = response.data;

        receivedArray.map(f => {
          f.distance = (1 - f.distance)
        });

        //receivedArray.map(f => console.log("receivedArray: " + f.distance));

        this.setState({networkGraphData: receivedArray});
        this.setState(prevState => ({
          fragmentsDistanceLoaded: !prevState.check
        }));

      });
    } else {
      console.log("NetworkGraphContainer: skipping service call since distances are already calculated");
      this.setState({networkGraphData: this.props.semanticCriteriaData});
      this.setState(prevState => ({
        fragmentsDistanceLoaded: !prevState.check
      }));
    }
  }

  render() {
    let graphToRender;

    if (this.state.fragmentsDistanceLoaded) {
      graphToRender = (<NetworkGraph graphData={this.state.networkGraphData} onChange={this.props.onChange}/>);
    } else {
      graphToRender = (<div>
        <img src={loadingGif} alt="loading...netgraph" className="loadingGifCentered"/>
        <p align="center">A carregar fragmentos semelhantes...</p>
        <p align="center">Se demorar demasiado tempo, atualize a página e volte a tentar.</p>
      </div>);
    }

    return <div>{graphToRender}</div>;
  }
}

const NetworkGraphContainer = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraphContainer);

export default NetworkGraphContainer;
