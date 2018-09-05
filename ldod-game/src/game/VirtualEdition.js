import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Alert, Jumbotron } from 'react-bootstrap';
import Fragment  from './Fragment';
import { endGame } from '../utils/APIUtils';
import './VirtualEdition.css';

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
            isEnding: false,
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
                    isEnding: false,
                }));
                return;
            
            case "end":
                var i = this.state.index+1;
                localStorage.setItem("currentFragment", i);
                this.endGame();
                this.setState({
                    isActive: false,
                    isEnding: true,
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
              <div>
                    <Fragment 
                        fragment={this.state.fragments[i]} 
                        endFragment={() => this.invokeCommand("end")}/>
              </div>
            );
        }
        else if(this.state.isEnding){
            return (
                <Jumbotron>
                    <h1>The game has ended!</h1>
                    <h1>The winner tag is: </h1> 
                    <p>Thank you for playing, hope you enjoyed it.</p>
                </Jumbotron>
            );
        }
        return (
            <div>
                <Alert bsStyle="info">
                    <strong>The game is about to start in: </strong>
                </Alert>
                <div className="start-clock">    
                    <ReactCountdownClock seconds={10}
                        color="#3498db"
                        size={200}
                        showMilliseconds={false}
                        onComplete={() => this.invokeCommand("start")}/>
                </div>
            </div>
        );
    }
    
}

export default withRouter(VirtualEdition);