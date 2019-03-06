import {Network, DataSet} from "vis";
import React, {Component, createRef} from "react";
import {connect} from "react-redux";
import "./SquareGrid.css";
import {
  setFragmentIndex,
  setCurrentVisualization,
  addHistoryEntry,
  setOutOfLandingPage,
  setHistoryEntryCounter,
  setRecommendationArray,
  setRecommendationIndex,
  setVisualizationTechnique,
  setSemanticCriteria,
  setCurrentCategory
} from "../actions/index";
import {VIS_SQUARE_GRID, VIS_NETWORK_GRAPH, CRIT_EDITION_ORDER, CRIT_CHRONOLOGICAL_ORDER, CRIT_CATEGORY} from "../constants/history-transitions";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    currentFragmentMode: state.currentFragmentMode,
    outOfLandingPage: state.outOfLandingPage,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    allFragmentsLoaded: state.allFragmentsLoaded,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    fragmentsSortedByDate: state.fragmentsSortedByDate,
    currentCategory: state.currentCategory,
    potentialCategory: state.potentialCategory
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
    setRecommendationIndex: recommendationIndex => dispatch(setRecommendationIndex(recommendationIndex)),
    setVisualizationTechnique: visualizationTechnique => dispatch(setVisualizationTechnique(visualizationTechnique)),
    setSemanticCriteria: semanticCriteria => dispatch(setSemanticCriteria(semanticCriteria)),
    setCurrentCategory: currentCategory => dispatch(setCurrentCategory(currentCategory))
  };
};

class ConnectedNavigationButton extends Component {
  constructor(props) {
    super(props);
    this.network = {};

    this.nodes = [];
    this.edges = [];
    this.options = [];
    this.myId = 'previousButton';
    //this.xOffset = -300;
    //⟶ ⮕ 🡆 🠚 🠞 🠒 🠪 🠮 🠲 🠊 🢂
    //http://xahlee.info/comp/unicode_arrows.html
    this.label = "🢀";
    this.shape = "box";
    if (this.props.nextButton) {
      this.myId = 'nextButton'
      //this.xOffset *= -1;
      this.label = "🢂";
    }

    this.state = {
      fragmentChangeUpdate: false
    };

    let obj;

    //blue
    this.nodeBorderColor = "#2B7CE9";
    this.nodeBackgroundColor = "#D2E5FF";

    let originalFragmentSize = 50;

    //       grey if date is a criteria and fragment has no date
    //
    //       let dateExistsAndChronologicalCriteria = false;
    //       grey
    //       if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date == null) {
    //         dateExistsAndChronologicalCriteria = true;
    //         nodeBorderColor = "#101010";
    //         nodeBackgroundColor = "#505050";
    //       } else if ((!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date == null)) {
    //         dateExistsAndChronologicalCriteria = true;
    //         nodeBorderColor = "#101010";
    //         nodeBackgroundColor = "#505050";
    //       }
    //
    // se hover no anterior mostrar o x, se nao houver anterior meter a cinzento. igual para o proximo.
    //       if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date !== null) {
    //         myTitle = this.myFragmentArray[i].meta.title + " | Data: " + this.myFragmentArray[i].meta.date;
    //       } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.myFragmentArray[i].meta.date) {
    //         myTitle = this.myFragmentArray[i].meta.title + " | Data: Sem data";
    //       }
    //
    //       if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY) {
    //         myTitle = this.myFragmentArray[i].meta.title + " | Categorias: " + this.myFragmentArray[i].meta.categories;
    //       } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CATEGORY) {
    //         myTitle = this.myFragmentArray[i].meta.title + " | Categorias: " + this.myFragmentArray[i].meta.categories;
    //       }

    if (this.props.nextButton) {

      if (this.props.recommendationIndex === this.props.recommendationArray.length - 1) {

        this.nodeBorderColor = "#101010";
        this.nodeBackgroundColor = "#505050";

      }

    } else {

      if (this.props.recommendationIndex === 0) {

        this.nodeBorderColor = "#101010";
        this.nodeBackgroundColor = "#505050";

      }

    }

    this.nodes = new DataSet([
      {
        id: this.myId,
        font: {
          size: 150,
          color: this.nodeBorderColor
        },
        label: this.label,
        shape: this.shape,
        size: originalFragmentSize,
        fixed: true,
        borderWidthSelected: 1,
        x: 0,
        y: 0,
        color: {
          border: this.nodeBorderColor,
          background: this.nodeBackgroundColor,
          highlight: {
            border: '#FFD700',
            background: '#DAA520'
          }
          // hover: {
          //   border: "#2B7CE9",
          //   background: "#D2E5FF"
          // }
        }
      }
      // , {
      //   id: "dummy",
      //   label: "",
      //   shape: "square",
      //   size: 1,
      //   fixed: true,
      //   borderWidthSelected: 1,
      //   chosen: false,
      //   x: this.xOffset,
      //   y: 0,
      //   color: {
      //     border: '#404040',
      //     background: '#404040',
      //      highlight: {
      //        border: '#FFD700',
      //        background: '#DAA520'
      //      },
      //      hover: {
      //        border: "#2B7CE9",
      //        background: "#D2E5FF"
      //      }
      //   }
      // }
    ]);

    // this.edges = new DataSet([
    //   {
    //     from: this.myId,
    //     to: "dummy",
    //     arrows: {
    //       to: true,
    //       scaleFactor: 80
    //     },
    //     length: 1,
    //     hoverWidth: 2,
    //     width: 10
    //   }
    // ]);

    this.options = {
      //autoResize: true,

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
        hover: false,
        multiselect: true,
        selectable: true,
        selectConnectedEdges: false
      }
    };

