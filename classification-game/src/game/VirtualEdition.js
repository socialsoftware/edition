import React, { Component } from 'react';
import { withRouter, Link } from 'react-router-dom';
import { endGame, getVirtualEditionFragment} from '../utils/APIUtils';
import Fragment  from './Fragment';
import { Alert, Grid, Jumbotron, Glyphicon, Button} from 'react-bootstrap';
var ReactCountdownClock = require("react-countdown-clock")
class VirtualEdition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isActive: false,
            isStarting: true,
            fragment: null,
            game: null,
        };
        this.invokeCommand = this.invokeCommand.bind(this);
        this.endGame = this.endGame.bind(this);
    }
    
    async componentDidMount() {
        let request = await getVirtualEditionFragment(this.props.game.virtualEditionAcronym, this.props.game.virtualEditionInterDto.urlId);
        this.setState({
            fragment: request,
            game: this.props.game,
        })
    }

    async endGame(){
        let response = await endGame(this.props.gameId);
        this.setState({
            ended: true,
            winner: response[0],
        })
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
        if(this.state.ended) {
            return (
                <Grid fluid>
                <Jumbotron  style={{ backgroundColor: 'white' }} >
                    <h1 className="text-center">The winner is: {this.state.winner}</h1>
                </Jumbotron>
                <div className="text-center"> 
                <Button bsStyle="primary" componentClass={Link} to="/leaderboard">
                    Check leaderboard for all scores <Glyphicon glyph="stats"/> 
                </Button>   
                </div>	
                    
              </Grid>
            );
        }
        if(this.state.isActive) {
            return (
              <Grid fluid>
                        <Fragment
                        fragment={this.state.fragment} 
                        userId={this.props.userId}
                        gameId={this.props.gameId} 
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