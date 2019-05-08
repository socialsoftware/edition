import React, {Component} from "react";
import "./ActivityMenu.css";
import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import NetworkGraphContainer from "../containers/NetworkGraphContainer";
import {setCurrentVisualization, setPotentialVisualizationTechnique, setPotentialSemanticCriteria, setSemanticCriteriaDataLoaded, setDisplayTextSkimming} from "../actions/index";
import SquareGrid from "../components/SquareGrid";
import MyWordCloud from "../components/MyWordCloud";
import MyHistory from "../components/MyHistory";

import {
  VIS_SQUARE_GRID,
  VIS_NETWORK_GRAPH,
  VIS_TIMELINE,
  VIS_WORD_CLOUD,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_TEXT_SIMILARITY,
  CRIT_HETERONYM,
  CRIT_CATEGORY,
  CRIT_TAXONOMY,
  CRIT_WORD_RELEVANCE
} from "../constants/history-transitions";

import picNetgraph from '../assets/card-pics-regular/netgraph.png';
import picSquare from '../assets/card-pics-regular/square.png';
import picSquareGolden from '../assets/card-pics-regular/square-golden.png';
import picSquareTime from '../assets/card-pics-regular/square-time.png';
import picWordCloud from '../assets/card-pics-regular/word-cloud.png';
import picTimeline from '../assets/card-pics-regular/timeline.png';
import picWordCloudSingular from '../assets/card-pics-regular/word-cloud-singular.png';

import picNetgraphGray from '../assets/card-pics-gray/netgraph-gray.png';
import picSquareGray from '../assets/card-pics-gray/square-gray.png';
import picSquareGoldenGray from '../assets/card-pics-gray/square-golden-gray.png';
import picSquareTimeGray from '../assets/card-pics-gray/square-time-gray.png';
import picWordCloudGray from '../assets/card-pics-gray/word-cloud-gray.png';
import picTimelineGray from '../assets/card-pics-gray/timeline-gray.png';
import picWordCloudSingularGray from '../assets/card-pics-gray/word-cloud-singular-gray.png';

const mapDispatchToProps = dispatch => {
  return {
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    setPotentialVisualizationTechnique: potentialVisualizationTechnique => dispatch(setPotentialVisualizationTechnique(potentialVisualizationTechnique)),
    setPotentialSemanticCriteria: potentialSemanticCriteria => dispatch(setPotentialSemanticCriteria(potentialSemanticCriteria)),
    setSemanticCriteriaDataLoaded: semanticCriteriaDataLoaded => dispatch(setSemanticCriteriaDataLoaded(semanticCriteriaDataLoaded)),
    setDisplayTextSkimming: displayTextSkimming => dispatch(setDisplayTextSkimming(displayTextSkimming))
  };
};

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    recommendationLoaded: state.recommendationLoaded,
    displayTextSkimming: state.displayTextSkimming,
    categories: state.categories,
    datesExist: state.datesExist,
    fragmentsSortedByDate: state.fragmentsSortedByDate
  };
};

