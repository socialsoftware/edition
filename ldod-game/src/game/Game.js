import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import VirtualEdition from './VirtualEdition';
import { getVirtualEditionIndex } from '../utils/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';

class Game extends Component {
    constructor(props, context) {
        super(props,  context);
        this.state = {
            virtualEdition: [],
        };

        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
    }

    componentDidMount(){
       this.loadVirtualEdition();
    }
    
    async loadVirtualEdition(){
        this.setState({
            isLoading: true,
        })

        let request =   await getVirtualEditionIndex("LdoD-ok");

        this.setState({
            virtualEdition: request,
            isLoading: false
        })
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }
       
        return ( 
            <div>
                <VirtualEdition virtualEdition={this.state.virtualEdition}/>
            </div>
    );
  }
}

export default withRouter(Game);