import React, { Component } from 'react';
import { setFragmentIndex } from "../actions/index";
import { connect } from "react-redux";
import { Button, ButtonToolbar } from 'react-bootstrap'

const mapStateToProps = state => {
    return {
        fragments: state.fragments,
        fragmentIndex: state.fragmentIndex
    };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex))
  };
};

class ConnectedReaderButtonToolbar extends React.Component {

  constructor(props) {

    super(props);

    this.state = {
      previousFragmentButtonStyle: "",
      nextFragmentButtonStyle: "primary"
    }

    this.handleClickPrevious = this.handleClickPrevious.bind(this);
    this.handleClickNext = this.handleClickNext.bind(this);

    }

  handleClickPrevious() {

    if (this.props.fragmentIndex === 1){
      this.setState({previousFragmentButtonStyle: ""})
    }

    if (this.props.fragmentIndex>0){
      this.setState({nextFragmentButtonStyle: "primary"})
      this.props.setFragmentIndex(this.props.fragmentIndex-1)
    }

  }

  handleClickNext() {

    if (this.props.fragmentIndex === this.props.fragments.length-2){
      this.setState({nextFragmentButtonStyle: ""})
    }

    if (this.props.fragmentIndex<this.props.fragments.length-1){
      this.props.setFragmentIndex(this.props.fragmentIndex+1)
      this.setState({previousFragmentButtonStyle: "primary"})
    }

  }

  render() {

      return (
        <ButtonToolbar>
          <Button bsStyle={this.state.previousFragmentButtonStyle} bsSize="large" onClick={this.handleClickPrevious}>
            Anterior
          </Button>
          <Button bsStyle="primary" bsSize="large">
            Global View
          </Button>
          <Button bsStyle="primary" bsSize="large">
            Configuração
          </Button>
          <Button bsStyle={this.state.nextFragmentButtonStyle} bsSize="large" onClick={this.handleClickNext}>
            Próximo
          </Button>
        </ButtonToolbar>
    );
  }
}

const ReaderButtonToolbar = connect(mapStateToProps,mapDispatchToProps)(ConnectedReaderButtonToolbar);

export default ReaderButtonToolbar;
