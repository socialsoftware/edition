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
            messages: []
        };  
        this.onMessageReceived = this.onMessageReceived.bind(this);   
        this.onConnected = this.onConnected.bind(this);
        this.sendMessage = this.sendMessage.bind(this);   
        this.getMessages = this.getMessages.bind(this);   
    }
    
    componentDidMount(){
        this.props.onRef(this)
        this.setState({
            userId: localStorage.getItem("currentUser"),
        });
        var socket = new SockJS(WEB_SOCKETS_URL);
        stompClient = Stomp.over(socket);
        stompClient.debug = null;
        stompClient.connect({}, this.onConnected);

    }
    
    componentWillUnmount(){
        subscription.unsubscribe();
        stompClient.disconnect();
        this.props.onRef(null)
    }


    onConnected(){
        subscription = stompClient.subscribe('/topic/tags', this.onMessageReceived, { id: this.state.userId });
        stompClient.subscribe('/ldod-game/ping', this.onMessageReceived , { id: this.state.userId });
    
    }
    
    onMessageReceived(payload) {
        var response = JSON.parse(payload.body);
        var temp = { authorId: response[0], tag: response[1]};
        this.setState({
            messages: [...this.state.messages, temp]
        })
    }
    
    sendMessage(msg){
        stompClient.send('/ldod-game/tags', {}, JSON.stringify({ userId: this.state.userId, msg: msg}));
    }
    
    getMessages(){
        return this.state.messages;
    }

    render() {
        return (
            <div>  
            </div>
        );
    }
}

export default withRouter(WebSockets);