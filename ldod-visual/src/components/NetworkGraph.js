import {Network} from "vis";
import React, {Component, createRef} from "react";
import {setFragmentIndex} from "../actions/index";
import {connect} from "react-redux";
import "./NetworkGraph.css";
import {setCurrentVisualization, addHistoryEntry, setHistoryEntryCounter, fragmentIndex} from "../actions/index";
import {VIS_NETWORK, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXTSIMILARITY} from "../constants/history-transitions";
import {Button, Popover, OverlayTrigger, Overlay} from "react-bootstrap";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history, historyEntryCounter: state.historyEntryCounter};
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter))
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
      title: this.props.fragments[this.props.fragmentIndex].meta.title, // + " || " + truncateText(this.props.fragments[this.props.fragmentIndex].text, 60),
      x: 0,
      y: 0,
      fixed: true
    };

    this.nodes.push(obj);

    //BUILD REMAINING FRAGMENTS' NODES
    var i;
    for (i = 1; i < this.props.graphData.length * maxFragsAnalyzedPercentage; i++) {
      let myTitle,
        myText;
      var j;
      for (j = 0; j < this.props.fragments.length; j++) {
        if (this.props.fragments[j].interId === this.props.graphData[i].interId) {
          myTitle = this.props.fragments[j].meta.title;
          myText = this.props.fragments[j].text;
        }
      }

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
        title: myTitle // + " || " + truncateText(myText, 60)
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
    this.handleHoverNode = this.handleHoverNode.bind(this);

    this.handleYolo = this.handleYolo.bind(this);
  }

  handleYolo(event) {
    console.log("handleYolo !!!!!!!!!!!!!!!!!!!!!!!!!!! GRAPH")
  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    if (nodeId) {
      //alert(nodeId);
      var i;
      for (i = 0; i < this.props.fragments.length; i++) {
        if (this.props.fragments[i].interId === nodeId) {
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.props.fragments[this.props.fragmentIndex],
            nextFragment: this.props.fragments[i],
            vis: VIS_NETWORK,
            criteria: CRIT_TEXTSIMILARITY,
            visualization: this.props.currentVisualization,
            recommendationArray: this.props.graphData,
            start: new Date().getTime()
          };
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          this.props.onChange();
          this.props.setFragmentIndex(i);
        }
      }
    }
  }

  handleHoverNode(event) {
    console.log("oi");
    this.setState({
      //show: !this.state.show
    });
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
