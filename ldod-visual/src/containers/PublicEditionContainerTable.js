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
import ldodIcon from '../assets/ldod-icon.png'
import loadingFragmentsGif from '../assets/fragmentload.gif'
import "./PublicEditionContainerTable.css";
import ReactTable from 'react-table';
import {ReactTableDefaults} from "react-table";

function replaceSpecialChars(word) {
  let tempWord = word.toString()
  if (tempWord !== null) {

    tempWord = tempWord.replace(/ç/gi, 'c')

    tempWord = tempWord.replace(/á/gi, 'a')
    tempWord = tempWord.replace(/à/gi, 'a')
    tempWord = tempWord.replace(/ã/gi, 'a')
    tempWord = tempWord.replace(/â/gi, 'a')

    tempWord = tempWord.replace(/é/gi, 'e')
    tempWord = tempWord.replace(/è/gi, 'e')
    tempWord = tempWord.replace(/ê/gi, 'e')

    tempWord = tempWord.replace(/í/gi, 'i')
    tempWord = tempWord.replace(/ì/gi, 'i')
    tempWord = tempWord.replace(/î/gi, 'i')

    tempWord = tempWord.replace(/ò/gi, 'o')
    tempWord = tempWord.replace(/ó/gi, 'o')
    tempWord = tempWord.replace(/õ/gi, 'o')
    tempWord = tempWord.replace(/ô/gi, 'o')

    tempWord = tempWord.replace(/ù/gi, 'u')
    tempWord = tempWord.replace(/ú/gi, 'u')
    tempWord = tempWord.replace(/û/gi, 'u')

    console.log("replaceSpecialChars " + tempWord)
  }

  return tempWord;
}

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

class ConnectedPublicEditionContainerTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      editionsReceived: false,
      editions: []
    };

    this.handleButtonClick = this.handleButtonClick.bind(this);

  }

  addFilterPlaceholder = () => {
    const filters = document.querySelectorAll("div.rt-th > input");
    for (let filter of filters) {
      console.log("ayy: " + filter)
      filter.placeholder = "Filtrar...";
    }
  }

  componentDidMount() {

    const service = new RepositoryService();

    service.getPublicEditions().then(response => {
      console.log("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ |" + response.data.map(e => console.log("|Title: " + e.title + " |Acronym:" + e.acronym + " |hasCategories: " + e.taxonomy.hasCategories + " |numberOfInters: " + e.numberOfInters)));
      this.setState({editions: response.data, editionsReceived: true});
      this.addFilterPlaceholder();
      this.props.onChange();

    });
  }

  handleButtonClick(item) {
    this.props.sendSelectedEdition(item);
  }

  render() {

    let editionButtonList;

    let data = [];

    let availableEditionsCounter = 0;

    let titleMaxChars = 0;

    if (this.state.editionsReceived) {
      console.log("editionsReceived");

      this.state.editions.map(item => {

        if (item.numberOfInters > 0) {

          availableEditionsCounter++;

          let buttonStyle = "primary";
          let categoryMessage = "Não"

          if (item.taxonomy.hasCategories) {
            categoryMessage = "Sim";
          }

          const options = {
            decodeEntities: true
          }

          let myTitle = ReactHtmlParser(item.title, options).toString();

          console.log(myTitle.length + "| mytitle |" + myTitle)
          if (myTitle.length > titleMaxChars) {
            titleMaxChars = myTitle.length;
          }

          console.log("MytitleMaxChars: " + titleMaxChars)

          let obj = {
            title: myTitle,
            acronym: item.acronym,
            nrFragments: item.numberOfInters,
            availableCategories: categoryMessage,
            navButton: (<Button block="block" bsStyle={buttonStyle} bsSize="small" onClick={() => this.handleButtonClick(item)}>
              Seleccionar edição
            </Button>)
          };

          data.push(obj);

        }
      });

      let myMinWidth = Math.min(titleMaxChars * 4, 250)

      const columns = [
        {
          Header: 'Título',
          accessor: 'title', // String-based value accessors!
          filterable: true,
          filterMethod: (filter, row, column) => {
            const id = filter.pivotId || filter.id
            console.log(String(row[id]));

            let auxWord = replaceSpecialChars(row[id]);
            let auxFilter = replaceSpecialChars(filter.value);

            return row[id] !== undefined
              ? String(auxWord).toUpperCase().includes(auxFilter.toUpperCase())
              : true
          },
          minWidth: myMinWidth
        }, {
          Header: 'Acrónimo',
          accessor: 'acronym',
          filterable: true,
          filterMethod: (filter, row, column) => {
            const id = filter.pivotId || filter.id
            console.log(String(row[id]));
            return row[id] !== undefined
              ? String(row[id]).toUpperCase().includes(filter.value.toUpperCase())
              : true
          }
        }, {
          Header: 'Categorias disponíveis',
          accessor: 'availableCategories',
          style: {
            textAlign: "center"
          },
          filterable: true,
          Cell: ({value}) => (
            value === "Sim"
            ? 'Sim'
            : 'Não'),
          filterMethod: (filter, row) => {
            if (filter.value === 'all') {
              return true;
            }
            if (filter.value === 'Sim') {
              return row[filter.id] === 'Sim';
            }
            if (filter.value === 'Não') {
              return row[filter.id] === 'Não';
            }
          },
          Filter: ({filter, onChange}) => <select onChange={event => onChange(event.target.value)} style={{
                width: '100%'
              }} value={filter
                ? filter.value
                : 'all'}>
              <option value="all">Todas</option>
              <option value="Sim">Sim</option>
              <option value="Não">Não</option>
            </select>
        }, {
          Header: 'Número de fragmentos',
          accessor: 'nrFragments',
          style: {
            textAlign: "center"
          }
        }, {
          Header: '',
          accessor: 'navButton'
        }

      ]

      function filterCaseInsensitive(filter, row) {
        const id = filter.pivotId || filter.id;
        return (
          row[id] !== undefined
          ? String(row[id].toLowerCase()).startsWith(filter.value.toLowerCase())
          : true);
      }

      Object.assign(ReactTableDefaults, {
        data: data,
        columns: columns,
        showPagination: false,
        defaultPageSize: availableEditionsCounter,
        filterable: false,
        defaultSorted: [
          {
            id: "title",
            desc: false
          }
        ],
        defaultSortMethod: (a, b, desc) => {
          // force null and undefined to the bottom
          a = a === null || a === undefined
            ? ''
            : a
          b = b === null || b === undefined
            ? ''
            : b
          // force any string values to lowercase
          a = typeof a === 'string'
            ? a.toLowerCase()
            : a
          b = typeof b === 'string'
            ? b.toLowerCase()
            : b
          // Return either 1 or -1 to indicate a sort priority
          if (a > b) {
            return 1
          }
          if (a < b) {
            return -1
          }
        }

        // etc...
      });

      editionButtonList = (<ReactTable/>)
    } else {
      editionButtonList = (<div><img src={loadingGif} alt="loading..." className="loadingGifCentered"/>
        <p align="center">A carregar a lista de edições virtuais públicas disponíveis...</p>
        <p align="center">Se demorar demasiado tempo, actualize a página e volte a tentar.</p>
      </div>);
    }

    return <div>
      <img src={ldodIcon} alt="loading..." className="loadingGifCentered"/>

      <h4 align="center">
        <b>Bem-vindo ao LdoD Visual!</b>
      </h4>

      <br/>
      <br/>

      <p align="center">Aqui poderá escolher e explorar uma das edições virtuais públicas do "Livro do Desassossego" disponíveis no {' '}
        <a href="https://ldod.uc.pt">
          Arquivo LdoD</a>.
      </p>
      <p align="center">Poderá criar a sua própria edição virtual e consultar detalhes de outras edições virtuais públicas {' '}
        <a href="https://ldod.uc.pt/virtualeditions">
          nesta secção do Arquivo LdoD</a>.
      </p>
      <p align="center">
        Apenas se seleccionar uma edição virtual com categorias disponíveis (taxonomia), poderá realizar actividades à volta das mesmas.</p>

      <div className="myTable">
        {editionButtonList}
      </div>
    </div>;
  }
}
const PublicEditionContainerTable = connect(mapStateToProps, mapDispatchToProps)(ConnectedPublicEditionContainerTable);
export default PublicEditionContainerTable;/*
      <button className="landingButton" bsStyle={buttonStyle} bsSize="small" onClick={() => this.handleButtonClick(item)}>
        Seleccionar edição
      </button>
      */
