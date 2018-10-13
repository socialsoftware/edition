import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL, SUBSCRIBE_URL, APP_PREFIX, FINAL_TIME} from '../utils/Constants';
import Vote  from './Vote';
import { Steps, Divider } from 'antd';
import { Grid, Alert} from 'react-bootstrap';
import LoadingIndicator  from '../common/LoadingIndicator';
import SockJsClient from 'react-stomp'
var ReactCountdownClock = require("react-countdown-clock")
class Review extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            socket: null,
            votes: [],
            seconds: 0.0,
            hasEnded: false,
        };
        this.handleMessageReview = this.handleMessageReview.bind(this);
        this.getFinalTags = this.getFinalTags.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            votes: this.props.initialTags,
            seconds: this.props.seconds,
            isLoading: true,
            socket: <SockJsClient
                            url={WEB_SOCKETS_URL}
                            topics={[ SUBSCRIBE_URL + this.props.gameId +'/review', SUBSCRIBE_URL + this.props.gameId +'/sync']}
                            ref={ (client) => { this.clientRef = client }}
                            onConnect={ () => { this.getFinalTags() }}
                            onMessage={(message) => this.handleMessageReview(message)} />
        })
        this.interval = setInterval(() => this.tick(), 1000);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    componentDidUpdate(prevProps) {
        if (this.props.seconds !== prevProps.seconds) {
            this.setState({
                seconds: this.props.seconds,
            })
        }
    }
  
    tick() {
        this.setState(prevState => ({
          seconds: prevState.seconds - 1
        }));
    }

    getFinalTags(){
        try{
            this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/review', JSON.stringify({ gameId: this.props.gameId, voterId: this.props.userId, msg: "emptyMsg", vote: "emptyVote", limit: this.props.limit}));
            this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/sync', JSON.stringify({ gameId: this.props.gameId, voterId: this.props.userId, msg: "emptyMsg", vote: "emptyVote", limit: this.props.limit}));
            return true;
        } catch(e) {
            return false;
        }
    
    }

    handleMessageReview(message) {
        if(message[0] === "continue") {
            this.setState({
                isLoading: false,
            })
            return;
        }
        var res = [];
        var tagAndWinner = message.pop();
        var winner = Object.keys(tagAndWinner);
        var topTag = Object.values(tagAndWinner)
        for(var i = 0; i < message.length; i++){
            var temp = { tag: message[i].tag, vote: Math.round(parseFloat(message[i].vote))}; 
            res.push(temp)
        }
        this.setState({
            votes: res,
            topTag: topTag[0],
            winner: winner[0],
        })
        
    }
    
    finishGame(){
        this.setState({
            hasEnded: true,
        })
        this.props.endFragment();
    }

    render() {
        let style = {   marginTop: "-45px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        paddingBottom: "25px",};
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert bsStyle="info">
                        <strong>Quick break before the final step...</strong>
                    </Alert>
                    {this.state.socket}
                    <LoadingIndicator />
                </div>
            );
        }

        return (
            <div>                
                <div style={style}>
                        <ReactCountdownClock seconds={FINAL_TIME}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.finishGame.bind(this)}/>
                </div>                
                {this.props.steps}
                <Grid fluid>
                    <h4 className="text-center">Tags submitted:</h4>
                    <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                        <Vote 
                            gameId={this.props.gameId} 
                            userId={this.props.userId} 
                           // seconds={this.props.totalTime} 
                            seconds={30} 
                            round={3}
                            limit={this.props.limit}
                            topTag={this.state.topTag}
                            hasEnded={this.state.hasEnded}
                            initialTags={this.state.votes}/>
                        <Divider dashed />
                            <h4 className="text-center">{this.props.title}</h4>
                            <div dangerouslySetInnerHTML={{__html: this.props.fullText}}></div>
                            
                    </div>
                </Grid>
            </div>
        );
    }
}


export default withRouter(Review);