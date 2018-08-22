import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { FRAGMENT_LIST_SIZE } from '../utils/Constants';
import { Alert, Button, ProgressBar, Glyphicon, Jumbotron } from 'react-bootstrap';
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
        var i = this.state.index;
        if(this.state.view && i < FRAGMENT_LIST_SIZE) {
            console.log(i);
            return (
              <div>
                    <Fragment key={this.state.fragments[i].title} fragment={this.state.fragments[i]} acronym={this.state.acronym} nextFragment={this.nextFragment}/>
                    <div className="progress">
                        <ProgressBar min={0} bsStyle="success"active now={this.state.index} />
                    </div>
                    <div className="next">
                        <Button bsStyle="primary" onClick={this.nextFragment}>
                            <Glyphicon glyph="arrow-right"/> 
                        </Button>
                    </div>
              </div>
            );
        }
        else if(i === FRAGMENT_LIST_SIZE) {
            {this.props.end()}
            return (
                <Jumbotron>
                    <h1>The game has ended!</h1>
                    <p>
                        Thank you for playing, hope you enjoyed it.
                    </p>
                </Jumbotron>
            );
        }
        return (
            <div>
                <Alert bsStyle="info">
                    <strong>The game is about to start in: </strong>
                </Alert>
                <div className="clock">    
                    <ReactCountdownClock seconds={1}
                        color="#3498db"
                        size={200}
                        onComplete={this.nextFragment}
                    />
                </div>
            </div>
        );
    }
    
}

export default withRouter(VirtualEdition);