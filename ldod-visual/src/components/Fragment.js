import React from 'react';
import {connect} from "react-redux";
import './Fragment.css';
import {RepositoryService} from "../services/RepositoryService";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {
    fragmentIndex: state.fragmentIndex,
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    recommendationLoaded: state.recommendationLoaded,
    displayTextSkimming: state.displayTextSkimming
  };
};

export class ConnectedFragment extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      toggleTextSkimmingUpdate: false
    };

    this.oldTfIdfDataMap = new HashMap();
    this.currentFragmentTfIdfMap = new HashMap();
  }

  render() {

    ("Fragment.js: render()")

    let fragmentToRender;
    let textToDisplay;
    let stringArray;

    if (this.props.allFragmentsLoaded && this.props.outOfLandingPage && this.props.recommendationLoaded) {

      if (this.props.toggleTextSkimming) {
        console.log("Fragment.js: => app.js wants toggleTextSkimming active!");

        let currentlyDisplayedFragmentId = this.props.recommendationArray[this.props.recommendationIndex].interId;

        if (!this.oldTfIdfDataMap.has(currentlyDisplayedFragmentId)) {
          console.log("Fragment.js: !this.oldTfIdfDataMap.has(currentlyDisplayedFragmentId) => New TFIDF info needed, requesting TF-IDF for fragId " + currentlyDisplayedFragmentId);

          const service = new RepositoryService();

          service.getFragmentTfIdf(currentlyDisplayedFragmentId).then(response => {

            console.log("TF-IDF received for fragId " + currentlyDisplayedFragmentId);
            console.log(response.data);

            console.log("<<<<<>>>>>");

            let myMap = new HashMap();
            response.data.map(d => console.log(Object.keys(d)[0] + " " + Object.values(d)[0]));
            response.data.map(d => myMap.set(Object.keys(d)[0], Object.values(d)[0]));

            this.oldTfIdfDataMap.set(currentlyDisplayedFragmentId, myMap);

            console.log(this.oldTfIdfDataMap.get(currentlyDisplayedFragmentId));

            this.setState({
              toggleTextSkimmingUpdate: !this.state.toggleTextSkimmingUpdate
            });

          });

        } else {
          console.log("Fragment.js: => TF-IDF info already loaded. Using TF-IDF info in hashmap.");
          let dataMap;
          let wordsTfIdfMap = this.oldTfIdfDataMap.get(currentlyDisplayedFragmentId);

          var str = this.props.recommendationArray[this.props.recommendationIndex].text;
          stringArray = str.split(/(\s+)/);
          //stringArray = str.split(" ");

          let boldWeight = 1;
          let w;
          for (w = 0; w < stringArray.length; w++) {

            if (wordsTfIdfMap.has(stringArray[w])) {

              let tfIdf = parseFloat(wordsTfIdfMap.get(stringArray[w]));

              boldWeight = 400 + (tfIdf * 10000);

              console.log("TfIdf for word " + stringArray[w] + ": " + tfIdf + " | boldWeight: " + boldWeight);

              if (boldWeight > 999) {
                boldWeight = 999;
              }

              stringArray[w] = (<span style={{
                  fontWeight: boldWeight,
                  color: "SteelBlue"
                }}>
                {stringArray[w]}
              </span>)

            }
          }

          textToDisplay = stringArray;

        }

      } else {
        console.log("Fragment.js: text skimming is disabled. should reset to normal font.")
        textToDisplay = this.props.recommendationArray[this.props.recommendationIndex].text
      }

      fragmentToRender = (<div className="box">

        <h1 align="center">
          <b>
            {this.props.recommendationArray[this.props.recommendationIndex].meta.title}
          </b>
        </h1>

        <br/> {textToDisplay}

      </div>);;

    } else {
      fragmentToRender = <div/>;
    }

    return (<div>{fragmentToRender}</div>);
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;

/*

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
