import React, {Component} from "react";
import {connect} from "react-redux";
import {addHistoryEntry} from "../actions/index";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import {VIS_NETWORK, BY_HISTORIC, BY_NEXTBUTTON, BY_PREVIOUSBUTTON, BY_NETWORK_TEXTSIMILARITY} from "../constants/history-transitions";

const mapDispatchToProps = dispatch => {
  return {
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry))
  };
};

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history};
};

class ConnectedHistoryMenu extends Component {
  constructor(props) {
    super(props);
  }

  render() {

    const listItems = this.props.history.map((obj) => <li>Navegou do fragmento{' '}
      {"\""}<b>{obj.originalFragment.meta.title}</b>{"\""}
      {' '}para o fragmento{' '}
      {"\""}<b>{obj.nextFragment.meta.title}</b>{"\""}
      {' '}através{' '}
      {obj.via}
    </li>);

    return (<div>
      <ul>{listItems}</ul>
    </div>);
  }
}

const HistoryMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedHistoryMenu);

export default HistoryMenu;
