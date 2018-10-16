import React from 'react';
import './Fragment.css';
import { connect } from "react-redux";

const mapStateToProps = state => {
  return {
    fragmentIndex: state.fragmentIndex,
    fragments: state.fragments
  };
};

export class ConnectedFragment extends React.Component {

  render() {
    return (
      <div className="box">
        <p align="center"> <b> {this.props.fragmentIndex+1}/{this.props.fragments.length} </b> </p>
        <h1>{this.props.fragments[this.props.fragmentIndex].meta.title}</h1>
        <p>{this.props.fragments[this.props.fragmentIndex].text}</p>
      </div>
    );
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
