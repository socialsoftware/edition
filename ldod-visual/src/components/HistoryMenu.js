import {Network, Timeline, DataSet} from "vis";
import React, {Component} from "react";
import {connect} from "react-redux";
import {VIS_NETWORK, BY_HISTORIC, BY_NETWORK_TEXTSIMILARITY, CRIT_TEXTSIMILARITY} from "../constants/history-transitions";
import {setFragmentIndex, setCurrentVisualization, addHistoryEntry, setOutOfLandingPage, setHistoryEntryCounter} from "../actions/index";
import "./HistoryMenu.css";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    history: state.history,
    allFragmentsLoaded: state.allFragmentsLoaded,
    historyEntryCounter: state.historyEntryCounter
  };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex)),
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    addHistoryEntry: historyEntry => dispatch(addHistoryEntry(historyEntry)),
    setOutOfLandingPage: outOfLandingPage => dispatch(setOutOfLandingPage(outOfLandingPage)),
    setHistoryEntryCounter: historyEntryCounter => dispatch(setHistoryEntryCounter(historyEntryCounter))
  };
};

class ConnectedHistoryMenu extends Component {
  constructor(props) {
    super(props);

    this.properties = [];

    this.options = {};
    this.timeline = [];

    this.handleClick = this.handleClick.bind(this);
    this.printMessage = this.printMessage.bind(this);

  }

  printMessage() {
    console.log("hello")
  }

  handleClick(event) {

    //const properties = this.timeline.getEventProperties(event);

    console.log(event)

    const properties = event;

    if (properties.item !== null && this.props.allFragmentsLoaded === true) {

      console.log("properties id: " + parseInt(properties.item));
      console.log("history array: " + this.props.history);
      console.log(this.props.history[properties.id]);

      var i;
      for (i = 0; i < this.props.fragments.length; i++) {
        if (this.props.fragments[i].interId === this.props.history[parseInt(properties.item)].nextFragment.interId) {
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          let obj;
          obj = {
            id: this.props.historyEntryCounter,
            originalFragment: this.props.fragments[this.props.fragmentIndex],
            nextFragment: this.props.history[parseInt(properties.item)].nextFragment,
            vis: 0,
            criteria: 0,
            visualization: this.props.history[parseInt(properties.item)].visualization,
            recommendationArray: 0, //mudar para quando o cirterio for difernete
            start: new Date().getTime()
          };
          this.props.addHistoryEntry(obj);
          this.props.setHistoryEntryCounter(this.props.historyEntryCounter + 1)
          //HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY HISTORY ENTRY
          this.props.onChange();
          this.props.setOutOfLandingPage(true);
          this.props.setFragmentIndex(i);
          const globalViewToRender = this.props.history[parseInt(properties.item)].visualization //(<SquareGrid onChange={this.props.onChange}/>);
          this.props.setCurrentVisualization(globalViewToRender);
          //  }

        }

      }
    }
  }

  componentDidMount() {
    var historyItems = [];

    var i;
    for (i = 0; i < this.props.historyEntryCounter; i++) {
      let item = {
        id: this.props.history[i].id,
        content: this.props.history[i].nextFragment.meta.title,
        start: this.props.history[i].start
      };
      historyItems.push(item);
    }

    var container = document.getElementById('visualization');
    console.log("history counter: " + this.props.historyEntryCounter);
    console.log("historico: " + this.props.history);
    console.log("history items: " + historyItems);
    this.timeline = new Timeline(container, historyItems, this.options);
    this.timeline.on('click', this.handleClick);
    let x = this.printMessage;
    x();

    //this.printMessage();
  }

  render() {

    let jsxToRender;

    if (this.props.allFragmentsLoaded) {
      jsxToRender = <div id="visualization"></div>
      console.log("na verdade, all fragments loaded.")
    }

    return (<div className="historyMenu">
      <p>
        Timeline.
      </p>
      {jsxToRender}
    </div>);
  }
}

const HistoryMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedHistoryMenu);

export default HistoryMenu;
