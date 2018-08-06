import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Button, ProgressBar, Glyphicon } from 'react-bootstrap';
import Fragment  from './Fragment';
import './VirtualEdition.css';
var ReactCountdownClock = require("react-countdown-clock")
class VirtualEdition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            fragments: [],
            title: " ",
            acronym: " ",
            pub: false,
            taxonomy: [],
            members: [],
            index: 0, //check this 
            view: false,
        };
        this.nextFragment = this.nextFragment.bind(this);
    }
    
    componentDidMount() {
        this.setState({
            fragments: this.props.virtualEdition.virtualEditionInterList,
            title: this.props.virtualEdition.title,
            acronym: this.props.virtualEdition.acronym,
            pub: this.props.virtualEdition.pub,
        })
    }

    nextFragment(){
        this.setState((prevState, props) => ({
            index: prevState.index + 1,
            view: true,
        })); 
    }

    render() {
        if(this.state.view) {
            var i = this.state.index;
            return (
              <div>
                    <Fragment ws={this.child} key={this.state.fragments[i].title} fragment={this.state.fragments[i]} acronym={this.state.acronym}/>
                    <ProgressBar min={0} bsStyle="success"active now={this.state.index} />
                    <div className="next">
                        <Button bsStyle="primary" onClick={this.nextFragment}>
                            <Glyphicon glyph="arrow-right"/> 
                        </Button>
                    </div>
              </div>
            );
        }
        return (
            <div>
            <span className="start">The game will start in:</span>
                <div className="clock">    
                    <ReactCountdownClock seconds={10}
                        color="#3498db"
                        size={200}
                        showMilliseconds={false}
                        onComplete={this.nextFragment}
                    />
                </div>
            </div>
        );
    }
    
}

export default withRouter(VirtualEdition);