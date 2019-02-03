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
  setRecommendationIndex,
  setCurrentFragmentMode,
  setRecommendationLoaded,
  setSemanticCriteriaDataLoaded,
  setVisualizationTechnique,
  setSemanticCriteria,
  setSemanticCriteriaData
} from "../actions/index";
import {VIS_NETWORK_GRAPH, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXT_SIMILARITY} from "../constants/history-transitions";
import {Button, Popover, OverlayTrigger, Overlay} from "react-bootstrap";
import NetworkGraphContainer from "../containers/NetworkGraphContainer";
import HashMap from "hashmap";
import {RepositoryService} from "../services/RepositoryService";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter,
    fragmentsHashMap: state.fragmentsHashMap,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    currentFragmentMode: state.currentFragmentMode,
    recommendationLoaded: state.recommendationLoaded,
    semanticCriteriaDataLoaded: state.semanticCriteriaDataLoaded,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationIndex: recommendationIndex => dispatch(setRecommendationIndex(recommendationIndex)),
    setCurrentFragmentMode: currentFragmentMode => dispatch(setCurrentFragmentMode(currentFragmentMode)),
    setRecommendationLoaded: recommendationLoaded => dispatch(setRecommendationLoaded(recommendationLoaded)),
    setSemanticCriteriaDataLoaded: semanticCriteriaDataLoaded => dispatch(setSemanticCriteriaDataLoaded(semanticCriteriaDataLoaded)),
    setVisualizationTechnique: visualizationTechnique => dispatch(setVisualizationTechnique(visualizationTechnique)),
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria)),
    setSemanticCriteriaData: semanticCriteriaData => dispatch(setSemanticCriteriaData(semanticCriteriaData))
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

    //console.log(this.props.graphData)
    //console.log("my graph data length: " + this.props.graphData.length);
    //this.props.graphData.map(d => console.log("my graphData id: " + d.interId + " distance: " + d.distance + " title: " + this.props.fragmentsHashMap.get(d.interId).meta.title));

    const maxFragsAnalyzedPercentage = 1.0;
    const mostDistantFragmentDistance = this.props.graphData[this.props.graphData.length - 1].distance;
    const graphHeight = 500;

    this.props.graphData.map(d => console.log("my graphData id: " + d.interId + " distance: " + d.distance + " title: " + this.props.fragmentsHashMap.get(d.interId).meta.title + "distance percentage: " + d.distance / mostDistantFragmentDistance * 100));

    let originalFragmentSize = 5; //Math.floor(this.props.graphData.length * 0.05); 60; Math.max(5, Math.floor(this.props.graphData.length * 0.01));
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
      title: this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title, // + " || " + truncateText(this.props.recommendationArray[this.props.recommendationIndex].text, 60),
      x: 0,
      y: 0,
      fixed: true
    };

    this.nodes.push(obj);
    this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[0].interId));
    //this.props.graphData.map((f, index) => console.log("Distance: " + f.distance + " index:" + index));

    //BUILD REMAINING FRAGMENTS' NODES

    let nrOfNonNullDistances = 0;
    let mask1 = 0.2;
    let mask2 = 0.5;
    let mask3 = 3;
    let mask4 = 10;
    let mask5 = 20;
    let mask6 = 30;
    let mask7 = 40;
    let mask8 = 50;
    let nrValuesSubMask1 = 0;
    let nrValuesSubMask2 = 0;
    let nrValuesSubMask3 = 0;
    let nrValuesSubMask4 = 0;
    let nrValuesSubMask5 = 0;
    let nrValuesSubMask6 = 0;
    let nrValuesSubMask7 = 0;
    let nrValuesSubMask8 = 0;

    let j;
    for (j = 1; j < this.props.graphData.length; j++) {
      let tempPercentage = this.props.graphData[j].distance / mostDistantFragmentDistance * 100;

      if (this.props.graphData[j].distance !== 0) {
        nrOfNonNullDistances++;
      }

      if (tempPercentage < mask1 && this.props.graphData[j].distance !== 0) {
        nrValuesSubMask1++;
      } else if (tempPercentage < mask2) {
        nrValuesSubMask2++;
      } else if (tempPercentage < mask3) {
        nrValuesSubMask3++;
      } else if (tempPercentage < mask4) {
        nrValuesSubMask4++;
      } else if (tempPercentage < mask4) {
        nrValuesSubMask4++;
      } else if (tempPercentage < mask5) {
        nrValuesSubMask5++;
      } else if (tempPercentage < mask6) {
        nrValuesSubMask6++;
      } else if (tempPercentage < mask7) {
        nrValuesSubMask7++;
      } else if (tempPercentage < mask8) {
        nrValuesSubMask8++;
      }

    }

    let nrOfNullDistances = this.props.graphData.length - nrOfNonNullDistances;
    console.log("Number of values with 0 distance: " + nrOfNullDistances);
    console.log("Number of values bellow mask2 " + nrValuesSubMask2);

    let xFactor = 0;
    let yFactor = 0;
    var i;
    for (i = 1; i < this.props.graphData.length; i++) {

      this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[i].interId));

      let nodeBorderColor = "#DC143C";
      let nodeBackgroundColor = "#FF7F50";
      let totalAxes = 200; //The number of different axes

      let absoluteDistance = this.props.graphData[i].distance;
      let distancePercentage = this.props.graphData[i].distance / mostDistantFragmentDistance * 100;

      //small interpolation for when the distance is zero

      //unique values
      let tempArray = [];
      this.props.graphData.map(g => tempArray.push(g.distance));
      var uniqueItemCount = Array.from(new Set(tempArray))
      console.log("uniqueItemCount: " + uniqueItemCount.length);

      let edgeLengthFactor = uniqueItemCount.length * 200; //8000; 10000;

      if (distancePercentage === 0) {
        //nodeBorderColor = "#101010";
        //nodeBackgroundColor = "#505050";
        totalAxes = nrOfNullDistances;
        absoluteDistance = mask1 / 100 * mostDistantFragmentDistance
      } else if (distancePercentage < mask2) {
        nodeBorderColor = "#7FFFD4";
        nodeBackgroundColor = "#00FFFF";
        totalAxes = nrValuesSubMask2;
        absoluteDistance = mask2 / 100 * mostDistantFragmentDistance
      }/*else if (distancePercentage > mask8) {
        nodeBorderColor = "#7FFFD4";
        nodeBackgroundColor = "#00FFFF";
        totalAxes = nrValuesSubMask8;
        absoluteDistance = mask8 / 100 * mostDistantFragmentDistance
        edgeLengthFactor = 10;
      }*/

      let angleSlice = Math.PI * 2 / totalAxes; //The width in radians of each "slice"

      console.log("NetworkGraph.js: The number of different axes: " + totalAxes);

      let rand = Math.random() * (360 - 1) + 1;

      xFactor = (absoluteDistance / mostDistantFragmentDistance) * edgeLengthFactor * Math.cos(angleSlice * rand - Math.PI / 2);

      yFactor = (absoluteDistance / mostDistantFragmentDistance) * edgeLengthFactor * Math.sin(angleSlice * rand - Math.PI / 2);

      //purple
      if (!this.props.currentFragmentMode && this.props.graphData[i].interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
        nodeBorderColor = "#800080";
        nodeBackgroundColor = "#663399";
      }

      obj = {
        id: this.props.graphData[i].interId,
        //label: "",
        shape: "dot",
        margin: 5, //só funciona com circle...
        size: originalFragmentSize * 0.7,
        color: {
          border: nodeBorderColor,
          background: nodeBackgroundColor
        },
        title: this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " " + this.props.graphData[i].distance, // + " || " + truncateText(myText, 60)
        fixed: true,
        x: xFactor,
        y: yFactor
      };

      this.nodes.push(obj);
    }

    //BUILD EDGES
    for (i = 1; i < this.props.graphData.length * maxFragsAnalyzedPercentage; i++) {

      obj = {
        from: this.props.graphData[0].interId,
        to: this.props.graphData[i].interId,
        hidden: true
      };

      this.edges.push(obj);
    }

    this.options = {
      autoResize: true,
      //height: "500",
      //width: "800",

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
        dragView: true,
        zoomView: true,
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
      for (i = 0; i < this.props.recommendationArray.length; i++) {

        if (this.recommendationArray[i].interId === nodeId) {
          let targetId = this.props.fragments[this.props.fragmentIndex].interId;
          if (this.props.currentFragmentMode) {
            targetId = this.props.recommendationArray[this.props.recommendationIndex].interId;
          }

          const globalViewToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);

          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          let historyVis = this.props.visualizationTechnique;
          let historyCriteria = this.props.semanticCriteria;
          if (this.props.currentFragmentMode) {
            historyVis = this.props.potentialVisualizationTechnique;
            historyCriteria = this.props.potentialSemanticCriteria;
          }
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
            nextFragment: this.recommendationArray[i],
            vis: historyVis,
            criteria: historyCriteria,
            visualization: globalViewToRender,
            recommendationArray: this.recommendationArray,
            recommendationIndex: i,
            fragmentIndex: this.props.fragmentIndex,
            start: new Date().getTime()
          };

          this.props.setRecommendationIndex(i);
          this.props.setRecommendationArray(this.recommendationArray);

          if (this.props.currentFragmentMode) {
            var j;
            for (j = 0; j < this.props.fragments.length; j++) {
              if (this.props.fragments[j].interId === nodeId) {
                console.log("networkgraph: because of currentFragmentMode, setFragmentIndex is now: " + j)
                this.props.setFragmentIndex(j);
                this.props.setRecommendationIndex(0);
                obj.fragmentIndex = j;
                obj.recommendationIndex = 0;
                let temp = [];
                temp.push(this.props.fragmentsHashMap.get(nodeId));
                obj.recommendationArray = temp;
                this.props.setRecommendationArray(temp);

              }
            }
          }
          this.props.setVisualizationTechnique(this.props.potentialVisualizationTechnique);
          this.props.setSemanticCriteria(this.props.potentialSemanticCriteria);
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)

          this.props.setRecommendationLoaded(false);

          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        }
        //this.props.setFragmentIndex(i);

      }
      console.log("NETWORKGRAPH: recommendation index actual: " + this.props.recommendationIndex)
      this.props.onChange();
    }

  }

  componentDidMount() {
    const data = {
      nodes: this.nodes,
      edges: this.edges
    };

    var container = document.getElementById('networkvis');
    this.network = new Network(container, data, this.options);
    //this.network.on("hoverNode", this.handleHoverNode);

    //this.network.stabilize(1);
    //this.network.stabilize(30);
    container.style.height = 750 + 'px';
    this.network.redraw();
    //this.network.fit();

    var moveToOptions = {
      position: {
        x: 0,
        y: 0
      }, // position to animate to (Numbers)
      scale: 2, // scale to animate to  (Number)
      offset: {
        x: 0,
        y: 0
      }, // offset from the center in DOM pixels (Numbers)
      animation: { // animation object, can also be Boolean
        duration: 1000, // animation duration in milliseconds (Number)
        easingFunction: "easeInCubic" // Animation easing function, available are:
      } // linear, easeInQuad, easeOutQuad, easeInOutQuad,
    } // easeInCubic, easeOutCubic, easeInOutCubic,
    // easeInQuart, easeOutQuart, easeInOutQuart,
    // easeInQuint, easeOutQuint, easeInOutQuint

    let targetId = this.props.fragments[this.props.fragmentIndex].interId;
    if (this.props.currentFragmentMode) {
      targetId = this.props.recommendationArray[this.props.recommendationIndex].interId;
    }
    this.network.focus(targetId, moveToOptions)
    this.network.on("selectNode", this.handleSelectNode);
    //console.log("randomSeed: " + this.network.getSeed());

  }

  render() {

    return (<div>
      <p>
        Seleccione um fragmento novo ao clicar num dos círculos vermelhos. Quanto mais próximos estiverem do círculo azul (correspondente ao fragmento que está a ler actualmente), mais semelhantes serão.
      </p>

      <div className="graphNetwork" id="networkvis"></div>

    </div>);
  }
}

const NetworkGraph = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraph);

export default NetworkGraph;
