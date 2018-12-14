import React from 'react';
import {connect} from "react-redux";
import './Fragment.css';

const mapStateToProps = state => {
  return {
    fragmentIndex: state.fragmentIndex,
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    recommendationLoaded: state.recommendationLoaded
  };
};

export class ConnectedFragment extends React.Component {

  render() {

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

      </div>);;
    } else {
      fragmentToRender = <div/>;
    }

    return (<div>{fragmentToRender}</div>);
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
