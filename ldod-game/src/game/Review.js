import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import Vote  from './Vote';
import { Steps } from 'antd';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Alert} from 'antd';
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
            this.clientRef.sendMessage('/ldod-game/review', JSON.stringify({ urlId: this.props.id, limit: this.props.limit, virtualEdition: "LdoD-ok"}));
            return true;
        } catch(e) {
            return false;
        }
    
    }

    handleMessageReview(message) {
        var topTags = message[3];
        this.setState({
            votes: topTags,
            isLoading: false,
        })
    }

    render() {
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert
                        style={{ fontSize: '20px', fontFamily: 'Ubuntu' }}
                        message="Quick break before the final step ..."
                        type="info"
                        banner />
                        {this.state.socket}
                    <LoadingIndicator />
                </div>
            );
        }

        return (
            <div>
                <div className="clock">
                        <ReactCountdownClock seconds={this.props.seconds}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.endFragment}/>
                </div>                
                {this.props.steps}
                <section className="intro">
                    <div className="col-lg-9 col-sm-12 left">
                        <div className="content">
                            <h4 className="text-center">{this.props.title}</h4>
                            <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                <div dangerouslySetInnerHTML={{__html: this.props.fullText}}></div>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-3 col-sm-12 right">
                        <div>
                            <h4 className="text-center">Tags submitted:</h4>
                            <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                <Vote id={this.props.id} seconds={this.props.seconds} initialTags={this.props.initialTags} />
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        );
    }
}


export default withRouter(Review);