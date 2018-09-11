import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { endGame } from '../utils/APIUtils';
import Fragment  from './Fragment';
import { Alert, Jumbotron, Col, Grid, Row } from 'react-bootstrap';

var ReactCountdownClock = require("react-countdown-clock")
class VirtualEdition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: " ",
            acronym: " ",
            pub: false,
            taxonomy: [],
            members: [],
            fragments: [],
            index: parseInt(localStorage.getItem("currentFragment"), 10), //check this 
            isActive: false,
            isStarting: true,
        };
        this.invokeCommand = this.invokeCommand.bind(this);
        this.endGame = this.endGame.bind(this);
    }
    
    componentDidMount() {
        this.setState({
            fragments: this.props.virtualEdition.virtualEditionInterList,
            title: this.props.virtualEdition.title,
            acronym: this.props.virtualEdition.acronym,
            pub: this.props.virtualEdition.pub,
        })
    }

    async endGame(){
        let request = await endGame(this.props.gameId);
    }
    
    invokeCommand(command) {
        switch(command) {
            case "start":
                this.setState(({
                    isActive: true,
                    isStarting: false,
                }));
                return;
            
            case "end":
                var i = this.state.index+1;
                localStorage.setItem("currentFragment", i);
                this.endGame();
                this.setState({
                    isActive: true,
                    isStarting: false,
                });
                return;
            
            default:
                console.log("error");
                return;
        }
    }

    render() {
        var i = this.state.index;
        if(this.state.isActive) {
            return (
              <Grid fluid>
                  <Fragment 
                        fragment={this.state.fragments[i]} 
                        endFragment={() => this.invokeCommand("end")}/>
              </Grid>
            );
        }
        if(this.state.isStarting){
            return (
            <Grid fluid>
                <Alert bsStyle="info">
                    <strong>The game is about to start in: </strong>
                </Alert>
                <div style={{ display: "flex", alignItems: "center", justifyContent: "center", }} >
                    <ReactCountdownClock seconds={10}
                        color="#3498db"
                        size={200}
                        showMilliseconds={false}
                        onComplete={() => this.invokeCommand("start")}/>
                </div>
            </Grid>
            );
        }
    }
    
}

export default withRouter(VirtualEdition);