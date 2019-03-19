import {Network} from "vis";
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
  setCurrentCategory,
  setGoldenArray
} from "../actions/index";
import {
  VIS_SQUARE_GRID,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_CATEGORY,
  CRIT_HETERONYM
} from "../constants/history-transitions";
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
    setCurrentCategory: currentCategory => dispatch(setCurrentCategory(currentCategory)),
    setGoldenArray: goldenArray => dispatch(setGoldenArray(goldenArray))
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

    this.myCategory = "";

    this.myFragmentArray = this.props.fragments;
    if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER) {
      this.myFragmentArray = this.props.fragmentsSortedByDate;
    } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER) {
      this.myFragmentArray = this.props.fragmentsSortedByDate;
    }

    if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY) {
      this.myCategory = this.props.potentialCategory;
    } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CATEGORY) {
      this.myCategory = this.props.currentCategory;
    }

    console.log("1 MAGNIFICENT DEBUG MY CATEGORY BEGGINING OF SQ: " + this.myCategory)

    const maxFragsAnalyzedPercentage = 1.0;
    const edgeLengthFactor = 10000;
    const originalFragmentSize = 30;
    const remainingNodeFactor = 1.0;
    const maxRows = Math.floor(Math.sqrt(this.props.fragments.length * 2));
    const nodeTranslationSpacing = originalFragmentSize * 3;
    let inversionToggle = false;
    this.nrOfLines = 1;

    //BUILD FRAGMENTS' NODES for SQUAREMAP
    let obj;
    var i;
    let xFactor = 0;
    let yFactor = 0;
    this.highlightText = "";
    this.supportMessage = "";
    this.conditionalSpace = "";

    for (i = 0; i < this.myFragmentArray.length; i++) {

      let myTitle = this.myFragmentArray[i].meta.title;
      //const myText = this.myFragmentArray[i].text;

      //blue
      let nodeBorderColor = "#2B7CE9";
      let nodeBackgroundColor = "#D2E5FF";

      //grey if date is a criteria and fragment has no date

      let dateExistsAndChronologicalCriteria = false;
      //grey
      if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date == null) {
        dateExistsAndChronologicalCriteria = true;
        nodeBorderColor = "#101010";
        nodeBackgroundColor = "#505050";
      } else if ((!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date == null)) {
        dateExistsAndChronologicalCriteria = true;
        nodeBorderColor = "#101010";
        nodeBackgroundColor = "#505050";
      }

      if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date !== null) {
        myTitle = this.myFragmentArray[i].meta.title + " | Data: " + this.myFragmentArray[i].meta.date;
      } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.myFragmentArray[i].meta.date) {
        myTitle = this.myFragmentArray[i].meta.title + " | Data: Sem data";
      } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i].meta.date !== null) {
        myTitle = this.myFragmentArray[i].meta.title + " | Data: " + this.myFragmentArray[i].meta.date;
      } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && !this.myFragmentArray[i].meta.date) {
        myTitle = this.myFragmentArray[i].meta.title + " | Data: Sem data";
      }

      if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.length !== 0) {
        myTitle = this.myFragmentArray[i].meta.title + " | Categorias: " + this.myFragmentArray[i].meta.categories;
      } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.length !== 0) {
        myTitle = this.myFragmentArray[i].meta.title + " | Categorias: " + this.myFragmentArray[i].meta.categories;
      } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.length == 0) {
        myTitle = this.myFragmentArray[i].meta.title + " | Categorias: Sem categorias."
      } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.length == 0) {
        myTitle = this.myFragmentArray[i].meta.title + " | Categorias: Sem categorias."
      }

      let myBorderWidth = 1;
      const newBorderW = 3;
      let goldHighlightStyle = {
        background: "#FFD700",
        padding: 2,
        color: "black",
        borderWidth: 3,
        borderColor: "#DAA520",
        borderStyle: 'solid'
      }

      //  #DAA520 goldenrod escuro
      // #FFD700 gold
      if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.includes(this.props.potentialCategory)) {
        this.conditionalSpace = " ";
        nodeBorderColor = "#DAA520";
        nodeBackgroundColor = "#FFD700";
        myBorderWidth = newBorderW;;
        this.supportMessage = (<span>
          <b>Categoria seleccionada:
          </b>
        </span >);
        this.highlightText = (<span style={goldHighlightStyle}>
          <b>{this.props.potentialCategory}</b>
        </span >);
      } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CATEGORY && this.myFragmentArray[i].meta.categories.includes(this.props.currentCategory)) {
        this.conditionalSpace = " ";
        nodeBorderColor = "#DAA520";
        nodeBackgroundColor = "#FFD700";
        myBorderWidth = newBorderW;;
        this.supportMessage = (<span>
          <b>Categoria seleccionada:
          </b>
        </span >);
        this.highlightText = (<span style={goldHighlightStyle}>
          <b>{this.props.currentCategory}</b>
        </span >);
      } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_HETERONYM && this.myFragmentArray[i].meta.heteronym == this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym) {
        this.conditionalSpace = " ";
        nodeBorderColor = "#DAA520";
        nodeBackgroundColor = "#FFD700";
        myBorderWidth = newBorderW;;
        this.supportMessage = (<span>
          <b>Heterónimo seleccionado:
          </b>
        </span >);
        this.highlightText = (<span style={goldHighlightStyle}>
          <b>{this.myFragmentArray[i].meta.heteronym}</b>
        </span >);
      } else if (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_HETERONYM && this.myFragmentArray[i].meta.heteronym == this.props.fragments[this.props.fragmentIndex].meta.heteronym) {
        this.conditionalSpace = " ";
        nodeBorderColor = "#DAA520";
        nodeBackgroundColor = "#FFD700";
        myBorderWidth = newBorderW;;
        this.supportMessage = (<span>
          <b>Heterónimo seleccionado:
          </b>
        </span >);
        this.highlightText = (<span style={goldHighlightStyle}>
          <b>{this.myFragmentArray[i].meta.heteronym}</b>
        </span >);
      }

      //purple
      if (!this.props.currentFragmentMode && this.myFragmentArray[i].interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {

        if (nodeBorderColor == "#DAA520") { //golden
          nodeBackgroundColor = "#8A2BE2"; //only purple background
          myBorderWidth = newBorderW;;
        } else {
          nodeBorderColor = "#4B0082";
          nodeBackgroundColor = "#8A2BE2";
          myBorderWidth = newBorderW;;
        }
      }

      //red
      if (this.props.outOfLandingPage && !this.props.currentFragmentMode && this.myFragmentArray[i].interId === this.props.fragments[this.props.fragmentIndex].interId) {

        if (nodeBorderColor == "#DAA520") { //golden
          nodeBackgroundColor = "#FF7F50"; //only red background
          myBorderWidth = newBorderW;;
        } else {
          nodeBorderColor = "#DC143C"
          nodeBackgroundColor = "#FF7F50"
          myBorderWidth = newBorderW;;
        }
      }

      if (this.props.outOfLandingPage && this.props.currentFragmentMode && this.myFragmentArray[i].interId === this.props.recommendationArray[this.props.recommendationIndex].interId) {
        if (nodeBorderColor == "#DAA520") { //golden
          nodeBackgroundColor = "#FF7F50"; //only red background
          myBorderWidth = newBorderW;;
        } else {
          nodeBorderColor = "#DC143C"
          nodeBackgroundColor = "#FF7F50"
          myBorderWidth = newBorderW;;
        }
      }

      let hoverBorderColor = "#DC143C" // red
      if (!this.props.currentFragmentMode) {
        hoverBorderColor = "#4B0082" //purple
      }

      obj = {
        id: this.myFragmentArray[i].interId,
        //label: "",
        shape: "square",
        margin: 5, //só funciona com circle...
        size: originalFragmentSize * remainingNodeFactor,
        fixed: true,
        chosen: true,
        borderWidth: myBorderWidth,
        color: {
          border: nodeBorderColor,
          background: nodeBackgroundColor,
          highlight: {
            border: '#FFD700',
            background: '#DAA520'
          },
          hover: {
            border: hoverBorderColor,
            background: nodeBackgroundColor
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

        if (inversionToggle) {
          xFactor = 0
        } else {
          xFactor = xFactor - nodeTranslationSpacing;
        }
        yFactor = yFactor + nodeTranslationSpacing;
        inversionToggle = !inversionToggle;
        this.nrOfLines = this.nrOfLines + 1;
      }

      this.nodes.push(obj);
    }

    //BUILD EDGES
    for (i = 0; i < this.myFragmentArray.length - 1; i++) {

      let showEdge = (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i + 1].meta.date == null) || (!(this.props.currentFragmentMode) && this.props.semanticCriteria == CRIT_CHRONOLOGICAL_ORDER && this.myFragmentArray[i + 1].meta.date == null)

      obj = {
        from: this.myFragmentArray[i].interId,
        to: this.myFragmentArray[i + 1].interId,
        length: 1,
        hidden: showEdge,
        arrows: 'to',
        hoverWidth: 0,
        chosen: false
      };

      this.edges.push(obj);
    }

    this.options = {
      //autoResize: true,
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
        dragView: false,
        zoomView: false,
        hover: true,
        hoverConnectedEdges: false
      }
    };

    //this.props.setRecommendationArray(this.myFragmentArray);

    this.handleSelectNode = this.handleSelectNode.bind(this);

  }

  handleSelectNode(event) {
    const nodeId = event.nodes[0];
    if (nodeId) {
      //alert(nodeId);
      // this.props.setSemanticCriteria(this.props.potentialSemanticCriteria);
      var i;
      for (i = 0; i < this.myFragmentArray.length; i++) {
        if (this.myFragmentArray[i].interId === nodeId) {

          console.log("MAGNIFICENT DEBUG0: " + this.myCategory)
          console.log("MAGNIFICENT DEBUG0.1: " + this.props.currentCategory)

          if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_HETERONYM) {
            this.myCategory = this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym;
            this.props.setCurrentCategory(this.myCategory);
          } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_HETERONYM) {
            this.myCategory = this.props.fragments[this.props.fragmentIndex].meta.heteronym;
            this.props.setCurrentCategory(this.myCategory);
          } else if (this.props.currentFragmentMode && this.props.potentialSemanticCriteria == CRIT_CATEGORY) {
            this.props.setCurrentCategory(this.myCategory);
          } else if (!this.props.currentFragmentMode && this.props.semanticCriteria == CRIT_CATEGORY) {
            this.props.setCurrentCategory(this.myCategory);
          }

          console.log("MAGNIFICENT DEBUG1: " + this.myCategory)
          console.log("MAGNIFICENT DEBUG1.1: " + this.props.currentCategory)

          const globalViewToRender = (<SquareGrid onChange={this.props.onChange}/>);
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
            originalFragment: this.myFragmentArray[this.props.fragmentIndex],
            nextFragment: this.myFragmentArray[i],
            vis: historyVis,
            criteria: historyCriteria,
            visualization: globalViewToRender,
            recommendationArray: this.myFragmentArray, //mudar para quando o cirterio for difernete,
            recommendationIndex: i,
            fragmentIndex: this.props.fragmentIndex,
            start: new Date().getTime(),
            category: this.myCategory
          };

          if (this.props.potentialSemanticCriteria == CRIT_CATEGORY && !this.props.currentFragmentMode) {
            this.props.setCurrentCategory(this.myCategory);
          } else if (this.props.potentialSemanticCriteria == CRIT_HETERONYM && !this.props.currentFragmentMode) {
            this.props.setCurrentCategory(this.myCategory);
          }

          console.log("MAGNIFICENT DEBUG2: " + this.myCategory)
          console.log("MAGNIFICENT DEBUG2.1: " + this.props.currentCategory)

          //this.props.setFragmentIndex(i);
          if (this.props.currentFragmentMode) {
            var j;
            for (j = 0; j < this.props.fragments.length; j++) {
              if (this.props.fragments[j].interId === nodeId) {
                console.log("squareGrid: because of currentFragmentMode, setFragmentIndex is now: " + j)
                this.props.setFragmentIndex(j);
                obj.fragmentIndex = j;
              }
            }
          }
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY

          this.props.setVisualizationTechnique(this.props.potentialVisualizationTechnique);
          this.props.setSemanticCriteria(this.props.potentialSemanticCriteria);

          this.props.setRecommendationArray(this.myFragmentArray);

          this.props.setRecommendationIndex(i);

          this.props.setOutOfLandingPage(true);

          console.log("MAGNIFICENT DEBUG3: " + this.myCategory)
          console.log("MAGNIFICENT DEBUG3.1: " + this.props.currentCategory)

          this.props.onChange();
        }
      }
    }
  }

  componentDidMount() {
    if (this.props.allFragmentsLoaded) {
      const data = {
        nodes: this.nodes,
        edges: this.edges
      };

      var container = document.getElementById('gridvis');
      this.network = new Network(container, data, this.options);
      var height = Math.round(window.innerHeight * 1.0) + 'px'; // The DOM way
      if (this.props.fragments.length < 100) {
        height = Math.round(window.innerHeight * 0.6) + 'px'; // The DOM way
      }
      container.style.height = height

      // container.style.height = this.nrOfLines * 50 + 'px'
      this.network.redraw();
      this.network.fit();
      this.network.on("selectNode", this.handleSelectNode);
      this.network.on("stabilizationIterationsDone", function() {
        this.network.setOptions({physics: false});
      });
    }
  }

  render() {

    let redSquareText = (<span style={{
        background: "#FF7F50",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#FF7F50",
        color: 'white'
      }}>
      <b>quadrado laranja</b>
    </span >);

    let purpleSquareText = (<span style={{
        background: "#8A2BE2",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#8A2BE2",
        color: 'white'
      }}>
      <b>quadrado roxo</b>
    </span >);

    let greySquareText = (<span style={{
        background: "#505050",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#101010",
        color: 'white'
      }}>
      <b>quadrados cinzentos</b>
    </span >);

    let goldenSquareText = (<span style={{
        background: "#DAA520",
        paddingLeft: '3px',
        paddingRight: '3px',
        border: "#DAA520",
        color: 'white'
      }}>
      <b>quadrados amarelos</b>
    </span >);

    return (<div>
      <p>
        Neste mapa, cada quadrado representa um fragmento da edição virtual do Livro do Desassossego que seleccionou.
      </p>

      <p>
        Seleccione um fragmento para leitura ao clicar num dos quadrados do mapa.
      </p>

      <p>
        Um {redSquareText}
        representará o fragmento sob o qual realizou ou está a realizar uma nova actividade.
      </p>

      <p>
        Um {purpleSquareText}
        representará o fragmento que está a ler durante a actividade actual caso navegue para um fragmento diferente do fragmento inicial que escolheu ao seleccionar uma nova actividade (o {redSquareText}).
      </p>

      <p>
        Caso esteja a realizar uma actividade que envolva datas, os {greySquareText}
        representarão fragmentos sem data.
      </p>

      <p>
        Por fim, caso esteja a realizar uma actividade que envolva categorias ou heterónimos, os {goldenSquareText}
        representarão os fragmentos correspondentes à categoria ou heterónimo.
      </p>

      <br/>

      <h4 align="center">{this.supportMessage}{this.conditionalSpace}{this.highlightText}</h4>

      <div className="graphGrid" id="gridvis"></div>

    </div>);
  }
}

const SquareGrid = connect(mapStateToProps, mapDispatchToProps)(ConnectedSquareGrid);

export default SquareGrid;
