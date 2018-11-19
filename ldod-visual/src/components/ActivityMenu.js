import React, {Component} from "react";
import "./ActivityMenu.css";
import {connect} from "react-redux";
import {DropdownButton, MenuItem, ButtonToolbar} from "react-bootstrap";
import NetworkGraphContainer from "../containers/NetworkGraphContainer";
import {setCurrentVisualization} from "../actions/index";

const mapDispatchToProps = dispatch => {
  return {
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization))
  };
};

const mapStateToProps = state => {
  return {fragments: state.fragments, fragmentIndex: state.fragmentIndex, currentVisualization: state.currentVisualization};
};

class ConnectedActivityMenu extends Component {
  constructor(props) {
    super(props);

    this.state = {
      show: true
    };

    this.toggleMenuVisualization = this.toggleMenuVisualization.bind(this);
  }

  toggleMenuVisualization() {
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  render() {
    let visualizationToRender;

    if (this.state.show) {
      visualizationToRender = (<ButtonToolbar>
        <DropdownButton id="1" bsSize="large" bsStyle="primary" title="Quero ler fragmentos semelhantes a este por...">
          <MenuItem eventKey="1" onClick={this.toggleMenuVisualization}>
            Semelhança de texto
          </MenuItem>
        </DropdownButton>
      </ButtonToolbar>);
    } else {
      visualizationToRender = (<NetworkGraphContainer pFragmentId={this.props.fragments[this.props.fragmentIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
    }

    return <div className="activityMenu">{visualizationToRender}</div>;
  }
}

const ActivityMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedActivityMenu);

export default ActivityMenu;
