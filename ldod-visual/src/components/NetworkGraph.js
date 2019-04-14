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
      show: false,
      showInstructions: false
    };

    //console.log(this.props.graphData)
    //console.log("my graph data length: " + this.props.graphData.length);
    //this.props.graphData.map(d => console.log("my graphData id: " + d.interId + " distance: " + d.distance + " title: " + this.props.fragmentsHashMap.get(d.interId).meta.title));

    const maxFragsAnalyzedPercentage = 1.0;
    let mostDistantFragmentDistance = 1 //this.props.graphData[this.props.graphData.length - 1].distance;
    let lessDistantFragmentDistance = 1
    const graphHeight = 500;

    let truncateCheckBuffer = [];
    let truncateFloor = 20;

    this.props.graphData.map(function(d) {

      if ((d.distance / mostDistantFragmentDistance * 100) < truncateFloor && d.distance !== 0) {
        //console.log("my graphData id: " + d.interId + " |distance: " + d.distance + " |title: " + this.props.fragmentsHashMap.get(d.interId).meta.title + " |distance percentage: " + d.distance / mostDistantFragmentDistance * 100)
        truncateCheckBuffer.push(d)
      }
    }.bind(this));

    this.targetIndex = 0;

    this.props.graphData.map(function(f, index) {
      if (f.interId == targetIdActual) {
        this.targetIndex = index
      }
    }.bind(this));

    let targetIdActual
    if (this.props.fragments[this.props.fragmentIndex].interId) {
      targetIdActual = this.props.fragments[this.props.fragmentIndex].interId;
    }
    if (this.props.currentFragmentMode) {
      targetIdActual = this.props.recommendationArray[this.props.recommendationIndex].interId;
    }

    let originalFragmentSize = 6; //Math.floor(this.props.graphData.length * 0.05); 60; Math.max(5, Math.floor(this.props.graphData.length * 0.01));
    //BUILD atual FRAGMENT NODE
    let obj;
    obj = {
      id: this.props.graphData[this.targetIndex].interId,
      //label: "",//this.props.fragments[this.props.fragmentIndex].meta.title,
      shape: "dot",
      chosen: false,
      margin: 5, //só funciona com circle...
      size: originalFragmentSize,
      color: {
        border: "#DC143C",
        background: "#FF7F50",
        highlight: {
          color: "#DC143C",
          background: "#FF7F50"
        },
        hover: {
          border: "#DC143C",
          background: "#FF7F50"
        }
      },
      title: this.props.fragmentsHashMap.get(targetIdActual).meta.title,
      x: 0,
      y: 0,
      fixed: true
    };

    this.nodes.push(obj);
    this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[0].interId));
    //this.props.graphData.map((f, index) => console.log("Distance: " + f.distance + " index:" + index));

    //BUILD REMAINING FRAGMENTS' NODES

    let nrOfNonNullDistances = 0;

    let j;
    for (j = 0; j < this.props.graphData.length; j++) {

      if (j !== this.targetIndex) {

        let tempPercentage = this.props.graphData[j].distance / mostDistantFragmentDistance * 100;

        if (this.props.graphData[j].distance !== 0) {
          nrOfNonNullDistances++;
        }

        if (this.props.graphData[j].distance < lessDistantFragmentDistance) {
          lessDistantFragmentDistance = this.props.graphData[j].distance;
        }

      }
    }

    let nrOfNullDistances = this.props.graphData.length - nrOfNonNullDistances;
    //console.log("Number of values with 0 distance: " + nrOfNullDistances);
    //console.log("Number of values bellow mask2 " + nrValuesSubMask2);

    let xFactor = 0;
    let yFactor = 0;
    var i;
    for (i = 1; i < this.props.graphData.length; i++) {

      this.recommendationArray.push(this.props.fragmentsHashMap.get(this.props.graphData[i].interId));

      //blue
      let nodeBorderColor = "#2B7CE9";

      let totalAxes = 200; //The number of different axes

      let absoluteDistance = this.props.graphData[i].distance;
      let distancePercentage = this.props.graphData[i].distance / mostDistantFragmentDistance * 100;

      let titleDistPerc = Math.round((100 - distancePercentage) * 100) / 100;

      // test for closer values
      // if (distancePercentage < 90)
      //   distancePercentage = 20

      //gradient
      var color1 = '061019'; // 194163 <- azul escuro #061019 -> azul ainda mais escuro
      var color2 = 'bcffff'; //"#DC143C"<-vermelho border 50ffff<-azul claro FF7F50<-laranja bcffff<-azul ainda + claro
      var ratio = ((distancePercentage) / 100)
      //  console.log("ratio: " + ratio)
      var hex = function(x) {
        x = x.toString(16);
        return (x.length == 1)
          ? '0' + x
          : x;
      };

      var r = Math.ceil(parseInt(color1.substring(0, 2), 16) * ratio + parseInt(color2.substring(0, 2), 16) * (1 - ratio));
      var g = Math.ceil(parseInt(color1.substring(2, 4), 16) * ratio + parseInt(color2.substring(2, 4), 16) * (1 - ratio));
      var b = Math.ceil(parseInt(color1.substring(4, 6), 16) * ratio + parseInt(color2.substring(4, 6), 16) * (1 - ratio));

      var middle = hex(r) + hex(g) + hex(b);

      let nodeBackgroundColor = "#" + middle; //pickHex("#3379B5", '#265B89', distancePercentage) "#D2E5FF";

      let mySize = originalFragmentSize * 0.5;

      let angleSlice = Math.PI * 2 / totalAxes; //The width in radians of each "slice"

      //console.log("NetworkGraph.js: The number of different axes: " + totalAxes);

      let rand = Math.random() * (360 - 1) + 1;

      // test for closer values
      // if (distancePercentage < 90)
      //   distancePercentage = 20

      //console.log("lessDistantFragmentDistance * 100: " + lessDistantFragmentDistance * 100)

      let valueToDec = 4 - (lessDistantFragmentDistance * 100);

      let edgeLengthFactor = (5 * lessDistantFragmentDistance) + 4; //10000;

      xFactor = (distancePercentage + valueToDec) * edgeLengthFactor * Math.cos(angleSlice * rand - Math.PI / 2);

      yFactor = (distancePercentage + valueToDec) * edgeLengthFactor * Math.sin(angleSlice * rand - Math.PI / 2);

      //purple
      if (!this.props.currentFragmentMode && this.props.graphData[i].interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
        nodeBorderColor = "#4B0082";
        nodeBackgroundColor = "#8A2BE2";
      }

      // let titleDistPerc = parseFloat(distancePercentage).toFixed(2);

      let myTitle = this.props.fragmentsHashMap.get(this.props.graphData[i].interId).meta.title + " | Semelhança: " + (
      titleDistPerc) + "%"
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
          },
          highlight: {
            color: nodeBorderColor,
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

    this.toggleInstructions = this.toggleInstructions.bind(this);

  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions
    });
  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];

    if (nodeId && nodeId !== this.props.recommendationArray[this.props.recommendationIndex].interId) {

      var i;
      for (i = 0; i < this.props.recommendationArray.length; i++) {

        if (this.props.recommendationArray[i].interId === nodeId) {
          //console.log("clicking on nodeId " + this.recommendationArray[i].interId + " of title " + this.recommendationArray[i].meta.title)
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
            nextFragment: this.props.recommendationArray[i],
            vis: historyVis,
            criteria: historyCriteria,
            visualization: globalViewToRender,
            recommendationArray: this.props.recommendationArray,
            recommendationIndex: i,
            fragmentIndex: this.props.fragmentIndex, //bug... o fragindex do proximo vai mudar ou nao consoante current ou not current. ou nao. se not current mode fica igual.
            start: new Date().getTime()
          };

          this.props.setRecommendationIndex(i);
          this.props.setRecommendationArray(this.props.recommendationArray);

          if (this.props.currentFragmentMode) {
            var j;
            for (j = 0; j < this.props.fragments.length; j++) {
              if (this.props.fragments[j].interId === nodeId) {
                //console.log("networkgraph: because of currentFragmentMode, setFragmentIndex is now: " + j)
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

          //bug - apenas calcular array de recomendações caso estejamos em nova atividade
          if (this.props.currentFragmentMode) {
            this.props.setRecommendationLoaded(false);
          }

          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        }
        //this.props.setFragmentIndex(i);

      }
      //console.log("NETWORKGRAPH: recommendation index actual: " + this.props.recommendationIndex)

      this.props.onChange();

    }
  }

  _handleKeyDownActivity = (event) => {

    var I_KEY = 73;

    switch (event.keyCode) {
      case I_KEY:
        this.toggleInstructions();
        break;
      default:
        break;

    }

  }

  componentDidMount() {
    document.addEventListener("keydown", this._handleKeyDownActivity);
    const data = {
      nodes: this.nodes,
      edges: this.edges
    };

    var container = document.getElementById('networkvis');
    this.network = new Network(container, data, this.options);
    //this.network.on("hoverNode", this.handleHoverNode);

    //this.network.stabilize(1);
    //this.network.stabilize(30);
    var height = Math.round(window.innerHeight * 0.65) + 'px'; // The DOM way

    container.style.height = height
    this.network.redraw();
    //this.network.fit();

    var moveToOptions = {
      position: {
        x: 0,
        y: 0
      }, // position to animate to (Numbers)
      scale: 2.5, // scale to animate to  (Number)
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
    this.network.focus(this.props.graphData[this.targetIndex].interId, moveToOptions)
    this.network.on("selectNode", this.handleSelectNode);
    //console.log("randomSeed: " + this.network.getSeed());

    this.network.on("hoverNode", function(params) {
      //this.network.canvas.body.container.style.cursor = 'pointer'
      document.getElementById('networkvis').getElementsByTagName("canvas")[0].style.cursor = 'pointer'
    });
    this.network.on("blurNode", function(params) {
      document.getElementById('networkvis').getElementsByTagName("canvas")[0].style.cursor = 'default'
    });

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

    let instructions = (<div className="instructionsButton">
      <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
        Mostrar instruções [i]
      </Button>
    </div>)

    if (this.state.showInstructions) {
      instructions = (<div>
        <div className="instructionsText">
          <p>
            Neste grafo de rede, cada círculo representa um fragmento da edição do{" "}
            <i>Livro do Desassossego</i>{" "}
            que selecionou.
          </p>

          <lu>
            <li>
              Selecione um fragmento novo ao clicar num dos círculos em torno do {orangeCircleText}.
            </li>
            <li>
              Quanto mais próximos estiverem do {orangeCircleText}
              (correspondente ao fragmento sob o qual realizou ou está a realizar uma nova atividade), mais semelhantes serão segundo o critério desta atividade.
            </li>
            <li>
              O círculo mais próximo será sempre do fragmento que, dentro do conjunto de todos os fragmentos da edição, é o que é relativamente mais semelhante, seja 5% ou 100% semelhante.
            </li>
            <li>
              Quanto mais alta a semelhança em percentagem, mais claro será o azul do círculo.
            </li>
            <li>
              Um {purpleCircleText}
              representará o fragmento que está a ler atualmente caso navegue para um fragmento diferente do fragmento inicial (o {orangeCircleText}).
            </li>

            <li>
              Para navegar pelo grafo, pode usar os botões de navegação na parte do inferior do grafo, ou simplesmente arrastar o grafo com o botão esquerdo do rato ou fazer zoom-in ou zoom-out com a roda do rato.
            </li>
          </lu>

        </div>

        <div className="instructionsButton">
          <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
            Esconder instruções [i]
          </Button>
        </div>

        <br/>

      </div>)
    }

    return (<div>

      {instructions}

      <div className="graphNetwork" id="networkvis"></div>

    </div>);
  }
}

const NetworkGraph = connect(mapStateToProps, mapDispatchToProps)(ConnectedNetworkGraph);

export default NetworkGraph;
