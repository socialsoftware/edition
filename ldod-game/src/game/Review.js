import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import Vote  from './Vote';
import { Steps } from 'antd';
import { Grid, Row, Col, Alert} from 'react-bootstrap';
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
                            topics={['/topic/review']}
                            ref={ (client) => { this.clientRef = client }}
                            onConnect={ () => { this.getFinalTags()}}
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
            this.clientRef.sendMessage('/ldod-game/review', JSON.stringify({ urlId: this.props.id, voterId: localStorage.getItem("currentUser"), msg: "emptyMsg", vote: "emptyVote", limit: this.props.limit}));
            return true;
        } catch(e) {
            return false;
        }
    
    }

    handleMessageReview(message) {
        var res = [];
        for(var i = 0; i < message.length; i++){
            var temp = { tag: message[i].tag, vote: 1}; 
            res.push(temp)
        }
        this.setState({
            votes: res,
            isLoading: false,
        })
        
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
                        <strong>Quick break before the final step ...</strong>
                    </Alert>
                    {this.state.socket}
                    <LoadingIndicator />
                </div>
            );
        }

        return (
            <div>                
                <div style={style}>
                        <ReactCountdownClock seconds={this.props.seconds}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.endFragment}/>
                </div>                
                {this.props.steps}
                <Grid fluid>
                    <Row>
                        <Col md={12}>
                            <h4 className="text-center">Tags submitted:</h4>
                            <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                <Vote 
                                    id={this.props.id} 
                                    seconds={this.props.seconds} 
                                    round={3}
                                    initialTags={this.state.votes}/>
                            </div>
                        </Col>
                        <Col md={12}>
                        <div className="content">
                            <h4 className="text-center">{this.props.title}</h4>
                            <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                <div dangerouslySetInnerHTML={{__html: this.props.fullText}}></div>
                            </div>
                        </div>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
}


export default withRouter(Review);