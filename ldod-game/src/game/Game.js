import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';

import { Well } from 'react-bootstrap';
import VirtualEdition from './VirtualEdition';

class Game extends Component {
    constructor(props, context) {
        super(props,  context);
        this.state = {
            current: "",
        };
    }



  render() {
    return ( 
        <div>
            <Well>
                <VirtualEdition/>
            </Well>    
      </div>
    );
  }
}

export default withRouter(Game);