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
    this.TfIdfData = [];
  }

  render() {

    ("Fragment.js: render()")

    if (this.props.toggleTextSkimming) {
      console.log("Fragment.js: => app.js wants toggleTextSkimming active!");

      let currentlyDisplayedFragmentId = this.props.recommendationArray[this.props.recommendationIndex].interId;

      if (!this.oldTfIdfDataMap.has(currentlyDisplayedFragmentId)) {
        console.log("Fragment.js: componentDidUpdate() => New TFIDF info needed, requesting TF-IDF for fragId " + currentlyDisplayedFragmentId);

        const service = new RepositoryService();

        service.getFragmentTfIdf(currentlyDisplayedFragmentId).then(response => {

          console.log("TF-IDF received for fragId " + currentlyDisplayedFragmentId);
          console.log(response.data);

          this.oldTfIdfDataMap.set(this.props.recommendationArray[this.props.recommendationIndex].interId, response.data);

          this.TfIdfData = response.data;

          this.setState({
            toggleTextSkimmingUpdate: !this.state.toggleTextSkimmingUpdate
          });

        });

      } else {
        console.log("Fragment.js: => TF-IDF info already loaded. Using TF-IDF info in hashmap.");
        this.TfIdfData = this.oldTfIdfDataMap.get(currentlyDisplayedFragmentId);

        //cenas para display do negrito

      }

    } else {
      console.log("Fragment.js: text skimming is disabled. should reset to normal font.")
    }

    let fragmentToRender;

    if (this.props.allFragmentsLoaded && this.props.outOfLandingPage && this.props.recommendationLoaded) {
      fragmentToRender = (<div className="box">

        <h1 align="center">
          <b>
            {this.props.recommendationArray[this.props.recommendationIndex].meta.title}
          </b>
        </h1>

        <br/>

        <p>{this.props.recommendationArray[this.props.recommendationIndex].text}</p>

        <p>{this.props.recommendationArray[this.props.recommendationIndex].interId}</p>

        <p>{this.props.recommendationArray[this.props.recommendationIndex].meta.dates}</p>

      </div>);;
      //console.log(this.props.recommendationArray[this.props.recommendationIndex].meta.dates);

    } else {
      fragmentToRender = <div/>;
    }

    return (<div>{fragmentToRender}</div>);
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