class ConnectedActivityMenu extends Component {
  constructor(props) {
    super(props);

    this.activityToRender = (<div></div>);
    this.state = {
      show: true,
      showInstructions: false
    };

    this.retreatButton = (<div/>);

    this.toggleActivityNetworkGraphTextSimilarity = this.toggleActivityNetworkGraphTextSimilarity.bind(this);

    this.toggleActivityNetworkGraphHeteronym = this.toggleActivityNetworkGraphHeteronym.bind(this);

    this.toggleActivityMyHistoryDate = this.toggleActivityMyHistoryDate.bind(this);

    this.toggleActivityNetworkGraphTaxonomy = this.toggleActivityNetworkGraphTaxonomy.bind(this);

    this.toggleSquareGridEditionOrder = this.toggleSquareGridEditionOrder.bind(this);

    this.toggleSquareGridHeteronym = this.toggleSquareGridHeteronym.bind(this);

    this.toggleSquareGridDateOrder = this.toggleSquareGridDateOrder.bind(this);

    this.toggleWordCloudTaxonomy = this.toggleWordCloudTaxonomy.bind(this);

    this.handleActivitySelectRetreat = this.handleActivitySelectRetreat.bind(this);

    this.toggleWordCloudTaxonomySingleFragment = this.toggleWordCloudTaxonomySingleFragment.bind(this);

    this.toggleInstructions = this.toggleInstructions.bind(this);

  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions
    });
  }

  toggleActivityNetworkGraphTextSimilarity() {
    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_TEXT_SIMILARITY);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleActivityNetworkGraphHeteronym() {

    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_HETERONYM);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleActivityMyHistoryDate() {

    if (this.props.datesExist & this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {

      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {

        this.props.setPotentialVisualizationTechnique(VIS_TIMELINE);
        this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
        this.activityToRender = (<MyHistory onChange={this.props.onChange}/>);
        this.setState(prevState => ({
          show: !prevState.show
        }));
      }
    }
  }

  toggleActivityNetworkGraphTaxonomy() {

    if (this.props.categories.length !== 0) {

      this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
      this.props.setPotentialSemanticCriteria(CRIT_TAXONOMY);
      this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
      this.setState(prevState => ({
        show: !prevState.show
      }));
    }
  }

  toggleSquareGridEditionOrder() {

    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_EDITION_ORDER);
    this.activityToRender = (<SquareGrid onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleSquareGridDateOrder() {

    if (this.props.datesExist) {

      this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
      this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
      this.activityToRender = (<SquareGrid onChange={this.props.onChange}/>);
      this.setState(prevState => ({
        show: !prevState.show
      }));
    }
  }

  toggleWordCloudTaxonomy() {

    if (this.props.categories.length !== 0) {

      this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
      this.props.setPotentialSemanticCriteria(CRIT_CATEGORY);
      this.activityToRender = (<MyWordCloud onChange={this.props.onChange}/>);
      this.setState(prevState => ({
        show: !prevState.show
      }));
    }
  }

  toggleWordCloudTaxonomySingleFragment() {

    if (this.props.categories.length !== 0 & this.props.recommendationArray[this.props.recommendationIndex].meta.categories.length !== 0) {

      this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
      this.props.setPotentialSemanticCriteria(CRIT_CATEGORY);
      this.activityToRender = (<MyWordCloud onChange={this.props.onChange} singleFragmentCategory={true}/>);
      this.setState(prevState => ({
        show: !prevState.show
      }));
    }
  }

  toggleSquareGridHeteronym() {

    if (this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym) {

      this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
      this.props.setPotentialSemanticCriteria(CRIT_HETERONYM);
      this.activityToRender = (<SquareGrid onChange={this.props.onChange}/>);
      this.setState(prevState => ({
        show: !prevState.show
      }));
    }
  }

  handleActivitySelectRetreat() {
    if (!this.state.show) {
      this.setState({show: true})
    }
  }

  _handleKeyDownActivity = (event) => {

    var ESCAPE_KEY = 27;
    var RIGHT_ARROW = 39;
    var LEFT_ARROW = 37;
    var A_KEY = 65;
    var N_KEY = 78;
    var H_KEY = 72;
    var E_KEY = 69;
    var D_KEY = 68;
    var T_KEY = 84;
    var C_KEY = 67;
    var X_KEY = 88;
    var O_KEY = 79;
    var R_KEY = 82;
    var V_KEY = 86;
    var I_KEY = 73;

    switch (event.keyCode) {
      case E_KEY:
        this.toggleSquareGridEditionOrder();
        break;
      case D_KEY:
        this.toggleSquareGridDateOrder();
        break;
      case H_KEY:
        this.toggleSquareGridHeteronym();
        break;
      case T_KEY:
        this.toggleWordCloudTaxonomy();
        break;
      case C_KEY:
        this.toggleWordCloudTaxonomySingleFragment();
        break;
      case V_KEY:
        this.toggleActivityMyHistoryDate();
        break;
      case X_KEY:
        this.toggleActivityNetworkGraphTextSimilarity();
        break;
      case O_KEY:
        this.toggleActivityNetworkGraphTaxonomy();
        break;
      case R_KEY:
        this.handleActivitySelectRetreat();
        break;
      case I_KEY:
        this.toggleInstructions();
        break;
      default:
        break;
    }

  }

  componentDidMount() {
    //window.addEventListener('scroll', this.listenScrollEvent)
    document.addEventListener("keydown", this._handleKeyDownActivity);
  }

  render() {

    const activitySelectable = "Escolher atividade";
    const activityUnselectable = "Atividade indisponível";

    let instructions = (<div className="instructionsButton">
      <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
        Mostrar instruções [i]
      </Button>
    </div>)

    if (this.state.showInstructions) {
      instructions = (<div className="readingMoreInfo">
        <p align="center">Este é o menu de nova atividade.</p>
        <lu>
          <li>
            Aqui poderá realizar uma de várias atividades clicando na respectiva imagem, botão ou tecla do seu teclado assinalada na etiqueta do botão (ex: tecla "E" para a primeira atividade).
          </li>
          <li>
            Note que todas as atividades que envolvam quadrados, círculos ou cronologia irão envolver um elemento cor-de-laranja que representa o fragmento que está a ler atualmente e à volta do qual deseja fazer a nova atividade.
          </li>
        </lu>

        <div className="instructionsButton">

          <Button bsStyle="default" bsSize="small" onClick={this.toggleInstructions}>
            Esconder instruções [i]
          </Button>

        </div>
      </div>)

    }

    if (this.state.show) {

      let categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia)"
      let networkGraphTaxonomyMessage = "Ler fragmentos semelhantes a este por taxonomia"
      let categoryButtonStyle = "default"
      let categoryButtonBotMessage = activitySelectable;
      let categoryImage = picWordCloud;
      let networkGraphTaxonomyImage = picNetgraph;
      if (this.props.categories.length === 0) {
        categoryButtonStyle = "default disabled";
        categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia) (edição sem taxonomia)"
        networkGraphTaxonomyMessage = "Ler fragmentos semelhantes a este por taxonomia (edição sem taxonomia)"
        networkGraphTaxonomyImage = picNetgraphGray;
        categoryImage = picWordCloudGray;
        categoryButtonBotMessage = activityUnselectable;
      }

      //toggleSquareGridHeteronym
      let heteronymButtonStyle = "default disabled";
      let heteronymButtonBotMessage = activityUnselectable;
      let myHeteronym = "heterónimo não disponível para este fragmento";
      let heteronymImage = picSquareGoldenGray;
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym) {
        heteronymButtonStyle = "default";
        myHeteronym = this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym;
        heteronymImage = picSquareGolden;
        heteronymButtonBotMessage = activitySelectable;
      }

      let datesButtonStyle = "default"
      let datesImage = picSquareTime;
      let datesButtonFunction = this.toggleSquareGridDateOrder;
      let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";
      let datesButtonBotMessage = activitySelectable;

      let datesSimilarButtonMessage = "Explorar fragmentos à volta da data deste fragmento (fragmento atual sem data disponível)"
      let datesSimilarButtonStyle = "default disabled"
      let datesSimilarImage = picTimelineGray;
      let datesSimilarButtonBotMessage = activityUnselectable;
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {
        datesSimilarButtonMessage = "Explorar fragmentos à volta da data deste fragmento (" + this.props.recommendationArray[this.props.recommendationIndex].meta.date + ")"
        datesSimilarButtonStyle = "default";
        datesSimilarImage = picTimeline;
        datesSimilarButtonBotMessage = activitySelectable;
      }

      if (!this.props.datesExist) {
        datesButtonStyle = "default disabled";
        datesButtonBotMessage = activityUnselectable;
        datesImage = picSquareTimeGray;
        datesSimilarButtonStyle = "default disabled";
        datesButtonFunction = function() {}
        datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data (edição sem datas)"
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (edição sem datas)"
      }

      let wordCloudSingleFragmentMessage = "Explorar mais fragmentos da(s) mesma(s) categoria(s) deste fragmento";
      let wordCloudSingleFragmentButtonStyle = "default";
      let wordCloudSingleFragmentImage = picWordCloudSingular;
      let wordCloudSingleBotMessage = activitySelectable;

      // console.log("blebleblebleble")
      // console.log(this.props.recommendationArray[this.props.recommendationIndex].meta.categories.length)

      if (this.props.recommendationArray[this.props.recommendationIndex].meta.categories.length == 0) {
        wordCloudSingleFragmentMessage = "Explorar mais fragmentos da(s) mesma(s) categoria(s) deste fragmento (fragmento atual sem categorias)";
        wordCloudSingleFragmentButtonStyle = "default disabled";
        wordCloudSingleFragmentImage = picWordCloudSingularGray;
        wordCloudSingleBotMessage = activityUnselectable;
      }

      this.retreatButton = (<div/>);

      this.activityToRender = (<div>

        {instructions}

        <br/>

        <p align="center">
          <b>Atividades a partir da edição escolhida:</b>
        </p>

        <div className="cardsContainerActivity">

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={picSquare} onClick={this.toggleSquareGridEditionOrder} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>Explorar os fragmentos por ordem desta edição</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle="default" bsSize="small" onClick={this.toggleSquareGridEditionOrder}>
                  {activitySelectable + " [E]"}
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={datesImage} onClick={datesButtonFunction} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>{datesButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={datesButtonStyle} bsSize="small" onClick={datesButtonFunction}>
                  {datesButtonBotMessage + " [D]"}
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={categoryImage} onClick={this.toggleWordCloudTaxonomy} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>{categoryButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={this.toggleWordCloudTaxonomy}>
                  {categoryButtonBotMessage + " [T]"}
                </Button>
              </div>
            </div>
          </div>

        </div>

        <br/>

        <p align="center">
          <b>Atividades a partir do fragmento atual:</b>
        </p>

        <div className="cardsContainerActivity">

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={datesSimilarImage} onClick={this.toggleActivityMyHistoryDate} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>{datesSimilarButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={datesSimilarButtonStyle} bsSize="small" onClick={this.toggleActivityMyHistoryDate}>
                  {datesSimilarButtonBotMessage + " [V]"}
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={picNetgraph} onClick={this.toggleActivityNetworkGraphTextSimilarity} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>Ler fragmentos semelhantes a este por semelhança de texto</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle="default" bsSize="small" onClick={this.toggleActivityNetworkGraphTextSimilarity}>
                  Escolher atividade [X]
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={wordCloudSingleFragmentImage} onClick={this.toggleWordCloudTaxonomySingleFragment} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>{wordCloudSingleFragmentMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={wordCloudSingleFragmentButtonStyle} bsSize="small" onClick={this.toggleWordCloudTaxonomySingleFragment}>
                  {wordCloudSingleBotMessage + " [C]"}
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={networkGraphTaxonomyImage} onClick={this.toggleActivityNetworkGraphTaxonomy} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>{networkGraphTaxonomyMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={this.toggleActivityNetworkGraphTaxonomy}>
                  {categoryButtonBotMessage + " [O]"}
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={heteronymImage} onClick={this.toggleSquareGridHeteronym} className="cardsActivityImage" alt="Avatar" style={{
                  width: "80%"
                }}/>
              <p align="center">
                <b>Explorar mais fragmentos assinados pelo mesmo heterónimo deste fragmento ({myHeteronym})</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={heteronymButtonStyle} bsSize="small" onClick={this.toggleSquareGridHeteronym}>
                  {heteronymButtonBotMessage + " [H]"}
                </Button>
              </div>
            </div>
          </div>

        </div>

      </div>);
    } else {
      this.activityToRender = this.activityToRender; //(<NetworkGraphContainer pFragmentId={this.props.recommendationArray[this.props.recommendationIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
      this.retreatButton = (<div className="newActRetreatButton">
        <Button bsStyle="default" onClick={this.handleActivitySelectRetreat}>
          ← Retroceder para outra atividade [R]
        </Button>
      </div>);

    }

    return <div className="activityMenu">{this.activityToRender}{this.retreatButton}</div>;
  }
}

const ActivityMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedActivityMenu);

export default ActivityMenu;

//         <p>Caso tenha selecionado uma edição sem taxonomia, ou categorias, não será possível realizar atividades que dependem das mesmas, que estarão devidamente assinaladas a cinzento. O mesmo se aplicará para a ausência de datas ou se o fragmento que está a ler atualmente não for assinado por qualquer heterónimo - as atividades em torno dessa informação estarão indisponíveis.
//         </p>