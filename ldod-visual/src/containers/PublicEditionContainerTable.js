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
import 'rc-slider/assets/index.css';
import 'rc-tooltip/assets/bootstrap.css';
import ReactPlayer from 'react-player'
import ldodVideo from '../assets/video-ldod-visual-1.mp4'

import Slider from 'react-rangeslider'
// import 'react-rangeslider/lib/index.css'

const styles = {
  transition: 'all 0.2s ease-out'
};

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
      editions: [],
      sliderNumberOfInters: 50,
      showInstructions: false
    };

    this.handleButtonClick = this.handleButtonClick.bind(this);

    this.toggleInstructions = this.toggleInstructions.bind(this);

  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions
    });
  }

  addFilterPlaceholder = () => {
    const filters = document.querySelectorAll("div.rt-th > input");
    for (let filter of filters) {
      console.log("ayy: " + filter)
      filter.placeholder = "Filtrar...";
    }
  }

  handleOnChange = (value) => {
    this.setState({sliderNumberOfInters: value})
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

    let numberOfIntersList = [];

    let minNumberOfInters = 0;
    let maxNumberOfInters = 0;

    if (this.state.editionsReceived) {
      console.log("editionsReceived");

      this.state.editions.map(item => {

        if (item.numberOfInters >= this.state.sliderNumberOfInters) {

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

          console.log("nrº de caracteres: " + myTitle.length + "| título: |" + myTitle)
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
          numberOfIntersList.push(Number(obj.nrFragments));

        }

      });

      minNumberOfInters = Math.min.apply(Math, numberOfIntersList);
      maxNumberOfInters = Math.max.apply(Math, numberOfIntersList);
      console.log("minNumberOfInters: " + minNumberOfInters)
      console.log("maxNumberOfInters: " + maxNumberOfInters)

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
          minWidth: myMinWidth,
          resizable: false
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
          resizable: false,
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
        pageSize: availableEditionsCounter,
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
      editionButtonList = (<div><img src={loadingGif} alt="loading...publiceditiontable" className="loadingGifCentered"/>
        <p align="center">A carregar a lista de edições virtuais públicas disponíveis...</p>
        <p align="center">Se demorar demasiado tempo, actualize a página e volte a tentar.</p>
      </div>);
    }

    const wrapperStyle = {
      width: 400
    };

    let sliderNum = this.state.sliderNumberOfInters

    let labelsObj = {
      '0': 0
    }

    labelsObj[maxNumberOfInters.toString()] = maxNumberOfInters;
    labelsObj[sliderNum.toString()] = sliderNum;

    let instructions = (<div className="instructionsButton">
      <Button bsStyle="primary" bsSize="small" onClick={this.toggleInstructions}>
        Mostrar vídeo
      </Button>
    </div>)

    if (this.state.showInstructions) {

      instructions = (<div>

        <div className="instructionsButton">
          <Button bsStyle="primary" bsSize="small" onClick={this.toggleInstructions}>
            Esconder vídeo
          </Button>
        </div>

        <div className="myVideo">

          <video controls="controls">
            <source src={ldodVideo} type="video/mp4"/>
          </video>

        </div>

      </div>)
    }
    return <div>
      <img src={ldodIcon} className="loadingGifCentered"/>

      <h3 align="center">
        <b>Bem-vindo ao LdoD Visual!</b>
      </h3>

      <br/>
      <br/>

      <p align="center">Aqui poderá escolher e explorar uma das edições virtuais públicas do{" "}
        <i>"Livro do Desassossego"</i>{" "}
        disponíveis no {' '}
        <a href="https://ldod.uc.pt">
          Arquivo LdoD</a>{' '}
        recorrendo a técnicas de visualização de informação.
      </p>

      <p align="center">Poderá criar a sua própria edição virtual e consultar detalhes de outras edições virtuais públicas {' '}
        <a href="https://ldod.uc.pt/virtualeditions">
          nesta secção do Arquivo LdoD</a>.
      </p>

      <p align="center">Recomendamos que visite este{' '}
        <i>website</i>{' '}
        num{' '}
        <i>browser desktop</i>{' '}
        como{' '}
        <i>Firefox</i>{' '}
        ou{' '}
        <i>Google Chrome</i>,{' '}
        pois não está disponível uma versão{' '}
        <i>mobile</i>.
      </p>

      <br/> {instructions}

      <br/>

      <Modal.Footer></Modal.Footer>

      <h4 align="center">
        <b>Comece por escolher uma edição virtual.</b>
      </h4>

      <br/>
      <p align="center">
        Apenas se seleccionar uma edição virtual com categorias disponíveis (taxonomia), poderá realizar actividades à volta das mesmas.
      </p>

      <br/>
      <br/>

      <p align="center">
        <i>Apresentar edições virtuais com um número mínimo de fragmentos de...</i>
      </p>
      <div className="mySlider">

        <Slider min={0} max={maxNumberOfInters} value={sliderNum} tooltip={false} orientation="horizontal" onChange={this.handleOnChange} labels={labelsObj}/>

      </div>

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

// <h4 align="center">
//   <b>A proposta do LdoD Visual</b>
// </h4>
//
// <br/>
//
// <div className="landingMoreInfo">
//
//   <p>
//     Ler um livro é uma tarefa em que o leitor constantemente troca entre dois estados de concentração: ou está completamente concentrado na leitura em si ou interrompe brevemente a mesma por várias razões.
//   </p>
//   <p>
//     Estes momentos de afastamento da leitura podem ser devido a cenários como tentar relembrar o que aconteceu em capítulos anteriores, tentar imaginar uma certa descrição de uma paisagem ou personagem, lembrar uma experiência pessoal semelhante em comparação ao que foi lido, ligar um novo evento ao que aconteceu no passado ou até mesmo à sua influência no futuro, entre outras possibilidades.
//   </p>
//   <p>
//     A proposta do LdoD Visual está ligada a este fenómeno. O objectivo é materializar e dirigir este constante{" "}
//     <b>
//       <i>in</i>
//     </b>{" "}
//     e{" "}
//     <b>
//       <i>out</i>
//     </b>{" "}
//     que acontece enquanto o leitor emerge e submerge da leitura do texto.
//   </p>
//
//   <p>
//     O{" "}
//     <i>"Livro do Desassossego"</i>,{" "}
//     em comparação a um livro típico onde temos uma sequência rígida definida pelo autor, trata-se de uma obra fragmentária que pode ser lida em qualquer ordem. Isto significa que estes momentos de afastamento da leitura podem tornar-se numa oportunidade para o leitor se reposicionar noutra parte do livro. Esta nuance leva a um caminho de leitura e a uma imagem global do livro que podem variar dramaticamente de leitor para leitor, abrindo espaço para oferecer ao utilizador do LdoD Visual uma experiência onde há uma possibilidade de escolha e um papel activo na leitura do
//     <i>"Livro do Desassossego"</i>.
//   </p>
//
// </div>
//
// <br/>
//
// <h4 align="center">
//   <b>A leitura fragmentária e as actividades disponíveis no LdoD Visual</b>
// </h4>
//
// <br/>
//
// <div className="landingMoreInfo">
//
//   <p>
//     O{" "}
//     <i>"Livro do Desassossego"</i>{" "}
//     de Fernando Pessoa tem uma natureza modular - tratando-se de uma obra inacabada cujos fragmentos não têm uma ordem imposta pelo autor, a leitura desta pode também ser fragmentária.
//   </p>
//   <p>
//     Podendo os fragmentos desta obra ser lidos em qualquer ordem, o objectivo do LdoD Visual é ajudar o leitor a dirigir e a criar o seu próprio caminho de leitura do{" "}
//     <i>"Livro do Desassossego"</i>,{" "}recorrendo a um conjunto de actividades. Estas vão permitir aos leitores - peritos ou não - ler, explorar e/ou analisar a obra de forma interactiva.
//   </p>
//   <p>
//     Estas actividades envolvem técnicas de visualização de informação como nuvens de palavras, grafos de rede, cronologias, mapas personalizados e{" "}
//     <i>text skimming</i>{" "}
//     que codificam critérios como semelhança textual, ordem cronológica, taxonomia, heterónimos, relevância de palavras ou a própria ordem da edição virtual seleccionada.
//   </p>
//
// </div>