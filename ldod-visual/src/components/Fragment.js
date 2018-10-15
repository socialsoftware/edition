import React from 'react';
import './Fragment.css';
import { connect } from "react-redux";

const mapStateToProps = state => {
  return { fragmentIndex: state.fragmentIndex };
};

export class ConnectedFragment extends React.Component {

  render() {
    const titleArray = this.props.titleArray;
    const textArray = this.props.textArray;
    return (
      <div className="box">
        <h1>{titleArray[this.props.fragmentIndex]}</h1>
        <p>{textArray[this.props.fragmentIndex]}</p>
      </div>
    );
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
