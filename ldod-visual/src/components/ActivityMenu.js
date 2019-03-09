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

import picNetgraphGray from '../assets/card-pics-gray/netgraph-gray.png';
import picSquareGray from '../assets/card-pics-gray/square-gray.png';
import picSquareGoldenGray from '../assets/card-pics-gray/square-golden-gray.png';
import picSquareTimeGray from '../assets/card-pics-gray/square-time-gray.png';
import picWordCloudGray from '../assets/card-pics-gray/word-cloud-gray.png';

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
      show: true
    };

    this.retreatButton = (<div/>);

    this.toggleActivityNetworkGraphTextSimilarity = this.toggleActivityNetworkGraphTextSimilarity.bind(this);

    this.toggleActivityNetworkGraphHeteronym = this.toggleActivityNetworkGraphHeteronym.bind(this);

    this.toggleActivityNetworkGraphDate = this.toggleActivityNetworkGraphDate.bind(this);

    this.toggleActivityNetworkGraphTaxonomy = this.toggleActivityNetworkGraphTaxonomy.bind(this);

    this.toggleSquareGridEditionOrder = this.toggleSquareGridEditionOrder.bind(this);

    this.toggleSquareGridHeteronym = this.toggleSquareGridHeteronym.bind(this);

    this.toggleSquareGridDateOrder = this.toggleSquareGridDateOrder.bind(this);

    this.toggleWordCloudTaxonomy = this.toggleWordCloudTaxonomy.bind(this);

    this.handleActivitySelectRetreat = this.handleActivitySelectRetreat.bind(this);

    this.toggleWordCloudTaxonomySingleFragment = this.toggleWordCloudTaxonomySingleFragment.bind(this);

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

  toggleActivityNetworkGraphDate() {

    if (this.props.datesExist & this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {

      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {

        this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
        this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
        this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
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

    this.props.setPotentialVisualizationTechnique(VIS_SQUARE_GRID);
    this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
    this.activityToRender = (<SquareGrid onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
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
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  render() {

    if (this.state.show) {

      let categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia)"
      let networkGraphTaxonomyMessage = "Ler fragmentos semelhantes a este por taxonomia"
      let categoryButtonStyle = "primary"
      let categoryImage = picWordCloud;
      let networkGraphTaxonomyImage = picNetgraph;
      if (this.props.categories.length === 0) {
        categoryButtonStyle = "secondary";
        categoryButtonMessage = "Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia) (edição sem taxonomia)"
        networkGraphTaxonomyMessage = "Ler fragmentos semelhantes a este por taxonomia (edição sem taxonomia)"
        networkGraphTaxonomyImage = picNetgraphGray;
        categoryImage = picWordCloudGray;
      }

      //toggleSquareGridHeteronym
      let heteronymButtonStyle = "secondary";
      let myHeteronym = "heterónimo não disponível para este fragmento";
      let heteronymImage = picSquareGoldenGray;
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym) {
        heteronymButtonStyle = "primary";
        myHeteronym = this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym;
        heteronymImage = picSquareGolden;
      }

      let datesButtonStyle = "primary"
      let datesImage = picSquareTime;
      let datesButtonFunction = this.toggleSquareGridDateOrder;
      let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";

      let datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (fragmento actual sem data disponível)"
      let datesSimilarButtonStyle = "secondary"
      let datesSimilarImage = picNetgraphGray;
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (" + this.props.recommendationArray[this.props.recommendationIndex].meta.date + ")"
        datesSimilarButtonStyle = "primary";
        datesSimilarImage = picNetgraph;
      }

      if (!this.props.datesExist) {
        datesButtonStyle = "secondary";
        datesImage = picSquareTimeGray;
        datesSimilarButtonStyle = "secondary";
        datesButtonFunction = function() {}
        datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data (edição virtual sem datas)"
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (edição virtual sem datas)"
      }

      let wordCloudSingleFragmentMessage = "Explorar mais fragmentos da(s) mesma(s) categoria(s) deste fragmento";
      let wordCloudSingleFragmentButtonStyle = "primary";
      let wordCloudSingleFragmentImage = picWordCloud;

      // console.log("blebleblebleble")
      // console.log(this.props.recommendationArray[this.props.recommendationIndex].meta.categories.length)

      if (this.props.recommendationArray[this.props.recommendationIndex].meta.categories.length == 0) {
        wordCloudSingleFragmentMessage = "Explorar mais fragmentos da(s) mesma(s) categoria(s) deste fragmento (fragmento actual sem categorias)";
        wordCloudSingleFragmentButtonStyle = "secondary";
        wordCloudSingleFragmentImage = picWordCloudGray;
      }

      this.retreatButton = (<div/>);

      this.activityToRender = (<div>

        <p>Caso tenha seleccionado uma edição virtual sem taxonomia, ou categorias, não será possível realizar actividades que dependem das mesmas, que estarão devidamente assinaladas a cinzento. O mesmo se aplicará para a ausência de datas ou se o fragmento que está a ler actualmente não for assinado por qualquer heterónimo - as actividades em torno dessa informação estarão indisponíveis.
        </p>

        <div className="cardsContainerActivity">

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={picSquare} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>Explorar os fragmentos por ordem desta edição virtual</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle="primary" bsSize="small" onClick={this.toggleSquareGridEditionOrder}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={datesImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>{datesButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={datesButtonStyle} bsSize="small" onClick={datesButtonFunction}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={heteronymImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>Explorar mais fragmentos assinados pelo mesmo heterónimo deste fragmento ({myHeteronym})</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={heteronymButtonStyle} bsSize="small" onClick={this.toggleSquareGridHeteronym}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={categoryImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>{categoryButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={this.toggleWordCloudTaxonomy}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={wordCloudSingleFragmentImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>{wordCloudSingleFragmentMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={wordCloudSingleFragmentButtonStyle} bsSize="small" onClick={this.toggleWordCloudTaxonomySingleFragment}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={picNetgraph} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>Ler fragmentos semelhantes a este por semelhança de texto</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle="primary" bsSize="small" onClick={this.toggleActivityNetworkGraphTextSimilarity}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={datesSimilarImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>{datesSimilarButtonMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={datesSimilarButtonStyle} bsSize="small" onClick={this.toggleActivityNetworkGraphDate}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

          <div className="cardActivity">
            <div className="containerActivity">
              <img src={networkGraphTaxonomyImage} className="cardsContainerActivity" alt="Avatar" style={{
                  width: "100%"
                }}/>
              <p align="center">
                <b>{networkGraphTaxonomyMessage}</b>
              </p>
              <div className="welcomeButtonActivity">
                <Button bsStyle={categoryButtonStyle} bsSize="small" onClick={this.toggleActivityNetworkGraphTaxonomy}>
                  Escolher actividade
                </Button>
              </div>
            </div>
          </div>

        </div>

      </div>);
    } else {
      this.activityToRender = this.activityToRender; //(<NetworkGraphContainer pFragmentId={this.props.recommendationArray[this.props.recommendationIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
      this.retreatButton = (<Button bsStyle="primary" bsSize="large" onClick={this.handleActivitySelectRetreat} block="block">
        ← Seleccionar outra actividade
      </Button>);

    }

    return <div className="activityMenu">{this.activityToRender}{this.retreatButton}</div>;
  }
}

const ActivityMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedActivityMenu);

export default ActivityMenu;