    this.handleSelectNode = this.handleSelectNode.bind(this);

  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    console.log(new Date().getTime() + " XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX " + nodeId)

    var clickedNode = this.nodes.get(nodeId);

    if (this.props.nextButton) {

      if (this.props.recommendationIndex === (this.props.recommendationArray.length - 2)) {

        clickedNode.color = {
          border: '#101010',
          background: '#505050'
        }
        clickedNode.font = {
          color: '#101010'
        }
        this.nodes.update(clickedNode);
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        let obj;
        let historyVis = this.props.visualizationTechnique;
        let historyCriteria = this.props.semanticCriteria;
        obj = {
          id: this.props.historyEntryCounter,
          originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
          nextFragment: this.props.recommendationArray[this.props.recommendationIndex + 1],
          vis: historyVis,
          criteria: historyCriteria,
          visualization: this.props.currentVisualization,
          recommendationArray: this.props.recommendationArray,
          recommendationIndex: this.props.recommendationIndex + 1,
          fragmentIndex: this.props.fragmentIndex,
          start: new Date().getTime(),
          category: this.props.currentCategory
        };

        this.props.addHistoryEntry(obj);
        this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        this.props.setRecommendationIndex(this.props.recommendationIndex + 1)
      } else if (this.props.recommendationIndex < this.props.recommendationArray.length - 1) {

        clickedNode.color = {
          border: "#2B7CE9",
          background: "#D2E5FF"
        }
        clickedNode.font = {
          color: '#2B7CE9'
        }
        this.nodes.update(clickedNode);
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        let obj;
        let historyVis = this.props.visualizationTechnique;
        let historyCriteria = this.props.semanticCriteria;
        obj = {
          id: this.props.historyEntryCounter,
          originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
          nextFragment: this.props.recommendationArray[this.props.recommendationIndex + 1],
          vis: historyVis,
          criteria: historyCriteria,
          visualization: this.props.currentVisualization,
          recommendationArray: this.props.recommendationArray,
          recommendationIndex: this.props.recommendationIndex + 1,
          fragmentIndex: this.props.fragmentIndex,
          start: new Date().getTime(),
          category: this.props.currentCategory
        };

        this.props.addHistoryEntry(obj);
        this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        this.props.setRecommendationIndex(this.props.recommendationIndex + 1)
      }

    } else {

      if (this.props.recommendationIndex === 1) {
        clickedNode.color = {
          border: '#101010',
          background: '#505050'
        }
        clickedNode.font = {
          color: '#101010'
        }
        this.nodes.update(clickedNode);

        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        let obj;
        let historyVis = this.props.visualizationTechnique;
        let historyCriteria = this.props.semanticCriteria;
        obj = {
          id: this.props.historyEntryCounter,
          originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
          nextFragment: this.props.recommendationArray[this.props.recommendationIndex - 1],
          vis: historyVis,
          criteria: historyCriteria,
          visualization: this.props.currentVisualization,
          recommendationArray: this.props.recommendationArray,
          recommendationIndex: this.props.recommendationIndex - 1,
          fragmentIndex: this.props.fragmentIndex,
          start: new Date().getTime(),
          category: this.props.currentCategory
        };

        this.props.addHistoryEntry(obj);
        this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        this.props.setRecommendationIndex(this.props.recommendationIndex - 1)

      } else if (this.props.recommendationIndex > 0) {
        clickedNode.color = {
          border: "#2B7CE9",
          background: "#D2E5FF"
        }
        clickedNode.font = {
          color: '#2B7CE9'
        }
        this.nodes.update(clickedNode);
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        let obj;
        let historyVis = this.props.visualizationTechnique;
        let historyCriteria = this.props.semanticCriteria;
        obj = {
          id: this.props.historyEntryCounter,
          originalFragment: this.props.recommendationArray[this.props.recommendationIndex],
          nextFragment: this.props.recommendationArray[this.props.recommendationIndex - 1],
          vis: historyVis,
          criteria: historyCriteria,
          visualization: this.props.currentVisualization,
          recommendationArray: this.props.recommendationArray,
          recommendationIndex: this.props.recommendationIndex - 1,
          fragmentIndex: this.props.fragmentIndex,
          start: new Date().getTime(),
          category: this.props.currentCategory
        };

        this.props.addHistoryEntry(obj);
        this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
        //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
        this.props.setRecommendationIndex(this.props.recommendationIndex - 1)

      }

    }

