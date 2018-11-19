import React from 'react';
import {connect} from "react-redux";
import './Fragment.css';

const mapStateToProps = state => {
  return {fragmentIndex: state.fragmentIndex, fragments: state.fragments};
};

export class ConnectedFragment extends React.Component {

  render() {

    //console.log(this.props.fragments[this.props.fragmentIndex]);
    //alert(this.props.fragments[0].interId)
    //<p>{this.props.fragments[this.props.fragmentIndex].interId}</p>

    return (<div className="box">

      <p align="center">
        <b>
          {this.props.fragmentIndex + 1}/{this.props.fragments.length}
        </b>
      </p>

      <h1 align="center">
        <b>
          {this.props.fragments[this.props.fragmentIndex].meta.title}
        </b>
      </h1>

      <br/>

      <p>{this.props.fragments[this.props.fragmentIndex].text}</p>

    </div>);
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
