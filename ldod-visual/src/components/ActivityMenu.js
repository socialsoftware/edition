import React, { Component } from 'react';
import './ActivityMenu.css';
import { connect } from "react-redux";
import { DropdownButton, MenuItem, ButtonToolbar } from 'react-bootstrap'
import NetworkGraph from '../components/NetworkGraph';
import NetworkGraphOriginal from '../components/NetworkGraphOriginal';

class ActivityMenu extends Component {

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
        <DropdownButton bsSize="large" bsStyle ="primary" title="Quero ler fragmentos semelhantes a este por...">
          <MenuItem eventKey="1" onClick={this.toggleMenuVisualization}>Semelhança de texto</MenuItem>
        </DropdownButton>
    </ButtonToolbar>
  );

  } else {
    componentToRender = <NetworkGraphOriginal/>
  }

    return (
      <div className="activityMenu">
        {componentToRender}
      </div>

    );
  }
}

export default ActivityMenu;
