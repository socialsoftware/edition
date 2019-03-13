import React from 'react';
import {connect} from "react-redux";
import './Fragment.css';
import {RepositoryService} from "../services/RepositoryService";
import HashMap from "hashmap";
import ReactHtmlParser, {processNodes, convertNodeToElement, htmlparser2} from 'react-html-parser';
import ReactDOMServer from 'react-dom/server';
import loadingGif from '../assets/loading.gif';

const styles = {
  transition: 'all 0.12s ease-out'
};

const mapStateToProps = state => {
  return {
    fragmentIndex: state.fragmentIndex,
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    recommendationLoaded: state.recommendationLoaded,
    displayTextSkimming: state.displayTextSkimming,
    datesExist: state.datesExist
  };
};

function replaceSpecialChars(word) {
  let tempWord = word.toString().toLowerCase();
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

    tempWord = tempWord.replace(/\!/gi, '')
    tempWord = tempWord.replace(/\?/gi, '')
    tempWord = tempWord.replace(/\,/gi, '')
    tempWord = tempWord.replace(/\./gi, '')

    //console.log("replaceSpecialChars " + tempWord)
  }
  return tempWord;
}

export class ConnectedFragment extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      toggleTextSkimmingUpdate: false,
      opacity: 1
    };

    this.fragTextMap = new HashMap();

    this.oldTfIdfDataMap = new HashMap();
    this.currentFragmentTfIdfMap = new HashMap();
    this.lowestTfIdfValueMap = new HashMap();

    this.min = 1000;
    this.max = 0.000001;
    this.truncateCounter = 0;
    this.TfIdfRequestCounter = 0;
  }

  componentDidUpdate(prevProps) {
    console.log("fragment.js: componentDidUpdate 1");
    if (this.props.recommendationIndex !== prevProps.recommendationIndex) {
      console.log("fragment.js: componentDidUpdate 2");
      this.setState({opacity: 0});

      setTimeout(function() {
        this.setState({opacity: 1});
      }.bind(this), 150);

    }
  }

  render() {

    ("Fragment.js: render()")

    let fragmentToRender;
    let textToDisplay = (<div><img src={loadingGif} alt="loading..." className="loadingGifCentered"/>
    </div>);
    let stringArray;

    if (this.props.allFragmentsLoaded && this.props.outOfLandingPage && this.props.recommendationLoaded) {

      console.log("XPTZ DATE: " + this.props.recommendationArray[this.props.recommendationIndex].meta.date);
      console.log("XPTZ DATE EXISTS: " + this.props.datesExist)

      if (this.props.toggleTextSkimming) {
        console.log("Fragment.js: => app.js wants toggleTextSkimming active!");

        let currentlyDisplayedFragmentId = this.props.recommendationArray[this.props.recommendationIndex].interId;

        if (!this.oldTfIdfDataMap.has(currentlyDisplayedFragmentId)) {
          console.log("Fragment.js: !this.oldTfIdfDataMap.has(currentlyDisplayedFragmentId) => New TFIDF info needed, requesting TF-IDF for fragId " + currentlyDisplayedFragmentId);

          const service = new RepositoryService(this.props.currentEdition.acronym);

          service.getFragmentTfIdf(currentlyDisplayedFragmentId).then(response => {

            console.log("TF-IDF received for fragId " + currentlyDisplayedFragmentId);
            console.log(response.data);

            let distinctTfIdfValues = 0;
            let temp;

            let stopWords = ["e", "a", "to", "of", "the"];

            let myMap = new HashMap();
            response.data.map(function(d) {
              if (!stopWords.includes(Object.keys(d)[0].toString())) {
                console.log("response tf-idf data: " + Object.keys(d)[0].toLowerCase() + " " + Object.values(d)[0]);
                myMap.set(Object.keys(d)[0].toLowerCase(), Object.values(d)[0]);

                if (temp !== Object.values(d)[0]) {
                  temp = Object.values(d)[0];
                  distinctTfIdfValues++;
                }
              } else {
                console.log("ignoring TF-IDF stopword: " + Object.keys(d)[0]);
              }
            }.bind(this));

            let obj = {
              low: Object.values(response.data[0])[0],
              len: distinctTfIdfValues
            }

            this.lowestTfIdfValueMap.set(currentlyDisplayedFragmentId, obj);

            this.oldTfIdfDataMap.set(currentlyDisplayedFragmentId, myMap);

            this.TfIdfRequestCounter = this.TfIdfRequestCounter + 1;

            this.setState({
              toggleTextSkimmingUpdate: !this.state.toggleTextSkimmingUpdate
            });

          });

        } else {
          console.log("Fragment.js: => TF-IDF info already loaded. Using TF-IDF info in hashmap.");
          console.log(this.oldTfIdfDataMap.get(currentlyDisplayedFragmentId));

          let dataMap;
          let wordsTfIdfMap = this.oldTfIdfDataMap.get(currentlyDisplayedFragmentId);

          let lowestTfIdfValue = this.lowestTfIdfValueMap.get(currentlyDisplayedFragmentId).low;
          let tfIdfLen = this.lowestTfIdfValueMap.get(currentlyDisplayedFragmentId).len;

          console.log("TF-IDF xxx LOWEST VALUE: " + lowestTfIdfValue);
          console.log("TF-IDF Xxx LEN: " + tfIdfLen);

          var str = this.props.recommendationArray[this.props.recommendationIndex].text;

          //stringArray = str.split(" ");
          stringArray = str.split("<");
          stringArray = str.split(">");
          stringArray = str.split(",");
          stringArray = str.split(".");
          stringArray = str.split(/(\s+)/);
          //console.log(str);
          //stringArray = str.split(/ (?=[^>]*(?:<|$))/);

          //stringArray = str.match(/<\s*(\w+\b)(?:(?!<\s*\/\s*\1\b)[\s\S])*<\s*\/\s*\1\s*>|\S+/g);

          //console.log("ZZZZZZZZZZZZZZZZZZZZ ")
          //console.log(stringArray);

          let outOfTag = true;
          let boldWeight = 1;
          let w;
          for (w = 0; w < stringArray.length; w++) {

            if (stringArray[w] === "<") {
              outOfTag = false;
            } else if (stringArray[w] === ">") {
              outOfTag = true;
            }

            let wordToCompare = replaceSpecialChars(stringArray[w].toLowerCase());

            if (wordsTfIdfMap.has(wordToCompare) && outOfTag && wordToCompare !== "e" && wordToCompare !== "a") {

              if (parseFloat(wordsTfIdfMap.get(wordToCompare)) !== lowestTfIdfValue || tfIdfLen == 1) {

                console.log("TF-IDF FOUND WORD: " + stringArray[w]);

                let tfIdf = parseFloat(wordsTfIdfMap.get(replaceSpecialChars(stringArray[w].toLowerCase())));
                if (tfIdf < this.min) {
                  this.min = tfIdf
                }
                if (tfIdf > this.max) {
                  this.max = tfIdf
                }

                boldWeight = 300 + (tfIdf * 10000);

                console.log("TfIdf for word " + stringArray[w] + ": " + tfIdf + " | boldWeight: " + boldWeight);

                if (boldWeight > 999) {
                  boldWeight = 999;
                  console.log("truncating to 999")
                  this.truncateCounter = this.truncateCounter + 1;
                }

                stringArray[w] = ReactDOMServer.renderToStaticMarkup((<span style={{
                    fontWeight: boldWeight,
                    color: "SteelBlue"
                  }}>
                  {stringArray[w]}
                </span>));

                stringArray[w] = stringArray[w].toString();

              } else if (!outOfTag) {
                stringArray[w] = ReactHtmlParser(stringArray[w]);
              }
            }
          }

          textToDisplay = ReactHtmlParser(stringArray.join(''));
          // console.log("ZZZZZZZZZZZ PROCESSED: ZZZZZZZZZZZZ");
          // console.log(textToDisplay);
          // console.log("ZZZZZZZZZZZ ORIGINAL: ZZZZZZZZZZZZ");
          // console.log(this.props.recommendationArray[this.props.recommendationIndex].text)
          //
          // console.log("min tf-idf: " + this.min);
          // console.log("max tf-idf: " + this.max);
          // console.log("truncateCounter: " + this.truncateCounter);
          // console.log("TfIdfRequestCounter: " + this.TfIdfRequestCounter);

        }

      } else {
        console.log("Fragment.js: text skimming is disabled. should reset to normal font.")
        textToDisplay = ReactHtmlParser(this.props.recommendationArray[this.props.recommendationIndex].text)
      }

      fragmentToRender = (<div className="box" style={styles}>

        <div style={{
            ...styles,
            opacity: this.state.opacity
          }}>

          <h1 align="center">
            <b>
              {this.props.recommendationArray[this.props.recommendationIndex].meta.title}
            </b>
          </h1>

          <br/> {textToDisplay}
        </div>
      </div>);;
    } else {
      fragmentToRender = (<div><img src={loadingGif} alt="loading..." className="loadingGifCentered"/>
      </div>);
    }

    return (<div>{fragmentToRender}</div>);
  }
}
const Fragment = connect(mapStateToProps)(ConnectedFragment);
export default Fragment;/*

      <br/>

      <p></p>

      <span style={{
          fontWeight: 0
        }}>
        Isto leva bold com valor a 0
      </span>

      <br/>

      <span style={{
          fontWeight: 1
        }}>
        Isto leva bold com valor a 1
      </span>

      <br/>

      <span style={{
          fontWeight: 5
        }}>
        Isto leva bold com valor a 5
      </span>

      <br/>

      <span style={{
          fontWeight: 100
        }}>
        Isto leva bold com valor a 100
      </span>

      <br/>

      <span style={{
          fontWeight: 150
        }}>
        Isto leva bold com valor a 150
      </span>

      <br/>

      <span style={{
          fontWeight: 200
        }}>
        Isto leva bold com valor a 200
      </span>

      <br/>

      <span style={{
          fontWeight: 250
        }}>
        Isto leva bold com valor a 250
      </span>

      <br/>

      <span style={{
          fontWeight: 300
        }}>
        Isto leva bold com valor a 300
      </span>

      <br/>

      <span style={{
          fontWeight: 350
        }}>
        Isto leva bold com valor a 350
      </span>

      <br/>

      <span style={{
          fontWeight: 400
        }}>
        Isto leva bold com valor a 400
      </span>

      <br/>

      <span style={{
          fontWeight: 450
        }}>
        Isto leva bold com valor a 450
      </span>

      <br/>

      <span style={{
          fontWeight: 500
        }}>
        Isto leva bold com valor a 500
      </span>

      <br/>

      <span style={{
          fontWeight: 550
        }}>
        Isto leva bold com valor a 550
      </span>

      <br/>

      <span style={{
          fontWeight: 600
        }}>
        Isto leva bold com valor a 600
      </span>

      <br/>

      <span style={{
          fontWeight: 650
        }}>
        Isto leva bold com valor a 650
      </span>

      <br/>

      <span style={{
          fontWeight: 700
        }}>
        Isto leva bold com valor a 700
      </span>

      <br/>

      <span style={{
          fontWeight: 750
        }}>
        Isto leva bold com valor a 750
      </span>

      <br/>

      <span style={{
          fontWeight: 800
        }}>
        Isto leva bold com valor a 800
      </span>

      <br/>

      <span style={{
          fontWeight: 850
        }}>
        Isto leva bold com valor a 850
      </span>

      <br/>

      <span style={{
          fontWeight: 900
        }}>
        Isto leva bold com valor a 900
      </span>

      <br/>

      <span style={{
          fontWeight: 950
        }}>
        Isto leva bold com valor a 950
      </span>

      <br/>

      <span style={{
          fontWeight: 999
        }}>
        Isto leva bold com valor a 999
      </span>

      <br/>

      <span style={{
          fontWeight: 1000
        }}>
        Isto leva bold com valor a 1000
      </span>

      <p></p>

      <p>{this.props.recommendationArray[this.props.recommendationIndex].interId}</p>

      <p>{this.props.recommendationArray[this.props.recommendationIndex].meta.date}</p>

      */