    this.setState({
      fragmentChangeUpdate: !this.state.fragmentChangeUpdate
    });
    //
    this.network.redraw();

    //must unselect or else, handleSelectNode won't fire on an already selected node.
    this.network.unselectAll();
    console.log("click")

  }

  componentDidMount() {

    console.log("container1: " + this.myId)

    if (this.props.allFragmentsLoaded) {
      const data = {
        nodes: this.nodes,
        edges: this.edges
      };

      var container = document.getElementById(this.myId);
      this.network = new Network(container, data, this.options);
      //container.style.height = 750 + 'px';
      this.network.redraw();
      //this.network.fit();
      this.network.on("selectNode", this.handleSelectNode);

      console.log("container2: " + this.myId)

    }
  }

  componentDidUpdate() {

    console.log(new Date().getTime() + " DID UPDATE: " + this.myId)

    var clickedNode = this.nodes.get(this.myId);
    if (this.props.visualizationTechnique == VIS_NETWORK_GRAPH) {
      clickedNode.shape = "circle"
    } else if (this.props.visualizationTechnique == VIS_SQUARE_GRID) {
      clickedNode.shape = "box"
    }

    this.nodes.update(clickedNode);

    if (this.props.nextButton) {

      if (this.props.recommendationIndex === (this.props.recommendationArray.length - 1)) {

        clickedNode.color = {
          border: '#101010',
          background: '#505050'
        }

        clickedNode.font = {
          color: '#101010'
        }

        this.nodes.update(clickedNode);

      } else if (this.props.recommendationIndex < this.props.recommendationArray.length - 1) {

        clickedNode.color = {
          border: "#2B7CE9",
          background: "#D2E5FF"
        }
        clickedNode.font = {
          color: '#2B7CE9'
        }

        this.nodes.update(clickedNode);

      }

    } else {

      if (this.props.recommendationIndex === 0) {
        clickedNode.color = {
          border: '#101010',
          background: '#505050'
        }
        clickedNode.font = {
          color: '#101010'
        }
        this.nodes.update(clickedNode);

      } else if (this.props.recommendationIndex > 0) {
        clickedNode.color = {
          border: "#2B7CE9",
          background: "#D2E5FF"
        }
        clickedNode.font = {
          color: '#2B7CE9'
        }
        this.nodes.update(clickedNode);

      }

    }

    this.network.redraw();
  }

  render() {
    console.log("rendering " + this.myId)
    return (<div id={this.myId}></div>);
  }
}

const NavigationButton = connect(mapStateToProps, mapDispatchToProps)(ConnectedNavigationButton);

export default NavigationButton;
