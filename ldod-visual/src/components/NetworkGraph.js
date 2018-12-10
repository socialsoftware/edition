import {Network} from "vis";
import React, {Component, createRef} from "react";
import {setFragmentIndex} from "../actions/index";
import {connect} from "react-redux";
import "./NetworkGraph.css";
import {
  setCurrentVisualization,
  addHistoryEntry,
  setHistoryEntryCounter,
  fragmentIndex,
  setRecommendationArray,
  setRecommendationIndex
} from "../actions/index";
import {VIS_NETWORK, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXTSIMILARITY} from "../constants/history-transitions";
import {Button, Popover, OverlayTrigger, Overlay} from "react-bootstrap";
import NetworkGraphContainer from "../containers/NetworkGraphContainer";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter,
    fragmentsHashMap: state.fragmentsHashMap,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationIndex: recommendationIndex => dispatch(setRecommendationIndex(recommendationIndex))
  };
};

function truncateText(text, length) {
  if (text.length <= length) {
    return text;
  }

  return text.substr(0, length) + "\u2026";
}

class ConnectedNetworkGraph extends Component {
  constructor(props) {
    super(props);
    this.network = {};
    this.appRef = createRef();
    this.nodes = [];
    this.edges = [];
    this.options = [];
    this.minMaxList = [];
    this.recommendationArray = [];
    this.state = {
      show: false
    };

    console.log(this.props.graphData);

    const maxFragsAnalyzedPercentage = 1.0;
    const edgeLengthFactor = 10000;
    const originalFragmentSize = 30;
    const mostDistantFragmentDistance = this.props.graphData[this.props.graphData.length - 1].distance;
    const graphHeight = 500;

    //BUILD ACTUAL FRAGMENT NODE
    let obj;
    obj = {
      id: this.props.graphData[0].interId,
      //label: "",//this.props.fragments[this.props.fragmentIndex].meta.title,
      shape: "dot",
      margin: 5, //só funciona com circle...
      size: originalFragmentSize,
      color: {
        border: "#2B7CE9",
        background: "#D2E5FF"
      },
      title: this.props.recommendationArray[this.props.recommendationIndex].meta.title, // + " || " + truncateText(this.props.recommendationArray[this.props.recommendationIndex].text, 60),
      x: 0,
      y: 0,
      fixed: true
    };

    this.nodes.push(obj);

    this.recommendationArray = [];
    this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[0].interId));

    //BUILD REMAINING FRAGMENTS' NODES
    var i;
    for (i = 1; i < this.props.graphData.length * maxFragsAnalyzedPercentage; i++) {

      this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[i].interId));

      obj = {
        id: this.props.graphData[i].interId,
        //label: "",
        shape: "dot",
        margin: 5, //só funciona com circle...
        size: originalFragmentSize * 0.8,
        color: {
          border: "#DC143C",
          background: "#FF7F50"
        },
        title: this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title // + " || " + truncateText(myText, 60)
        //fixed: true
      };

      this.nodes.push(obj);
    }

    //BUILD EDGES
    for (i = 1; i < this.props.graphData.length * maxFragsAnalyzedPercentage; i++) {
      let myLength = 0;

      if (this.props.graphData[i].distance > 0) {
        myLength = (this.props.graphData[i].distance / mostDistantFragmentDistance) * edgeLengthFactor;
      }

      //truncate max value
      if (myLength > edgeLengthFactor / 2) {
        myLength = edgeLengthFactor / 2;
      }

      console.log("myLength: " + myLength);

      obj = {
        from: this.props.graphData[0].interId,
        to: this.props.graphData[i].interId,
        length: myLength,
        hidden: true
      };

      this.edges.push(obj);
    }

    this.options = {
      autoResize: true,
      height: "500",
      //width: "500", comment to center
      layout: {
        hierarchical: false,
        randomSeed: undefined
      },
      physics: {
        enabled: true
      },
      interaction: {
        dragNodes: false,
        dragView: false,
        zoomView: false,
        hover: true
      }
    };

    this.handleSelectNode = this.handleSelectNode.bind(this);

  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    if (nodeId) {
      //alert(nodeId);
      var i;
      for (i = 0; i < this.recommendationArray.length; i++) {
        if (this.recommendationArray[i].interId === nodeId) {
          const globalViewToRender = (<NetworkGraphContainer pFragmentId={this.props.fragments[this.props.fragmentIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
            nextFragment: this.recommendationArray[i],
            vis: VIS_NETWORK,
            criteria: CRIT_TEXTSIMILARITY,
            visualization: globalViewToRender,
            recommendationArray: this.recommendationArray,
            recommendationIndex: i,
            start: new Date().getTime()
          };
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          this.props.setRecommendationArray(this.recommendationArray);
          this.props.setRecommendationIndex(i);
          //this.props.setFragmentIndex(i);
          console.log("recommendationindex actual a partir do network graph: " + this.props.setRecommendationIndex)
          this.props.onChange();

        }
      }
    }
  }

  componentDidMount() {
    const data = {
      nodes: this.nodes,
      edges: this.edges
    };

    this.network = new Network(this.appRef.current, data, this.options);
    this.network.on("hoverNode", this.handleHoverNode);
    this.network.on("selectNode", this.handleSelectNode);
    console.log("randomSeed: " + this.network.getSeed());

  }

  render() {

    return (<div>
      <Overlay show={this.state.show} target={this.state.target} placement="bottom" container={this} containerPadding={20}>
        <Popover id="popover-contained" title="Popover bottom">
          <strong>Holy guacamole!</strong>
          Check this info.
        </Popover>
      </Overlay>

      <p>
        Seleccione um fragmento novo ao clicar num dos círculos vermelhos. Quanto mais próximos estiverem do círculo azul (correspondente ao fragmento que está a ler actualmente), mais semelhantes serão.
      </p>
      <div className="graphNetwork">
        <div ref={this.appRef}/>
      </div>
    </div>);
  }
}

const NetworkGraph = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraph);

export default NetworkGraph;
