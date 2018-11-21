import {Network} from "vis";
import React, {Component, createRef} from "react";
import {setFragmentIndex} from "../actions/index";
import {connect} from "react-redux";
import "./NetworkGraph.css";
import {setCurrentVisualization, addHistoryEntry, setOutOfLandingPage} from "../actions/index";
import {VIS_NETWORK, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXTSIMILARITY} from "../constants/history-transitions";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history};
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage))
  };
};

function truncateText(text, length) {
  if (text.length <= length) {
    return text;
  }

  return text.substr(0, length) + "\u2026";
}

class ConnectedSquareGrid extends Component {
  constructor(props) {
    super(props);
    this.network = {};
    this.appRef = createRef();
    this.nodes = [];
    this.edges = [];
    this.options = [];
    this.minMaxList = [];

    console.log(this.props.graphData);

    const maxFragsAnalyzedPercentage = 1.0;
    const edgeLengthFactor = 10000;
    const originalFragmentSize = 30;
    const remainingNodeFactor = 1.0;
    const maxRows = 10;
    const nodeTranslationSpacing = originalFragmentSize * 3;
    let inversionToggle = false;

    //BUILD FRAGMENTS' NODES for SQUAREMAP
    let obj;
    var i;
    let xFactor = 0;
    let yFactor = 0;

    for (i = 0; i < this.props.fragments.length; i++) {

      const myTitle = this.props.fragments[i].meta.title;
      const myText = this.props.fragments[i].text;

      obj = {
        id: this.props.fragments[i].interId,
        //label: "",
        shape: "square",
        margin: 5, //só funciona com circle...
        size: originalFragmentSize * remainingNodeFactor,
        fixed: true,
        color: {
          border: '#2B7CE9',
          background: '#D2E5FF',
          highlight: {
            border: '#FFD700',
            background: '#DAA520'
          },
          hover: {
            border: '#DC143C',
            background: '#FF7F50'
          }

        },
        title: myTitle + " || " + truncateText(myText, 60),
        x: xFactor,
        y: yFactor
      };

      if (inversionToggle) {
        xFactor = xFactor - nodeTranslationSpacing;
      } else {
        xFactor = xFactor + nodeTranslationSpacing;
      }

      if ((i + 1) % maxRows === 0 && i != 0) {
        console.log(i)
        if (inversionToggle) {
          xFactor = 0
        } else {
          xFactor = xFactor - nodeTranslationSpacing;
        }
        yFactor = yFactor + nodeTranslationSpacing;
        inversionToggle = !inversionToggle;
      }

      this.nodes.push(obj);
    }

    //BUILD EDGES
    for (i = 0; i < this.props.fragments.length - 1; i++) {

      obj = {
        from: this.props.fragments[i].interId,
        to: this.props.fragments[i + 1].interId,
        length: 1,
        hidden: false,
        arrows: 'to',
        hoverWidth: 0
      };

      this.edges.push(obj);
    }

    this.options = {
      autoResize: true,
      height: "500",
      layout: {
        hierarchical: {
          enabled: false
        },
        randomSeed: 1
      },
      physics: {
        enabled: false
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
      for (i = 0; i < this.props.fragments.length; i++) {
        if (this.props.fragments[i].interId === nodeId) {
          //add history entry
          let obj;
          obj = {
            originalFragment: this.props.fragments[this.props.fragmentIndex],
            nextFragment: this.props.fragments[i],
            via: BY_NETWORK_TEXTSIMILARITY,
            criteria: CRIT_TEXTSIMILARITY,
            visualization: this.props.currentVisualization
          };
          this.props.addHistoryEntry(obj);
          this.props.onChange();
          this.props.setOutOfLandingPage(true);
          this.props.setFragmentIndex(i);
          const globalViewToRender = (<SquareGrid onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
        }
      }
    }
  }

  componentDidMount() {
    if (this.props.fragments.length > 0) {
      const data = {
        nodes: this.nodes,
        edges: this.edges
      };

      this.network = new Network(this.appRef.current, data, this.options);
      this.network.on("selectNode", this.handleSelectNode);
    }
  }

  render() {

    return (<div>
      <p>
        Instruções do square grid.
      </p>
      <div className="graph">
        <div ref={this.appRef}/>
      </div>
    </div>);
  }
}

const SquareGrid = connect(mapStateToProps, mapDispatchToProps)(ConnectedSquareGrid);

export default SquareGrid;
