import React, { Component } from 'react';
import './ActivityMenu.css';
import { connect } from "react-redux";
import { DropdownButton, MenuItem, ButtonToolbar } from 'react-bootstrap'
import NetworkGraphContainer from '../containers/NetworkGraphContainer';

const mapStateToProps = state => {
    return {
        fragments: state.fragments,
        fragmentIndex: state.fragmentIndex
    };
};

class ConnectedActivityMenu extends Component {

  constructor(props) {

    super(props);

    this.state = {
      show: true
  }

  this.toggleMenuVisualization = this.toggleMenuVisualization.bind(this);

}

  toggleMenuVisualization() {

    this.setState(prevState => ({
      show: !prevState.show
    }));

  }

render() {

let componentToRender;

if (this.state.show){

    componentToRender = (
    <ButtonToolbar>
        <DropdownButton id="1" bsSize="large" bsStyle ="primary" title="Quero ler fragmentos semelhantes a este por...">
          <MenuItem eventKey="1" onClick={this.toggleMenuVisualization}>Semelhança de texto</MenuItem>
        </DropdownButton>
    </ButtonToolbar>
  );

  } else {
    componentToRender = (
                          <NetworkGraphContainer
                            pFragmentId={this.props.fragments[this.props.fragmentIndex].interId}
                            pHeteronymWeight="0.0"
                            pTextWeight="1.0"
                            pDateWeight="0.0"
                            ptaxonomyWeight="0.0"
                            onChange={this.props.onChange}
                            />
                          )
  }

    return (
      <div className="activityMenu">
        {componentToRender}
      </div>

    );
  }
}

const ActivityMenu = connect(mapStateToProps)(ConnectedActivityMenu);

export default ActivityMenu;
