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
import {VIS_NETWORK_GRAPH, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXT_SIMILARITY, CRIT_CHRONOLOGICAL_ORDER} from "../constants/history-transitions";
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
    this.props.graphData.map(d => console.log("my graphData id: " + d.interId + " distance: " + d.distance + " title: " + this.props.fragmentsHashMap.get(d.interId).meta.title));

    const maxFragsAnalyzedPercentage = 1.0;
    let mostDistantFragmentDistance = this.props.graphData[this.props.graphData.length - 1].distance;
    const graphHeight = 500;

    let truncateCheckBuffer = [];
    let truncateFloor = 20;

    this.props.graphData.map(function(d) {

      if ((d.distance / mostDistantFragmentDistance * 100) < truncateFloor && d.distance !== 0) {
        console.log("my graphData id: " + d.interId + " |distance: " + d.distance + " |title: " + this.props.fragmentsHashMap.get(d.interId).meta.title + " |distance percentage: " + d.distance / mostDistantFragmentDistance * 100)
        truncateCheckBuffer.push(d)
      }
    }.bind(this));

    let myTitle = this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title;
    if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date !== null) {
      myTitle = this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title + " | Data: " + this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date;
    } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date) {
      myTitle = this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title + " | Data: Sem data";
    } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date !== null) {
      myTitle = this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title + " | Data: " + this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date;
    } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.date) {
      myTitle = this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title + " | Data: Sem data";
    }

    let originalFragmentSize = 6; //Math.floor(this.props.graphData.length * 0.05); 60; Math.max(5, Math.floor(this.props.graphData.length * 0.01));
    //BUILD ACTUAL FRAGMENT NODE
    let obj;
    obj = {
      id: this.props.graphData[0].interId,
      //label: "",//this.props.fragments[this.props.fragmentIndex].meta.title,
      shape: "dot",
      chosen: false,
      margin: 5, //só funciona com circle...
      size: originalFragmentSize,
      color: {
        border: "#DC143C",
        background: "#FF7F50"
      },
      title: myTitle, //this.props.fragmentsHashMap.get(this.props.graphData[0].interId).meta.title,  + " || " + truncateText(this.props.recommendationArray[this.props.recommendationIndex].text, 60),
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
    let mask2 = 0.3;
    let mask3 = 0.5;
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
    const sizeMultiplier1 = 0.7;
    const sizeMultiplier2 = 0.8;
    const sizeMultiplier3 = 0.9;
    // const sizeMultiplier1 = 0.4;
    // const sizeMultiplier2 = 0.5;
    // const sizeMultiplier3 = 0.7;
    const sizeMultiplier4 = 5;
    const sizeMultiplier5 = 8;
    let multiplier;

    let j;
    for (j = 1; j < this.props.graphData.length; j++) {

      this.props.graphData[j].distance = (1 - this.props.graphData[j].distance)

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

      //blue
      let nodeBorderColor = "#2B7CE9";
      let nodeBackgroundColor = "#D2E5FF";
      let totalAxes = 200; //The number of different axes

      let absoluteDistance = this.props.graphData[i].distance;
      let distancePercentage = this.props.graphData[i].distance / mostDistantFragmentDistance * 100;

      let edgeLengthFactor = 10000; //10000;
      let mySize = originalFragmentSize * 0.7;

      //small interpolation for when the distance is zero

      if (truncateCheckBuffer.length < 2) {
        console.log("NetworkGraph.js: no values after " + truncateFloor + "%, will truncate edgeLengthFactor");
        console.log(truncateCheckBuffer.length);
        edgeLengthFactor = edgeLengthFactor / 5;
      } else {
        console.log("NetworkGraph.js: values after " + truncateFloor + "%, not going to truncate edgeLengthFactor");
        console.log(truncateCheckBuffer.length);
      }

      if (distancePercentage < mask2) {
        // nodeBorderColor = "#7FFFD4";
        // nodeBackgroundColor = "#00FFFF";
        totalAxes = nrValuesSubMask2;
        mySize = mySize * sizeMultiplier2;
        absoluteDistance = mask2 / 100 * mostDistantFragmentDistance

      } else if (distancePercentage < mask3) {
        totalAxes = nrValuesSubMask3;
        mySize = mySize * sizeMultiplier3;
        absoluteDistance = mask3 / 100 * mostDistantFragmentDistance

      } else if (distancePercentage > mask4) {
        mySize = mySize * sizeMultiplier4;
        //nodeBorderColor = "#7FFFD4";
        //nodeBackgroundColor = "#00FFFF";
        //totalAxes = nrValuesSubMask8;
        //absoluteDistance = mask8 / 100 * mostDistantFragmentDistance
        //edgeLengthFactor = 4000;
      }

      if (distancePercentage === 0) {
        // nodeBorderColor = "#101010";
        // nodeBackgroundColor = "#505050";
        console.log("NetworkGraph.js: distance is zero!")
        totalAxes = nrOfNullDistances;
        mySize = mySize * sizeMultiplier1;
        absoluteDistance = mask1 / 100 * mostDistantFragmentDistance
        edgeLengthFactor = 10000;
      }

      if (mostDistantFragmentDistance === 0) {
        // nodeBorderColor = "#101010";
        // nodeBackgroundColor = "#505050";
        console.log("NetworkGraph.js: mostDistantFragmentDistance is zero!")
        totalAxes = nrOfNullDistances;
        mySize = mySize * sizeMultiplier1;
        mostDistantFragmentDistance = 10;
        absoluteDistance = mask1 / 100 * mostDistantFragmentDistance
        edgeLengthFactor = 10000;
      }

      let angleSlice = Math.PI * 2 / totalAxes; //The width in radians of each "slice"

      console.log("NetworkGraph.js: The number of different axes: " + totalAxes);

      let rand = Math.random() * (360 - 1) + 1;

      xFactor = (absoluteDistance / mostDistantFragmentDistance) * edgeLengthFactor * Math.cos(angleSlice * rand - Math.PI / 2);

      yFactor = (absoluteDistance / mostDistantFragmentDistance) * edgeLengthFactor * Math.sin(angleSlice * rand - Math.PI / 2);

      //purple
      if (!this.props.currentFragmentMode && this.props.graphData[i].interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
        nodeBorderColor = "#4B0082";
        nodeBackgroundColor = "#8A2BE2";
      }

      let myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title
      let hasDateWhileCritDate = true;
      if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date !== null) {
        myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " | Data: " + this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date;

      } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date) {
        myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " | Data: Sem data";
        hasDateWhileCritDate = false;
      } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date !== null) {
        myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " | Data: " + this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date;

      } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.date) {
        myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " | Data: Sem data";
        hasDateWhileCritDate = false;
      }

      let hoverBorderColor = "#DC143C" // red
      if (!this.props.currentFragmentMode) {
        hoverBorderColor = "#4B0082" //purple
      }

      obj = {
        id: this.props.graphData[i].interId,
        //label: "",
        shape: "dot",
        margin: 5, //só funciona com circle...
        size: mySize,
        color: {
          border: nodeBorderColor,
          background: nodeBackgroundColor,
          hover: {
            border: hoverBorderColor,
            background: nodeBackgroundColor
          }
        },
        title: myTitle, //this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title, + " " + this.props.graphData[i].distance,  + " || " + truncateText(myText, 60)
        fixed: true,
        x: xFactor,
        y: yFactor
      };

      if (hasDateWhileCritDate) {
        this.nodes.push(obj);
      }
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
        hover: true,
        navigationButtons: true,
        hoverConnectedEdges: false
      }
    };

    this.handleSelectNode = this.handleSelectNode.bind(this);

  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    if (nodeId && nodeId !== this.props.fragments[this.props.fragmentIndex].interId) {

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
    container.style.height = 500 + 'px';
    this.network.redraw();
    //this.network.fit();

    var moveToOptions = {
      position: {
        x: 0,
        y: 0
      }, // position to animate to (Numbers)
      scale: 10, // scale to animate to  (Number)
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

    let orangeCircleText = (<span style={{
        background: "#FF7F50",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#FF7F50",
        color: 'white'
      }}>
      <b>círculo laranja</b>
    </span >);

    let purpleCircleText = (<span style={{
        background: "#8A2BE2",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#8A2BE2",
        color: 'white'
      }}>
      <b>círculo roxo</b>
    </span >);

    return (<div>

      <p>
        Neste grafo de rede, cada círculo representa um fragmento da edição virtual do livro do desassossego que seleccionou.
      </p>

      <p>
        Seleccione um fragmento novo ao clicar num dos círculos em torno do {orangeCircleText}. Quanto mais próximos estiverem do {orangeCircleText}
        (correspondente ao fragmento sob o qual realizou ou está a realizar uma nova actividade), mais semelhantes serão segundo o critério desta actividade.
      </p>

      <p>
        Um {purpleCircleText}
        representará o fragmento que está a ler actualmente caso navegue para um fragmento diferente do fragmento inicial (o {orangeCircleText}).
      </p>

      <p>
        Para navegar pelo grafo, pode usar os botões de navegação na parte do inferior do grafo, ou simplesmente arrastar o grafo com o botão esquerdo do rato ou fazer zoom-in ou zoom-out com a roda do rato.
      </p>

      <div className="graphNetwork" id="networkvis"></div>

    </div>);
  }
}

const NetworkGraph = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraph);

export default NetworkGraph;
