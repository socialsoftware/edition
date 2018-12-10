import {Network} from "vis";
import React, {Component, createRef} from "react";
import {setFragmentIndex} from "../actions/index";
import {connect} from "react-redux";
import "./SquareGrid.css";
import {
  setCurrentVisualization,
  addHistoryEntry,
  setOutOfLandingPage,
  setHistoryEntryCounter,
  setRecommendationArray,
  setRecommendationIndex
} from "../actions/index";
import {VIS_SQUAREGRID, BY_SQUAREGRID_EDITIONORDER, CRIT_EDITIONORDER} from "../constants/history-transitions";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage)),
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

class ConnectedSquareGrid extends Component {
  constructor(props) {
    super(props);
    this.network = {};
    this.appRef = createRef();
    this.nodes = [];
    this.edges = [];
    this.options = [];
    this.minMaxList = [];
    this.myFragmentArray = [];

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

    this.myFragmentArray = this.props.fragments;

    for (i = 0; i < this.myFragmentArray.length; i++) {

      const myTitle = this.myFragmentArray[i].meta.title;
      const myText = this.myFragmentArray[i].text;

      obj = {
        id: this.myFragmentArray[i].interId,
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
        title: myTitle, //"<div><h1><b>" + myTitle + "</b></h1></div>",  + " || " + truncateText(myText, 60),
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
    for (i = 0; i < this.myFragmentArray.length - 1; i++) {

      obj = {
        from: this.myFragmentArray[i].interId,
        to: this.myFragmentArray[i + 1].interId,
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
      width: "800",
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

    this.props.setRecommendationArray(this.myFragmentArray);

    this.handleSelectNode = this.handleSelectNode.bind(this);
  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    if (nodeId) {
      //alert(nodeId);
      var i;
      for (i = 0; i < this.myFragmentArray.length; i++) {
        if (this.myFragmentArray[i].interId === nodeId) {
          const globalViewToRender = (<SquareGrid onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.myFragmentArray[this.props.fragmentIndex],
            nextFragment: this.myFragmentArray[i],
            vis: VIS_SQUAREGRID,
            criteria: CRIT_EDITIONORDER,
            visualization: globalViewToRender,
            recommendationArray: this.myFragmentArray, //mudar para quando o cirterio for difernete,
            recommendationIndex: i,
            start: new Date().getTime()
          };
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY

          this.props.setFragmentIndex(i);

          this.props.setRecommendationArray(this.myFragmentArray);

          this.props.setRecommendationIndex(i);

          this.props.setOutOfLandingPage(true);

          this.props.onChange();
        }
      }
    }
  }

  componentDidMount() {
    //if (this.props.fragments.length > 0) {
    const data = {
      nodes: this.nodes,
      edges: this.edges
    };

    var container = document.getElementById('gridvis');
    this.network = new Network(container, data, this.options);
    this.network.on("selectNode", this.handleSelectNode);
    //}
  }

  render() {

    return (<div>
      <p>
        Instruções do square grid.
      </p>

      <div className="graphGrid" id="gridvis"></div>

    </div>);
  }
}

const SquareGrid = connect(mapStateToProps, mapDispatchToProps)(ConnectedSquareGrid);

export default SquareGrid;
