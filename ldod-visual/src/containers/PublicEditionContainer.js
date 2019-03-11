import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import {
  addFragment,
  setAllFragmentsLoaded,
  setfragmentsHashMap,
  setRecommendationArray,
  setRecommendationLoaded,
  setSemanticCriteriaData,
  setPotentialSemanticCriteriaData,
  setFragmentsSortedByDate,
  setCategories
} from "../actions/index";
import {connect} from "react-redux";
import HashMap from "hashmap";
import {
  VIS_SQUARE_GRID,
  VIS_NETWORK_GRAPH,
  VIS_WORD_CLOUD,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_TEXT_SIMILARITY,
  CRIT_HETERONYM,
  CRIT_TAXONOMY,
  CRIT_WORD_RELEVANCE
} from "../constants/history-transitions";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import ReactHtmlParser, {processNodes, convertNodeToElement, htmlparser2} from 'react-html-parser';
import loadingGif from '../assets/loading.gif'
import loadingFragmentsGif from '../assets/fragmentload.gif'
import "./PublicEditionContainer.css";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    fragmentsHashMap: state.fragmentsHashMap,
    fragmentIndex: state.fragmentIndex,
    currentFragmentMode: state.currentFragmentMode,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationLoaded: state.recommendationLoaded,
    semanticCriteriaDataLoaded: state.semanticCriteriaDataLoaded,
    visualizationTechnique: state.visualizationTechnique,
    semanticCriteria: state.semanticCriteria,
    potentialVisualizationTechnique: state.potentialVisualizationTechnique,
    potentialSemanticCriteria: state.potentialSemanticCriteria,
    history: state.history,
    historyEntryCounter: state.historyEntryCounter
  };
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setfragmentsHashMap: fragmentsHashMap => dispatch(setfragmentsHashMap(fragmentsHashMap)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationLoaded: recommendationLoaded => dispatch(setRecommendationLoaded(recommendationLoaded)),
    setSemanticCriteriaData: semanticCriteriaData => dispatch(setSemanticCriteriaData(semanticCriteriaData)),
    setPotentialSemanticCriteriaData: potentialSemanticCriteriaData => dispatch(setPotentialSemanticCriteriaData(potentialSemanticCriteriaData)),
    setFragmentsSortedByDate: fragmentsSortedByDate => dispatch(setFragmentsSortedByDate(fragmentsSortedByDate)),
    setCategories: categories => dispatch(setCategories(categories))
  };
};

class ConnectedPublicEditionContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      editionsReceived: false,
      editions: []
    };

    this.handleButtonClick = this.handleButtonClick.bind(this);

  }

  componentDidMount() {

    const service = new RepositoryService();

    service.getPublicEditions().then(response => {
      console.log("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ |" + response.data.map(e => console.log("|Title: " + e.title + " |Acronym:" + e.acronym + " |hasCategories: " + e.taxonomy.hasCategories + " |numberOfInters: " + e.numberOfInters)));
      this.setState({editions: response.data, editionsReceived: true});
      this.props.onChange();

    });
  }

  handleButtonClick(item) {
    this.props.sendSelectedEdition(item);
  }

  render() {

    let editionButtonList = [];

    if (this.state.editionsReceived) {
      console.log("editionsReceived");

      editionButtonList = this.state.editions.map(item => {

        if (item.numberOfInters > 0) {
          let buttonStyle = "primary";
          let categoryMessage = "Não"

          if (item.taxonomy.hasCategories) {
            categoryMessage = (<b>Sim</b>)
          }

          const options = {
            decodeEntities: true
          }

          //let myTitle = ReactHtmlParser(item.title + " (" + item.numberOfInters + " fragmentos)", options);

          let myTitle = ReactHtmlParser(item.title, options);

          // return (<Button bsStyle={buttonStyle} bsSize="large" block="block" onClick={() => this.handleButtonClick(item)}>
          //   {myTitle}
          // </Button>);

          const styles = {
            width: '100%'
          };

          //<img src="img_avatar.png" alt="Avatar" style={styles}/>

          return (<div className="card">

            <div className="container">

              <p>
                <b>{myTitle}</b>
              </p>
              <p>{"Acrónimo: " + item.acronym}</p>
              <p>{"Número de fragmentos: " + item.numberOfInters}</p>
              <p>Categorias disponíveis: {categoryMessage}</p>

              <div className="welcomeButton">

                <Button bsStyle={buttonStyle} bsSize="small" onClick={() => this.handleButtonClick(item)}>
                  Seleccionar edição
                </Button>

              </div>

            </div>

          </div>)
        }
      })
    } else {
      editionButtonList = (<div><img src={loadingGif} alt="loading..." className="loadingGifCentered"/>
        <p align="center">A carregar a lista de edições virtuais públicas disponíveis...</p>
        <p align="center">Se demorar demasiado tempo, actualize a página e volte a tentar.</p>
      </div>);
    }

    return <div>
      <p>Bem-vindo ao LdoD Visual. Escolha uma das edições virtuais públicas disponíveis do Livro do Desassossego.
      </p>
      <p>Pode criar a sua própria edição virtual e consultar detalhes de outras edições virtuais públicas no{' '}
        <a href="https://ldod.uc.pt/virtualeditions">
          Arquivo LdoD</a>.
      </p>
      <p>
        Apenas se escolher uma edição virtual com categorias disponíveis (taxonomia), poderá realizar actividades à volta das mesmas.</p>

      <br></br>

      <div className="cardsContainer">
        {editionButtonList}
      </div>
    </div>;
  }

}

const PublicEditionContainer = connect(mapStateToProps, mapDispatchToProps)(ConnectedPublicEditionContainer);

export default PublicEditionContainer;

/*
<button className="landingButton" bsStyle={buttonStyle} bsSize="small" onClick={() => this.handleButtonClick(item)}>
  Seleccionar edição
</button>
*/
