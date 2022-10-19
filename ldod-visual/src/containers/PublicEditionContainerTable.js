import React from 'react';
import { RepositoryService } from '../services/RepositoryService';
import {
  addFragment,
  setAllFragmentsLoaded,
  setfragmentsHashMap,
  setRecommendationArray,
  setRecommendationLoaded,
  setSemanticCriteriaData,
  setPotentialSemanticCriteriaData,
  setFragmentsSortedByDate,
  setCategories,
} from '../actions/index';
import { connect } from 'react-redux';
import HashMap from 'hashmap';
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
  CRIT_WORD_RELEVANCE,
} from '../constants/history-transitions';
import { Button, ButtonToolbar, Modal } from 'react-bootstrap';
import ReactHtmlParser, {
  processNodes,
  convertNodeToElement,
  htmlparser2,
} from 'react-html-parser';
import loadingGif from '../assets/loading.gif';
import ldodIcon from '../assets/ldod-icon.png';
import loadingFragmentsGif from '../assets/fragmentload.gif';
import './PublicEditionContainerTable.css';
import ReactTable from 'react-table';
import 'react-table/react-table.css';

import { ReactTableDefaults } from 'react-table';
import 'rc-slider/assets/index.css';
import 'rc-tooltip/assets/bootstrap.css';
import ReactPlayer from 'react-player';
import ldodVideo from '../assets/video-ldod-visual-1.mp4';
import ldodThumbnail from '../assets/video-thumbnail.png';

import Slider from 'react-rangeslider';
// import 'react-rangeslider/lib/index.css'

const styles = {
  transition: 'all 0.2s ease-out',
};

function replaceSpecialChars(word) {
  let tempWord = word.toString();
  if (tempWord !== null) {
    tempWord = tempWord.replace(/ç/gi, 'c');

    tempWord = tempWord.replace(/á/gi, 'a');
    tempWord = tempWord.replace(/à/gi, 'a');
    tempWord = tempWord.replace(/ã/gi, 'a');
    tempWord = tempWord.replace(/â/gi, 'a');

    tempWord = tempWord.replace(/é/gi, 'e');
    tempWord = tempWord.replace(/è/gi, 'e');
    tempWord = tempWord.replace(/ê/gi, 'e');

    tempWord = tempWord.replace(/í/gi, 'i');
    tempWord = tempWord.replace(/ì/gi, 'i');
    tempWord = tempWord.replace(/î/gi, 'i');

    tempWord = tempWord.replace(/ò/gi, 'o');
    tempWord = tempWord.replace(/ó/gi, 'o');
    tempWord = tempWord.replace(/õ/gi, 'o');
    tempWord = tempWord.replace(/ô/gi, 'o');

    tempWord = tempWord.replace(/ù/gi, 'u');
    tempWord = tempWord.replace(/ú/gi, 'u');
    tempWord = tempWord.replace(/û/gi, 'u');

    console.log('replaceSpecialChars ' + tempWord);
  }

  return tempWord;
}

const mapStateToProps = (state) => {
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
    historyEntryCounter: state.historyEntryCounter,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    addFragment: (fragment) => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: (allFragmentsLoaded) =>
      dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setfragmentsHashMap: (fragmentsHashMap) =>
      dispatch(setfragmentsHashMap(fragmentsHashMap)),
    setRecommendationArray: (recommendationArray) =>
      dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationLoaded: (recommendationLoaded) =>
      dispatch(setRecommendationLoaded(recommendationLoaded)),
    setSemanticCriteriaData: (semanticCriteriaData) =>
      dispatch(setSemanticCriteriaData(semanticCriteriaData)),
    setPotentialSemanticCriteriaData: (potentialSemanticCriteriaData) =>
      dispatch(setPotentialSemanticCriteriaData(potentialSemanticCriteriaData)),
    setFragmentsSortedByDate: (fragmentsSortedByDate) =>
      dispatch(setFragmentsSortedByDate(fragmentsSortedByDate)),
    setCategories: (categories) => dispatch(setCategories(categories)),
  };
};

class ConnectedPublicEditionContainerTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      editionsReceived: false,
      editions: [],
      sliderNumberOfInters: 50,
      showInstructions: false,
    };

    this.expertEditionArray = [];

    this.handleButtonClick = this.handleButtonClick.bind(this);

    this.toggleInstructions = this.toggleInstructions.bind(this);
  }

  toggleInstructions() {
    this.setState({
      showInstructions: !this.state.showInstructions,
    });
  }

  addFilterPlaceholder = () => {
    const filters = document.querySelectorAll('div.rt-th > input');
    for (let filter of filters) {
      console.log('ayy: ' + filter);
      filter.placeholder = 'Filtrar...';
    }
  };

  handleOnChange = (value) => {
    this.setState({ sliderNumberOfInters: value });
  };

  componentDidMount() {
    const service = new RepositoryService();

    service.getPublicEditions().then((response) => {
      console.log(
        'ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ |' +
          response.data.map((e) =>
            console.log(
              '|Title: ' +
                e.title +
                ' |Acronym:' +
                e.acronym +
                ' |hasCategories: ' +
                e.taxonomy.hasCategories +
                ' |numberOfInters: ' +
                e.numberOfInters
            )
          )
      );
      this.setState({ editions: response.data, editionsReceived: true });
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
      console.log('editionsReceived');

      this.state.editions.map((item) => {
        if (item.type == 'perito' && this.expertEditionArray.length < 4) {
          const options = {
            decodeEntities: true,
          };

          let myTitle = ReactHtmlParser(item.title, options).toString();
          let expertObj = {
            title: myTitle,
            acronym: item.acronym,
            nrFragments: item.numberOfInters,
            navButton: (
              <Button
                bsStyle="default"
                bsSize="small"
                onClick={() => this.handleButtonClick(item)}>
                Selecionar edição
              </Button>
            ),
          };
          this.expertEditionArray.push(expertObj);

          if (this.expertEditionArray.length == 4) {
            let tempOrderedExpertEdition = [];
            let orderedCounter = 0;

            let e;
            for (e = 0; orderedCounter !== 4; e++) {
              if (this.expertEditionArray[e].acronym == 'JPC') {
                tempOrderedExpertEdition[0] = this.expertEditionArray[e];
                orderedCounter++;
              } else if (this.expertEditionArray[e].acronym == 'TSC') {
                tempOrderedExpertEdition[1] = this.expertEditionArray[e];
                orderedCounter++;
              } else if (this.expertEditionArray[e].acronym == 'RZ') {
                tempOrderedExpertEdition[2] = this.expertEditionArray[e];
                orderedCounter++;
              } else if (this.expertEditionArray[e].acronym == 'JP') {
                tempOrderedExpertEdition[3] = this.expertEditionArray[e];
                orderedCounter++;
              }
            }

            this.expertEditionArray.map((f) =>
              console.log(
                'ordering experts this.expertEditionArray;' + f.acronym
              )
            );

            tempOrderedExpertEdition.map((f) =>
              console.log(
                'ordering experts this.tempOrderedExpertEdition;' + f.acronym
              )
            );

            this.expertEditionArray = tempOrderedExpertEdition;
          }
        } else if (item.numberOfInters >= this.state.sliderNumberOfInters) {
          availableEditionsCounter++;

          let buttonStyle = 'default';
          let categoryMessage = 'Não';

          if (item.taxonomy.hasCategories) {
            categoryMessage = 'Sim';
          }

          const options = {
            decodeEntities: true,
          };

          let myTitle = ReactHtmlParser(item.title, options).toString();

          console.log(
            'nrº de caracteres: ' + myTitle.length + '| título: |' + myTitle
          );
          if (myTitle.length > titleMaxChars) {
            titleMaxChars = myTitle.length;
          }

          console.log('MytitleMaxChars: ' + titleMaxChars);

          let obj = {
            title: myTitle,
            acronym: item.acronym,
            nrFragments: item.numberOfInters,
            availableCategories: categoryMessage,
            navButton: (
              <Button
                block
                bsStyle={buttonStyle}
                bsSize="small"
                onClick={() => this.handleButtonClick(item)}>
                Selecionar edição
              </Button>
            ),
          };

          data.push(obj);
          numberOfIntersList.push(Number(obj.nrFragments));
        }
      });

      minNumberOfInters = Math.min.apply(Math, numberOfIntersList);
      maxNumberOfInters = Math.max.apply(Math, numberOfIntersList);
      console.log('minNumberOfInters: ' + minNumberOfInters);
      console.log('maxNumberOfInters: ' + maxNumberOfInters);

      let myMinWidth = Math.min(titleMaxChars * 4, 250);

      const columns = [
        {
          Header: '',
          accessor: 'navButton',
          resizable: false,
          minWidth: 70,
        },
        {
          Header: 'Título',
          accessor: 'title', // String-based value accessors!
          filterable: true,
          filterMethod: (filter, row, column) => {
            const id = filter.pivotId || filter.id;
            console.log(String(row[id]));

            let auxWord = replaceSpecialChars(row[id]);
            let auxFilter = replaceSpecialChars(filter.value);

            return row[id] !== undefined
              ? String(auxWord).toUpperCase().includes(auxFilter.toUpperCase())
              : true;
          },
          minWidth: myMinWidth,
          resizable: false,
        },
        {
          Header: 'Acrónimo',
          accessor: 'acronym',
          resizable: false,
          filterable: true,
          filterMethod: (filter, row, column) => {
            const id = filter.pivotId || filter.id;
            console.log(String(row[id]));
            return row[id] !== undefined
              ? String(row[id])
                  .toUpperCase()
                  .includes(filter.value.toUpperCase())
              : true;
          },
        },
        {
          Header: 'Categorias disponíveis',
          accessor: 'availableCategories',
          resizable: false,
          style: {
            textAlign: 'center',
          },
          filterable: true,
          Cell: ({ value }) => (value === 'Sim' ? 'Sim' : 'Não'),
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
          Filter: ({ filter, onChange }) => (
            <select
              onChange={(event) => onChange(event.target.value)}
              style={{
                width: '100%',
              }}
              value={filter ? filter.value : 'all'}>
              <option value="all">Todas</option>
              <option value="Sim">Sim</option>
              <option value="Não">Não</option>
            </select>
          ),
        },
        {
          Header: 'Número de fragmentos',
          accessor: 'nrFragments',
          resizable: false,
          style: {
            textAlign: 'center',
          },
        },
      ];

      function filterCaseInsensitive(filter, row) {
        const id = filter.pivotId || filter.id;
        return row[id] !== undefined
          ? String(row[id].toLowerCase()).startsWith(filter.value.toLowerCase())
          : true;
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
            id: 'title',
            desc: false,
          },
        ],
        defaultSortMethod: (a, b, desc) => {
          // force null and undefined to the bottom
          a = a === null || a === undefined ? '' : a;
          b = b === null || b === undefined ? '' : b;
          // force any string values to lowercase
          a = typeof a === 'string' ? a.toLowerCase() : a;
          b = typeof b === 'string' ? b.toLowerCase() : b;
          // Return either 1 or -1 to indicate a sort priority
          if (a > b) {
            return 1;
          }
          if (a < b) {
            return -1;
          }
        },

        // etc...
      });

      editionButtonList = <ReactTable />;
    } else {
      editionButtonList = (
        <div>
          <img
            src={loadingGif}
            alt="loading...publiceditiontable"
            className="loadingGifCentered"
          />
          <p align="center">
            A carregar a lista de edições públicas disponíveis...
          </p>
          <p align="center">
            Se demorar demasiado tempo, atualize a página e volte a tentar.
          </p>
        </div>
      );
    }

    const wrapperStyle = {
      width: 400,
    };

    let sliderNum = this.state.sliderNumberOfInters;

    let labelsObj = {
      0: 0,
    };

    labelsObj[maxNumberOfInters.toString()] = maxNumberOfInters;
    labelsObj[sliderNum.toString()] = sliderNum;

    let instructions = (
      <img
        src={ldodThumbnail}
        className="myVideoThumbnail"
        onClick={this.toggleInstructions}
      />
    );

    if (this.state.showInstructions) {
      instructions = (
        <div>
          <video className="myVideo" controls="controls" autoplay="autoplay">
            <source src={ldodVideo} type="video/mp4" />
          </video>
        </div>
      );
    }

    let expertEditionsCards = [];

    expertEditionsCards = this.expertEditionArray.map((obj, index) => {
      return (
        <div key={index} className="card">
          <div className="containerActivity">
            <p align="center">
              <b>{obj.title}</b>
            </p>

            <div className="welcomeButtonActivity">{obj.navButton}</div>
          </div>
        </div>
      );
    });

    return (
      <div>
        <h1 align="center">
          <b>LdoD</b>
          <img src={ldodIcon} className="ldodIcon" />
          <b>Visual</b>
        </h1>
        <h4 align="center">
          <b>Bem-vindo ao LdoD Visual! Veja o nosso vídeo de apresentação:</b>
        </h4>
        <br /> {instructions}
        <br />
        <br />
        <Modal.Footer></Modal.Footer>
        <h3 align="center">
          <b>Comece por escolher uma edição!</b>
        </h3>
        <br />
        <b>
          <p align="center">
            Recomendamos que visite este <i>website</i> num{' '}
            <i>browser desktop</i> como <i>Firefox</i> ou <i>Google Chrome</i>,{' '}
            pois não está ainda disponível uma versão <i>mobile</i>.
          </p>
        </b>
        <br />
        <p align="center">
          Alternativamente a uma edição virtual, pode escolher uma das edições
          dos peritos, cuja leitura e interação no LdoD Visual serão semelhantes
          às de uma edição virtual.
        </p>
        <p align="center">
          Relembramos que as edições dos peritos não têm categorias disponíveis
          (taxonomia).
        </p>
        <p align="center">
          Apenas se selecionar uma edição com categorias disponíveis, poderá
          realizar atividades à volta das mesmas.
        </p>
        <br />
        <h4 align="center">
          <b>Edições dos peritos:</b>
        </h4>
        <br />
        <div className="cardsContainer">{expertEditionsCards}</div>
        <br />
        <br />
        <h4 align="center">
          <b>Edições virtuais:</b>
        </h4>
        <br />
        <br />
        <p align="center">
          <i>
            Apresentar edições virtuais com um número mínimo de fragmentos de...
          </i>
        </p>
        <div className="mySlider">
          <Slider
            min={0}
            max={maxNumberOfInters}
            value={sliderNum}
            tooltip={false}
            orientation="horizontal"
            onChange={this.handleOnChange}
            labels={labelsObj}
          />
        </div>
        <div className="myTable">{editionButtonList}</div>
      </div>
    );
  }
}
const PublicEditionContainerTable = connect(
  mapStateToProps,
  mapDispatchToProps
)(ConnectedPublicEditionContainerTable);
export default PublicEditionContainerTable;
