import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { WEB_SOCKETS_URL} from '../utils/Constants';
var stompClient = null;
var subscription = null;

class WebSockets extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: " ",
            tags: []
        };  
        this.onMessageReceived = this.onMessageReceived.bind(this);   
        this.onConnected = this.onConnected.bind(this);
        this.sendMessage = this.sendMessage.bind(this);   
        this.getTags = this.getTags.bind(this);   
    }
    
    componentDidMount(){
        this.props.onRef(this)
        this.setState({
            userId: localStorage.getItem("currentUser"),
        });
        var socket = new SockJS(WEB_SOCKETS_URL);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, this.onConnected);

    }
    
    onConnected(){
        subscription = stompClient.subscribe('/topic/tags', this.onMessageReceived,{ id: this.state.userId});
    
    }
    
    onMessageReceived(payload) {
        this.setState(prevState => ({
            tags: [...prevState.tags, payload.body]
        }));
    }
    
    componentWillUnmount(){
        subscription.unsubscribe();
        stompClient.disconnect();
        this.props.onRef(null)
    }

    sendMessage(msg){
        stompClient.send('/ldod-game/tags', {}, JSON.stringify({ userId: this.state.userId, msg: msg}));
    }
    
    getTags(){
        return this.state.tags;
    }

    render() {
        return (
            <div>  
            </div>
        );
    }
}

export default withRouter(WebSockets);