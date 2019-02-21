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

    this.toggleActivityNetworkGraphTextSimilarity = this.toggleActivityNetworkGraphTextSimilarity.bind(this);

    this.toggleActivityNetworkGraphHeteronym = this.toggleActivityNetworkGraphHeteronym.bind(this);

    this.toggleActivityNetworkGraphDate = this.toggleActivityNetworkGraphDate.bind(this);

    this.toggleActivityNetworkGraphTaxonomy = this.toggleActivityNetworkGraphTaxonomy.bind(this);

    this.toggleSquareGridEditionOrder = this.toggleSquareGridEditionOrder.bind(this);

    this.toggleSquareGridHeteronym = this.toggleSquareGridHeteronym.bind(this);

    this.toggleSquareGridDateOrder = this.toggleSquareGridDateOrder.bind(this);

    this.toggleWordCloudTaxonomy = this.toggleWordCloudTaxonomy.bind(this);

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

    if (this.props.datesExist) {

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

  render() {

    if (this.state.show) {

      let categoryButtonStyle = "primary"
      if (this.props.categories.length === 0) {
        categoryButtonStyle = "secondary";
      }

      let heteronymButtonStyle = "secondary";
      let myHeteronym = "heterónimo não disponível para este fragmento";
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym) {
        heteronymButtonStyle = "primary";
        myHeteronym = this.props.recommendationArray[this.props.recommendationIndex].meta.heteronym;
      }

      let datesButtonStyle = "primary"
      let datesButtonFunction = this.toggleSquareGridDateOrder;
      let datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data";

      let datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data"
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date !== null) {
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (" + this.props.recommendationArray[this.props.recommendationIndex].meta.date + ")"
      }
      let datesSimilarButtonStyle = "primary"

      if (!this.props.datesExist) {
        datesButtonStyle = "secondary";
        datesSimilarButtonStyle = "secondary";
        datesButtonFunction = function() {}
        datesButtonMessage = "Explorar os fragmentos desta edição ordenados por data (edição virtual sem datas)"
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (edição virtual sem datas)"
      }
      if (this.props.recommendationArray[this.props.recommendationIndex].meta.date == null) {
        datesSimilarButtonMessage = "Ler fragmentos semelhantes a este por data (fragmento actual sem datas)"
        datesSimilarButtonStyle = "secondary";
      }

      this.activityToRender = (<div>
        <p>Caso tenha seleccionado uma edição virtual sem taxonomia, ou categorias, não será possível realizar actividades que dependem das mesmas, que estarão devidamente assinaladas a cinzento. O mesmo se aplicará para a ausência de datas ou se o fragmento que está a ler actualmente não for assinado por qualquer heterónimo - as actividades em torno dessa informação estarão indisponíveis.
        </p>

        <ButtonToolbar>

          <Button bsStyle="primary" bsSize="large" onClick={this.toggleActivityNetworkGraphTextSimilarity} block="block">
            Ler fragmentos semelhantes a este por semelhança de texto
          </Button>

          <Button bsStyle={datesSimilarButtonStyle} bsSize="large" onClick={this.toggleActivityNetworkGraphDate} block="block">
            {datesSimilarButtonMessage}
          </Button>

          <Button bsStyle={categoryButtonStyle} bsSize="large" onClick={this.toggleActivityNetworkGraphTaxonomy} block="block">
            Ler fragmentos semelhantes a este por taxonomia
          </Button>

          <Button bsStyle="primary" bsSize="large" onClick={this.toggleSquareGridEditionOrder} block="block">
            Explorar os fragmentos por ordem desta edição virtual
          </Button>

          <Button bsStyle={datesButtonStyle} bsSize="large" onClick={datesButtonFunction} block="block">
            {datesButtonMessage}
          </Button>

          <Button bsStyle={heteronymButtonStyle} bsSize="large" onClick={this.toggleSquareGridHeteronym} block="block">
            Explorar mais fragmentos assinados pelo mesmo heterónimo ({myHeteronym})
          </Button>

          <Button bsStyle={categoryButtonStyle} bsSize="large" onClick={this.toggleWordCloudTaxonomy} block="block">
            Explorar os fragmentos desta edição pelas categorias a que pertencem (taxonomia)
          </Button>

        </ButtonToolbar>
      </div>);
    } else {
      this.activityToRender = this.activityToRender; //(<NetworkGraphContainer pFragmentId={this.props.recommendationArray[this.props.recommendationIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
    }

    return <div className="activityMenu">{this.activityToRender}</div>;

  }
}

//<Modal.Footer></Modal.Footer>
// <Button bsStyle="primary" bsSize="large" onClick={this.toggleActivityNetworkGraphHeteronym} block="block">
//   Ler fragmentos semelhantes a este por heterónimo
// </Button>

const ActivityMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedActivityMenu);

export default ActivityMenu;
