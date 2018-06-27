import React, { Component } from 'react';
import {
    withRouter
} from 'react-router-dom';

import { Well, ProgressBar } from 'react-bootstrap';
import Fragment from './Fragment';
import VirtualEdition from './VirtualEdition';

class Game extends Component {
    constructor(props, context) {
        super(props,  context);
        this.state = {
            current: "",
            progress: 0,
        };
    }



  render() {
    /*const fragmentViews = [];
    this.state.fragments.forEach((f, fIndex) => {
        fragmentViews.push(<Fragment
            key={f.meta.title}
            fragment={f}/>)
    });*/
    return ( 
        <div>
            <Well>
                <ProgressBar min={0} bsStyle="success"active now={50} />
                <VirtualEdition/>

            </Well>    
      </div>
    );
  }
}

export default Game;