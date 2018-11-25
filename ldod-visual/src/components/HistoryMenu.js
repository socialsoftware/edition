import {connect} from "react-redux";
import {Button, ButtonToolbar, Modal} from "react-bootstrap";
import {VIS_NETWORK, BY_HISTORIC, BY_NEXTBUTTON, BY_PREVIOUSBUTTON, BY_NETWORK_TEXTSIMILARITY} from "../constants/history-transitions";
import {Timeline, DataSet} from "vis";
import React, {Component, createRef} from "react";
import {setFragmentIndex, setCurrentVisualization, addHistoryEntry, setOutOfLandingPage} from "../actions/index";
import "./HistoryMenu.css";

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization, history: state.history, allFragmentsLoaded: state.allFragmentsLoaded};
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage))
  };
};

class ConnectedHistoryMenu extends Component {
  constructor(props) {
    super(props);

    this.options = {};
    this.timeline = {};

  }

  componentDidMount() {
    if (this.props.allFragmentsLoaded) {
      var items = [
        {
          id: 1,
          content: 'item 1',
          start: '2014-04-20'
        }, {
          id: 2,
          content: 'item 2',
          start: '2014-04-14'
        }, {
          id: 3,
          content: 'item 3',
          start: '2014-04-18'
        }, {
          id: 4,
          content: 'item 4',
          start: '2014-04-16',
          end: '2014-04-19'
        }, {
          id: 5,
          content: 'item 5',
          start: '2014-04-25'
        }, {
          id: 6,
          content: 'item 6',
          start: '2014-04-27',
          type: 'point'
        }
      ];

      var container = document.getElementById('visualization');
      this.timeline = new Timeline(container, items, this.options);
    }

  }

  render() {

    return (<div className="historyMenu">>
      <p>
        Timeline.
      </p>
      <div id="visualization"></div>
    </div>);
  }
}

const HistoryMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedHistoryMenu);

export default HistoryMenu;
